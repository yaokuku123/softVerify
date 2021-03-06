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
     * ?????????????????????????????????
     * @param govUserId
     * @return
     */
    @GetMapping("/userFileInfo")
    public ResponseResult getUserUploadInfo(@RequestParam("govUserId") Integer govUserId){
        List<SoftInfoVo> uploadInfo = softInfoService.getUploadInfo(govUserId);
        return ResponseResult.success().data("softInfo",uploadInfo);

    }


    /**
     * ???????????????????????????   status = 2
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
     * ??????
     * @param
     * @return
     */

    @GetMapping("/filed")
    public ResponseResult file(@RequestParam("pid")String pid) throws IOException {

        String softName = softInfoService.findSoftName(pid);

        // ??????pid  ???????????????????????????
        List<SignFileInfo> signFileInfos = softInfoService.SignFileInfos(pid);

        String signFilePath = EnvUtils.TmpFile +softName + ".bin";
        File signFile = new File(signFilePath);
        if (!signFile.getParentFile().exists()) { // ??????????????????????????????????????????
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

        //??????
        BlindAlgorithm algorithm = new BlindVerifyAlgorithmImpl1(signFilePath);
        Map<String, Object> keyMap = algorithm.initParams();
        PublicKey publicKey = (PublicKey) keyMap.get(BlindAlgorithm.PUBLIC_KEY);
        ArrayList<Element> signList = algorithm.sign(signFilePath, publicKey,
                (Element) keyMap.get(BlindAlgorithm.PRIVATE_KEY));

        //signFile.delete();

        //??????????????????   ????????????????????????
        List<String> signStringList = new ArrayList<>();
        Base64.Encoder encoder = Base64.getEncoder();
        for (Element elm : signList) {
            byte[] signByte = encoder.encode(elm.toBytes());
            signStringList.add(new String(signByte, StandardCharsets.UTF_8));
        }
        // ??????????????????
        String signFileName = pid + ".sign";
        File rootPath = new File(EnvUtils.ROOT_PATH);
        if (!rootPath.exists()){
            rootPath.mkdirs();
        }
        String signFilePathDes = EnvUtils.ROOT_PATH + signFileName;
        //???list????????????String????????????????????????????????????
        try {
            String str = ListStringUtils.listToString(signStringList);
            FileUtil.write(signFilePathDes, str);
        } catch (IOException e) {
            throw new FileReadWriteException();
        }
        //??????????????????????????????
        RemoteUtil.makeDir(pid);
        String remoteSignPath = EnvUtils.REMOTE_PATH + new SimpleDateFormat("yyyy").format(new Date()) +"/signFile/";
        ScpUtil.putFile(signFilePathDes, remoteSignPath);

        //??????????????????
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

        // ???????????????
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

        softInfoService.insertFingerCode(pid,fingerCode,new Date());


        List<SignFileInfo> fileRecords = softInfoService.softFileRecords(pid);

        //??????pid??????????????? ?????????????????????
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


        String path = EnvUtils.REMOTE_PATH;
        String zipPath = path + new SimpleDateFormat("yyyy").format(new Date()) + "/" +pid;
//        String remoteOriginPath = path + new SimpleDateFormat("yyyy").format(new Date()) + "/" +pid + "/original/";
//        String remoteArchivePath = path + new SimpleDateFormat("yyyy").format(new Date()) + "/" +pid + "/archive/";

        for (SignFileInfo signFileInfo : fileRecords ){
            //ScpUtil.putFile(signFileInfo.getFilePath() ,remoteOriginPath);
            FileUtil.copyFile(signFileInfo.getFilePath(), localOriginPath + signFileInfo.getFileName());
            String archiveName = pid + "type" + signFileInfo.getFileType() + ".bin";
            FileUtil.copyFile(signFileInfo.getFilePath(), localArchivePath + archiveName);
            //ScpUtil.putFile(localArchivePath + archiveName ,remoteArchivePath);
        }

        // ??????pid
        String zipOriginName = pid + "_o.zip";
        String zipArchiveName = pid + "_a.zip";
//        Random random = new Random();
//        int math = random.nextInt(1000000);
//        //System.out.println(math);
//        int i = math ^ 1010;
//        String password = String.valueOf(i);
        String password = "btsu_202";
        softInfoService.insertZipPwd(pid,MD5Utils.code(password));

        ZipDe.zipFile(localOriginPath,localPath+"/"+zipOriginName,password);
        ZipDe.zipFile(localArchivePath,localPath+"/"+zipArchiveName,password);

        ScpUtil.putFile(localPath+"/"+zipOriginName ,zipPath);
        ScpUtil.putFile(localPath+"/"+zipArchiveName ,zipPath);

        //??????status
        softInfoService.changeStatus(pid);
        softInfoService.insertPath(pid,zipPath +"/"+ zipOriginName,zipOriginName);
        return ResponseResult.success().data("fingerCode",fingerCode);
    }


    /**
     * ??????????????????
     * @param certificateInfo ????????????
     * @return ??????id
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



    @GetMapping(value = "/zipSoftDownload",produces = "application/json;charset=UTF-8")
    public ResponseResult zipDownload(@RequestParam("pid")String pid, HttpServletResponse response){
        SoftInfo softInfo = softInfoService.getSoftInfo(pid);

        File fileP = new File(EnvUtils.CERT_PATH);
        if (!fileP.exists()){
            fileP.mkdirs();
        }

        ScpUtil.getFile(softInfo.getSoftRemotePath() ,EnvUtils.CERT_PATH);

        //????????????
        File file = new File(EnvUtils.CERT_PATH + softInfo.getZipName() );

//        int decode = Integer.parseInt(softInfo.getZipPassword()) ^ 1010;
        ZipDe.unZipFile(EnvUtils.CERT_PATH + softInfo.getZipName(),EnvUtils.CERT_PATH,"btsu_202");
        file.delete();
        ZipDe.zip(EnvUtils.CERT_PATH,EnvUtils.CERT_PATH + softInfo.getZipName());


        // ???????????????????????????
        String fileName = (EnvUtils.CERT_PATH + softInfo.getZipName()).substring(EnvUtils.CERT_PATH.lastIndexOf("/") + 1);
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// ???????????????
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

            return ResponseResult.success().message("????????????");
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
        return ResponseResult.error().message("????????????");
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
                .setSoftVersion("1.0").setDate(new SimpleDateFormat("yyyy ??? MM ??? dd ???").format(soft.getGenerateTime()))
                .setSoftUi(soft.getVerificationCode());

        String saveName = EnvUtils.CSVTmp;
        File file = new File(saveName);
        if (!file.exists()){
            file.mkdir();
        }
        FileOutputStream fos = new FileOutputStream(saveName+ "templetedata.csv");
        OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("cert-ID", "app-name","soft-name","date","soft-ui");
        CSVPrinter csvPrinter = new CSVPrinter(osw, csvFormat);

        csvPrinter.printRecord(pid, pdfTemplete.getAppName(),pdfTemplete.getSoftName()
                ,pdfTemplete.getDate(),pdfTemplete.getSoftUi());

        csvPrinter.flush();
        csvPrinter.close();

        synchronized (this) {
            ScpUtil.putFile(saveName+"templetedata.csv" ,"/root/Certificat/");
            RemoteUtil.generatePdf(pid);
        }


        ScpUtil.getFile("/root/Certificat/Certificat.pdf" ,EnvUtils.TmpFile);

        File file1 = new File(EnvUtils.TmpFile + "Certificat.pdf");
        // ???????????????????????????
        String fileName = ("Certificat.pdf");
        response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// ???????????????
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
     * ??????excel????????????
     * @return
     */
    @GetMapping(value = "/excel",produces = "application/json;charset=UTF-8")
    public ResponseResult exportExcel(HttpServletResponse response) {
        //??????excel????????????
        String excelFilePath = softInfoService.getExcel();
        File excelFile = new File(excelFilePath);
        // ???????????????????????????
        response.addHeader("Content-Disposition", "attachment;fileName=" + excelFile.getName());// ???????????????
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
            //????????????excel??????
            FileUtil.delete(excelFilePath);
            return ResponseResult.success().message("????????????");
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
