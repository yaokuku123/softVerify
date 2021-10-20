package com.ustb.softverify.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.domain.vo.PublicKeyStr;

import com.ustb.softverify.entity.dto.CertificateInfo;
import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.service.Impl.ChainService;
import com.ustb.softverify.service.SoftInfoService;
import com.ustb.softverify.utils.*;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WYP
 * @date 2021-10-19 14:31
 */
@RestController
public class FiledController {

    @Autowired
    private SoftInfoService softInfoService;

    @Autowired
    private ChainService chainService;

    @Value("${chainobj.address}")
    private String chainAddresses;


    /**
     * 用户上传软件已归档列表
     * @param govUserId
     * @return
     */
    @GetMapping("/userFileInfo")
    public ResponseResult getUserUploadInfo(@RequestParam("govUserId") Integer govUserId){
        List<SoftInfoVo> uploadInfo = softInfoService.getUploadInfo(govUserId);
        return ResponseResult.success().data("softInfo",uploadInfo);

    }


    /**
     * 已归档软件信息列表   status = 3
     * @param
     * @return
     */
    @GetMapping("/fileInfos")
    public ResponseResult getAllUploadInfo(){
        List<SoftInfoVo> uploadInfo = softInfoService.getAllUploadInfo();

        return ResponseResult.success().data("softInfo",uploadInfo);

    }

    /**
     * 文件保存待归档软件信息列表  status = 2
     * @param
     * @return
     */
    @GetMapping("/unFiledInfos")
    public ResponseResult getAllUnFiledInfo(){
        List<SoftInfoVo> uploadInfo = softInfoService.getUnFiledSoftInfo();
        return ResponseResult.success().data("softInfo",uploadInfo);

    }

    /**
     * 归档
     * @param
     * @return
     */

    @GetMapping("/filed")
    public ResponseResult file(@RequestParam("govUserId")Integer govUserId){
        // 根据gov sid 查询软件列表的路径
        List<SignFileInfo> signFileInfos = softInfoService.SignFileInfos(govUserId);
        for (SignFileInfo signFileInfo : signFileInfos){
            //签名
            BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(signFileInfo.getServerLocalPath());
            Map<String, Object> keyMap = algorithm.initParams();
            PublicKey publicKey = (PublicKey) keyMap.get(BlindAlgorithm.PUBLIC_KEY);
            ArrayList<Element> signList = algorithm.sign(signFileInfo.getServerLocalPath(), publicKey ,
                    (Element) keyMap.get(BlindAlgorithm.PRIVATE_KEY));



            //保存签名文件   签名列表格式转换
            List<String> signStringList = new ArrayList<>();
            Base64.Encoder encoder = Base64.getEncoder();
            for (Element elm : signList) {
                byte[] signByte = encoder.encode(elm.toBytes());
                signStringList.add(new String(signByte, StandardCharsets.UTF_8));
            }
            // 指定文件路径
            String signFileName = signFileInfo.getServerLocalName().split("\\.")[0] + ".sign";
            String signFilePath = EnvUtils.ROOT_PATH + signFileName;
            //将list集合变为String字符串后存储至指定路径下
            try {
                String str = ListStringUtils.listToString(signStringList);
                FileUtil.write(signFilePath, str);
            } catch (IOException e) {
                throw new FileReadWriteException();
            }
            //构造证书对象
            PublicKeyStr publicKeyStr = null;
            try {
                publicKeyStr = PublicKeyTransferUtil.encodeToStr(publicKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CertificateInfo certificateInfo =  new CertificateInfo(publicKeyStr,signFileInfo);
            String txid = upChain(certificateInfo);

            Integer softId = softInfoService.findSoftId(govUserId);
            softInfoService.insertSignFile(signFileInfo.getServerLocalName(),txid,softId);
            FileUtil.copyFile(signFileInfo.getServerLocalPath(),EnvUtils.ROOT_PATH + signFileInfo.getServerLocalName());
        }

        // 根据govId
        String softName = softInfoService.findSoftName(govUserId);
        //String jointPath = govUserId + "-" + softName + "-" + new SimpleDateFormat("yyyyMMss").format(new Date())  + ".zip";
        String zipName = govUserId + "-" + softName + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip";
        ZipDe.zip(EnvUtils.ROOT_PATH,EnvUtils.ROOT_PATH + "/" + zipName);

        //改变status
        softInfoService.changeStatus(govUserId);
        ScpUtil.putFile(EnvUtils.ROOT_PATH + "/" + zipName,"/root/scpTest");


        //根据softName保存
        String remotePath = "/root/scpTest/";
        softInfoService.insertPath(softName,remotePath,zipName);

        //删除
        FileUtil.deleteDir(EnvUtils.ROOT_PATH);
        return ResponseResult.success().data("info",signFileInfos);
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
            throw new CertificateUpChainException();
        }
    }

    @GetMapping(value = "/getInfo", produces = "application/json;charset=UTF-8")
    public ResponseResult getInfo(@RequestParam("sid")Integer sid, HttpServletResponse response){
        List<SignFile> signFiles = softInfoService.getTxid(sid);

        JSONObject jsonObject = new JSONObject();
        for (SignFile signFile : signFiles){
            try {
                String fromObj = chainService.getFromObj(signFile.getTxid());
                int i= 1;
                String jsonString = JSONObject.parseObject(fromObj).get("certificateInfo").toString();
                jsonObject.put(signFile.getFileName(),jsonString);
            } catch (ShellChainException e) {
                e.printStackTrace();
            }
        }

        SoftInfo softInfo = softInfoService.getSoftInfo(sid);
        String softPath = EnvUtils.CERT_PATH + softInfo.getGovUserId() + "-" + softInfo.getSoftName() +".txt";
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(softPath));
            writer.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //下载软件
        File file = new File(softPath);
        // 设置下载软件文件名
        String fileName = softPath.substring(softPath.lastIndexOf("/") + 1);
        // response.setContentType("application/json");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        OutputStream os = null;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ResponseResult.success().message("下载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseResult.error().message("下载失败");

    }

    @GetMapping(value = "/zipSoftDownload",produces = "application/json;charset=UTF-8")
    public ResponseResult zipDownload(@RequestParam("sid")Integer sid, HttpServletResponse response){

        SoftInfo softInfo = softInfoService.getSoftInfo(sid);

        ScpUtil.getFile(softInfo.getSoftRemotePath() + softInfo.getZipName(),EnvUtils.CERT_PATH);


        //下载软件
        File file = new File(EnvUtils.CERT_PATH + softInfo.getZipName() );
        // 设置下载软件文件名
        String fileName = (EnvUtils.CERT_PATH + softInfo.getZipName()).substring(EnvUtils.CERT_PATH.lastIndexOf("/") + 1);
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        OutputStream os = null;
        try (FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            return ResponseResult.success().message("下载成功");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseResult.error().message("下载失败");
    }




}
