package com.ustb.softverify.controller;

import ch.ethz.ssh2.Connection;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.domain.vo.PublicKeyStr;

import com.ustb.softverify.entity.dto.CertificateInfo;
import com.ustb.softverify.entity.dto.CheckPwd;
import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import com.ustb.softverify.exception.CertificateUpChainException;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.service.Impl.ChainService;
import com.ustb.softverify.service.Impl.ShellTools;
import com.ustb.softverify.service.SoftInfoService;
import com.ustb.softverify.utils.*;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

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
@CrossOrigin
public class FiledController {

    @Autowired
    private SoftInfoService softInfoService;

    @Autowired
    private ChainService chainService;

    @Value("${chainobj.address}")
    private String chainAddresses;

    @Autowired
    private ShellTools shellTools;


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
     * 已归档软件信息列表   status = 2
     * @param
     * @return
     */
    @GetMapping("/fileInfos")
    public ResponseResult getAllUploadInfo(){
        List<SoftInfoVo> uploadInfo = softInfoService.getAllUploadInfo();

        return ResponseResult.success().data("softInfo",uploadInfo);

    }

    /**
     *
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
    public ResponseResult file(@RequestParam("pid")String pid) throws IOException {

        String softName = softInfoService.findSoftName(pid);

        // 根据pid  查询软件列表的路径
        List<SignFileInfo> signFileInfos = softInfoService.SignFileInfos(pid);
        String signFilePath = EnvUtils.TmpFile +softName + ".bin";
        File signFile = new File(signFilePath);
        try {
            signFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (SignFileInfo signFileInfo : signFileInfos){
            String localPath = signFileInfo.getFilePath();
            try {
                FileUtil.append(signFilePath,FileUtil.read(localPath));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //签名
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(signFilePath);
        Map<String, Object> keyMap = algorithm.initParams();
        PublicKey publicKey = (PublicKey) keyMap.get(BlindAlgorithm.PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(signFilePath, publicKey,
                (Element) keyMap.get(BlindAlgorithm.PRIVATE_KEY));

        //signFile.delete();

        //保存签名文件   签名列表格式转换
        List<String> signStringList = new ArrayList<>();
        Base64.Encoder encoder = Base64.getEncoder();
        for (Element elm : signList) {
            byte[] signByte = encoder.encode(elm.toBytes());
            signStringList.add(new String(signByte, StandardCharsets.UTF_8));
        }
        // 指定文件路径
        String signFileName = pid + ".sign";
        String signFilePathDes = EnvUtils.ROOT_PATH + signFileName;
        //将list集合变为String字符串后存储至指定路径下
        try {
            String str = ListStringUtils.listToString(signStringList);
            FileUtil.write(signFilePathDes, str);
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
        SoftInfo softDetail = softInfoService.getSoftDetail(pid);
        CertificateInfo certificateInfo = new CertificateInfo(publicKeyStr, softDetail);
        String txid = upChain(certificateInfo);
        softInfoService.insertTxid(pid,txid);

        List<SignFileInfo> fileRecords = softInfoService.softFileRecords(pid);
        //根据pid创建文件夹 相应两个文件夹
        String localPath = EnvUtils.TmpSoftware + "/data/" + new SimpleDateFormat("yyyy").format(new Date()) + "/" + pid;
        String localOriginPath = localPath + "/original/";
        String localArchivePath = localPath + "/archive/";
        File originFile = new File(localOriginPath);
        File archiveFile = new File(localArchivePath);
        if (!originFile.exists()){
            originFile.mkdirs();
        }
        if (!archiveFile.exists()){
            archiveFile.mkdirs();
        }

        RemoteUtil.makeDir(pid);
        String path = "/root/TmpSoftware/";
        String zipPath = path + new SimpleDateFormat("yyyy").format(new Date()) + "/" +pid;
        String remoteOriginPath = path + new SimpleDateFormat("yyyy").format(new Date()) + "/" +pid + "/original/";
        String remoteArchivePath = path + new SimpleDateFormat("yyyy").format(new Date()) + "/" +pid + "/archive/";

        for (SignFileInfo signFileInfo : fileRecords ){
            ScpUtil.putFile(signFileInfo.getFilePath() ,remoteOriginPath);
            FileUtil.copyFile(signFileInfo.getFilePath(), localOriginPath + signFileInfo.getFileName());
            String archiveName = pid + "type" + signFileInfo.getFileType();
            FileUtil.copyFile(signFileInfo.getFilePath(), localArchivePath + archiveName);
            ScpUtil.putFile(localArchivePath + archiveName ,remoteArchivePath);
        }

        // 根据pid
        String zipOriginName = pid + "_o.zip";
        String zipArchiveName = pid + "_a.zip";
        Random random = new Random();
        int math = random.nextInt(1000000);
        //System.out.println(math);
        int i = math ^ 1010;
        String password = String.valueOf(i);
        softInfoService.insertZipPwd(pid,password);

        ZipDe.zipFile(localOriginPath,localPath+"/"+zipOriginName,String.valueOf(math));
        ZipDe.zipFile(localArchivePath,localPath+"/"+zipArchiveName,String.valueOf(math));

        ScpUtil.putFile(localPath+"/"+zipOriginName ,zipPath);
        ScpUtil.putFile(localPath+"/"+zipArchiveName ,zipPath);

        //改变status
        softInfoService.changeStatus(pid);
        softInfoService.insertPath(pid,zipPath +"/"+ zipOriginName,zipOriginName);
        return ResponseResult.success().message("归档完成");
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

//    @GetMapping(value = "/getInfo", produces = "application/json;charset=UTF-8")
//    public ResponseResult getInfo(@RequestParam("govUserId")Integer govUserId, HttpServletResponse response){
//        Integer sid = softInfoService.getSid(govUserId);
//        List<SignFile> signFiles = softInfoService.getTxid(sid);
//
//        JSONObject jsonObject = new JSONObject();
//        for (SignFile signFile : signFiles){
//            try {
//                String fromObj = chainService.getFromObj(signFile.getTxid());
//                int i= 1;
//                String jsonString = JSONObject.parseObject(fromObj).get("certificateInfo").toString();
//                jsonObject.put(signFile.getFileName(),jsonString);
//            } catch (ShellChainException e) {
//                e.printStackTrace();
//            }
//        }
//
//        SoftInfo softInfo = softInfoService.getSoftInfo(sid);
//        File fileP = new File(EnvUtils.CERT_PATH);
//        if (!fileP.exists()){
//            fileP.mkdirs();
//        }
//        String softPath = EnvUtils.CERT_PATH + softInfo.getGovUserId() + "-" + softInfo.getSoftName() +".txt";
//        try {
//            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(softPath));
//            writer.write(jsonObject.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //下载软件
//        File file = new File(softPath);
//        // 设置下载软件文件名
//        String fileName = softPath.substring(softPath.lastIndexOf("/") + 1);
//        // response.setContentType("application/json");// 设置强制下载不打开
//        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
//        OutputStream os = null;
//        try (FileInputStream fis = new FileInputStream(file);
//             BufferedInputStream bis = new BufferedInputStream(fis)) {
//            os = response.getOutputStream();
//            byte[] buffer = new byte[1024];
//            int i = bis.read(buffer);
//            while (i != -1) {
//                os.write(buffer, 0, i);
//                i = bis.read(buffer);
//            }
//            return ResponseResult.success().message("下载成功");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            try {
//                os.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return ResponseResult.error().message("下载失败");
//
//    }


    @GetMapping(value = "/zipSoftDownload",produces = "application/json;charset=UTF-8")
    public ResponseResult zipDownload(@RequestParam("pid")String pid, HttpServletResponse response){
        SoftInfo softInfo = softInfoService.getSoftInfo(pid);

        File fileP = new File(EnvUtils.CERT_PATH);
        if (!fileP.exists()){
            fileP.mkdirs();
        }
//        if (!MD5Utils.md5Hex(uploadPassword).equals(softInfo.getUploadPassword())){
//            return ResponseResult.error().message("密码错误，请重新输入");
//        }


        ScpUtil.getFile(softInfo.getSoftRemotePath() ,EnvUtils.CERT_PATH);

        //下载软件
        File file = new File(EnvUtils.CERT_PATH + softInfo.getZipName() );

        int decode = Integer.parseInt(softInfo.getZipPassword()) ^ 1010;
        ZipDe.unZipFile(EnvUtils.CERT_PATH + softInfo.getZipName(),EnvUtils.CERT_PATH,String.valueOf(decode));
        file.delete();
        ZipDe.zip(EnvUtils.CERT_PATH,EnvUtils.CERT_PATH + softInfo.getZipName());



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
            FileUtil.deleteDir(EnvUtils.CERT_PATH);
        }
        return ResponseResult.error().message("下载失败");
    }

    @PostMapping("/check")
    public ResponseResult checkPwd(@RequestBody CheckPwd checkPwd){
        SoftInfo softInfo = softInfoService.getSoftInfo(checkPwd.getPid());
        if (!MD5Utils.md5Hex(checkPwd.getPassword()).equals(softInfo.getUploadPassword())){

            return ResponseResult.error().data("result",false);
        }
        return ResponseResult.success().data("result",true);
    }
}
