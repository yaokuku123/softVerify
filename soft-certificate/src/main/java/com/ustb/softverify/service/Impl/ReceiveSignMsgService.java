package com.ustb.softverify.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.vo.PublicKeyStr;
import com.ustb.softverify.entity.dto.CertificateInfo;
import com.ustb.softverify.entity.dto.IdentityInfo;
import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.dto.SignIdentityInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.mapper.SignFileDAO;
import com.ustb.softverify.utils.*;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

@Service
@RabbitListener(queues = "sign-queue")
public class ReceiveSignMsgService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SignFileDAO signFileDAO;

    @Autowired
    private ChainService chainService;

    @Value("${chainobj.address}")
    private String chainAddresses;

    @RabbitHandler
    public void receiveMsg(String msg){
        try {
            //获取传输对象数据
            ObjectMapper mapper = new ObjectMapper();
            IdentityInfo identityInfo = mapper.readValue(msg, IdentityInfo.class);
            //获取需要签名文件路径
            List<SignFile> signFilePathList = signFileDAO.listSignFilePath(identityInfo);
            //拼接得到全路径
            for (int i = 0; i < signFilePathList.size(); i++) {
                String absolutePath = EnvUtils.ROOT_PATH + identityInfo.getGovUserId() + "/"
                        + identityInfo.getSoftName() + "/" + signFilePathList.get(i).getPath();
                signFilePathList.get(i).setPath(absolutePath);
            }

            for (SignFile signFile : signFilePathList) {
                //对每个文件进行签名
                CertificateInfo certificateInfo = signFile(signFile, identityInfo);
                //证书上链
                String txid = upChain(certificateInfo);
                //交易id存储
                signFileDAO.updateTxidById(signFile.getFid(),txid);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对指定路径对文件进行签名处理
     * @param signFile 文件路径和编号信息
     * @param identityInfo 用户标识和软件名称
     * @return 公钥信息
     */
    private CertificateInfo signFile(SignFile signFile,IdentityInfo identityInfo) {
        //签名
        String signPath = signFile.getPath();
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(signPath);
        Map<String, Object> keyMap = algorithm.initParams();
        PublicKey publicKey = (PublicKey) keyMap.get(BlindAlgorithm.PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(signPath, publicKey ,
                (Element) keyMap.get(BlindAlgorithm.PRIVATE_KEY));
        //保存签名文件
        saveSignFile(signList,identityInfo, signFile.getDocNumber());
        //构造证书对象
        try {
            SignFileInfo signFileInfo = getSignFileInfo(signFile,identityInfo);
            PublicKeyStr publicKeyStr = PublicKeyTransferUtil.encodeToStr(publicKey);
            return new CertificateInfo(publicKeyStr,signFileInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取被签名文件的相关属性信息
     * @param signFile 文件路径和编号信息
     * @param identityInfo 用户标识和软件名称
     * @return 签名文件信息对象
     */
    private SignFileInfo getSignFileInfo(SignFile signFile,IdentityInfo identityInfo) {
        SignFileInfo signFileInfo = new SignFileInfo();
        File file = new File(signFile.getPath());
        signFileInfo.setGovUserId(identityInfo.getGovUserId().toString());
        signFileInfo.setFileName(identityInfo.getSoftName());
        signFileInfo.setFileSize(((Long)file.length()).toString());
        signFileInfo.setCreateTime(((Long)System.currentTimeMillis()).toString());
        return signFileInfo;
    }

    /**
     * 保存签名文件
     * @param signList 签名列表
     * @param identityInfo 用户标识和软件名称
     * @param number 签名文件编号
     */
    private void saveSignFile(ArrayList<Element> signList,IdentityInfo identityInfo,String number) {
        //签名列表格式转换
        List<String> signStringList = new ArrayList<>();
        Base64.Encoder encoder = Base64.getEncoder();
        for (Element elm : signList) {
            byte[] signByte = encoder.encode(elm.toBytes());
            signStringList.add(new String(signByte, StandardCharsets.UTF_8));
        }
        //指定文件路径
        String signFileName = number + ".sign";
        String signFilePath = EnvUtils.ROOT_PATH + identityInfo.getGovUserId() + "/"
                + identityInfo.getSoftName() + "/sign/" + signFileName;
        //将list集合变为String字符串后存储至指定路径下
        try {
            String str = ListStringUtils.listToString(signStringList);
            FileUtil.write(signFilePath, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 证书上链处理
     * @param certificateInfo 证书对象
     * @return 交易id
     */
    private String upChain(CertificateInfo certificateInfo) {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("certificateInfo",certificateInfo);
        String txid = null;
        try {
            txid = chainService.send2Obj(chainAddresses, 0, attributes);
            return txid;
        } catch (ShellChainException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
