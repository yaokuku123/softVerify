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
import com.ustb.softverify.entity.dto.PdfTemplete;
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
import com.ustb.softverify.utils.checkcode.CheckCode;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import it.unisa.dia.gas.jpbc.Element;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadFactory;

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
        List<SoftInfo> allSoft = softInfoService.getAllSoft();
        return ResponseResult.success().data("softInfo",allSoft);

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
        if (!signFile.getParentFile().exists()) { // 如果父目录不存在，创建父目录
            signFile.getParentFile().mkdirs();
        }
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

        // 生成核验码
        String fingerCode = "";
        if (signFileInfos.size() == 1){
            fingerCode = CheckCode.getFingerCode(signFileInfos.get(0).getFilePath(), txid);
        }else if(signFileInfos.size() == 2){
            String firstPath = "";
            String secondPath = "";
            for (SignFileInfo signFileInfo :signFileInfos){
                if (signFileInfo.getFileType().equals(1)){
                    firstPath = signFileInfo.getFilePath();
                }
                if (!signFileInfo.getFileType().equals(1)){
                    secondPath = signFileInfo.getFilePath();
                }
            }
            fingerCode = CheckCode.getFingerCode(firstPath, secondPath, txid);

        }else if(signFileInfos.size() == 3){
            String firstPath = "";
            String secondPath = "";
            String thirdPath = "";
            for (SignFileInfo signFileInfo :signFileInfos){
                if (signFileInfo.getFileType().equals(1)){
                    firstPath = signFileInfo.getFilePath();
                }
                if (signFileInfo.getFileType().equals(2)){
                    secondPath = signFileInfo.getFilePath();
                }
                if (signFileInfo.getFileType().equals(3)){
                    thirdPath = signFileInfo.getFilePath();
                }
            }

            fingerCode = CheckCode.getFingerCode(firstPath, secondPath, thirdPath, txid);

        }

        softInfoService.insertFingerCode(pid,fingerCode);



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
        return ResponseResult.success().data("fingerCode",fingerCode);
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



    @GetMapping("/generatePDF")
    public ResponseResult generatePDF(@RequestParam("pid") String pid,HttpServletResponse response) throws Exception {

        SoftInfo soft = softInfoService.getSoftDetail(pid);
        PdfTemplete pdfTemplete = new PdfTemplete();
        Random random = new Random();
        pdfTemplete.setCertId(random.nextInt(10000))
                .setAppName(soft.getComName()).setSoftName(soft.getProName())
                .setSoftVersion("1.0").setDate(new SimpleDateFormat("yyyy 年 MM 月 dd 日").format(new Date()))
                .setSoftUi(soft.getVerificationCode());

        String saveName = EnvUtils.CSVTmp;
        File file = new File(saveName);
        if (!file.exists()){
            file.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(saveName+ "templetedata.csv");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("cert-ID", "app-name","soft-name","soft-version","date","soft-ui");
        CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);

        csvPrinter.printRecord(pid, pdfTemplete.getAppName(),pdfTemplete.getSoftName()
                ,pdfTemplete.getSoftVersion(),pdfTemplete.getDate(),pdfTemplete.getSoftUi());

        csvPrinter.flush();
        csvPrinter.close();

        //String csvTitle =

        ScpUtil.putFile(saveName+"templetedata.csv" ,"/root/Certificat/");

        RemoteUtil.generatePdf(pid);

        //Thread.sleep(3000);

        ScpUtil.getFile("/root/Certificat/Certificat.pdf" ,EnvUtils.TmpFile);

        File file1 = new File(EnvUtils.TmpFile + "Certificat.pdf");
        // 设置下载软件文件名
        String fileName = ("Certificat.pdf");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
        OutputStream os = null;
        try (FileInputStream fis = new FileInputStream(file1);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
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

        return ResponseResult.success().data("data",1);
    }

    /**
     * 获取excel导出数据
     * @return
     */
    @GetMapping(value = "/excel",produces = "application/json;charset=UTF-8")
    public ResponseResult exportExcel(HttpServletResponse response) {
        //获取excel文件路径
        String excelFilePath = softInfoService.getExcel();
        File excelFile = new File(excelFilePath);
        // 设置下载软件文件名
        response.addHeader("Content-Disposition", "attachment;fileName=" + excelFile.getName());// 设置文件名
        OutputStream os = null;
        try (FileInputStream fis = new FileInputStream(excelFile);
             BufferedInputStream bis = new BufferedInputStream(fis)) {
            os = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
            //删除临时excel文件
            FileUtil.delete(excelFilePath);
            return ResponseResult.success().message("下载成功");
        } catch (Exception e) {
            throw new FileReadWriteException();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                throw new FileReadWriteException();
            }
        }
    }


}
