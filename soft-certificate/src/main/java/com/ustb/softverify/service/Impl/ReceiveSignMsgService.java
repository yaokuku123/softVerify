package com.ustb.softverify.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.entity.dto.IdentityInfo;
import com.ustb.softverify.entity.dto.SignIdentityInfo;
import com.ustb.softverify.mapper.SignFileDAO;
import com.ustb.softverify.utils.EnvUtils;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@RabbitListener(queues = "sign-queue")
public class ReceiveSignMsgService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SignFileDAO signFileDAO;

    @RabbitHandler
    public void receiveMsg(String msg){
        try {
            //获取传输对象数据
            ObjectMapper mapper = new ObjectMapper();
            IdentityInfo identityInfo = mapper.readValue(msg, IdentityInfo.class);
            //获取需要签名文件路径
            List<SignIdentityInfo> signFilePathList = signFileDAO.listSignFilePath(identityInfo);
            //拼接得到全路径
            for (int i = 0; i < signFilePathList.size(); i++) {
                String absolutePath = EnvUtils.ROOT_PATH + identityInfo.getGovUserId() + "/"
                        + identityInfo.getSoftName() + "/" + signFilePathList.get(i).getPath();
                signFilePathList.get(i).setPath(absolutePath);
            }
            System.out.println(signFilePathList);
            //对每个文件进行签名
            for (SignIdentityInfo signIdentityInfo : signFilePathList) {
                PublicKey publicKey = signFile(signIdentityInfo,identityInfo);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对指定路径对文件进行签名处理
     * @param signIdentityInfo 文件路径和编号信息
     * @param identityInfo 用户标识和软件名称
     * @return 公钥信息
     */
    private PublicKey signFile(SignIdentityInfo signIdentityInfo,IdentityInfo identityInfo) {
        //签名
        String signPath = signIdentityInfo.getPath();
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(signPath);
        Map<String, Object> keyMap = algorithm.initParams();
        PublicKey publicKey = (PublicKey) keyMap.get(BlindAlgorithm.PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(signPath, publicKey ,
                (Element) keyMap.get(BlindAlgorithm.PRIVATE_KEY));
        //保存签名文件
        saveSignFile(signList,identityInfo,signIdentityInfo.getDocNumber());
        return publicKey;
    }

    /**
     * 保存签名文件
     * @param signList 签名列表
     * @param identityInfo 用户标识和软件名称
     * @param number 签名文件编号
     */
    private void saveSignFile(ArrayList<Element> signList,IdentityInfo identityInfo,Integer number) {
        //签名列表格式转换
        List<String> signStringList = new ArrayList<>();
        Base64.Encoder encoder = Base64.getEncoder();
        for (Element elm : signList) {
            byte[] signByte = encoder.encode(elm.toBytes());
            signStringList.add(new String(signByte, StandardCharsets.UTF_8));
        }
        //保存至指定目录位置

        String signFilePath = EnvUtils.ROOT_PATH + identityInfo.getGovUserId() + "/"
                + identityInfo.getSoftName() + "/" ;
    }
}
