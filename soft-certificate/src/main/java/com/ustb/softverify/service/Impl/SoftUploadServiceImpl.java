package com.ustb.softverify.service.Impl;

import com.ustb.softverify.algorithm.sm3.SM3Algorithm;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.FileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.mapper.SignFileDAO;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.SoftUploadService;
import com.ustb.softverify.utils.FileUtil;
import com.ustb.softverify.utils.HashBasicOperaterSetUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author WYP
 * @date 2021-10-14 10:30
 */
@Service
@Transactional
public class SoftUploadServiceImpl implements SoftUploadService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Autowired
    private SignFileDAO signFileDAO;

    @Autowired
    private ZipCompressImpl zipCompress;

    @Autowired
    private ControlExcelImpl controlExcel;

    @Override
    public User insertUser(UserUploadInfoVo userUploadInfo) {

        User user = userDAO.getUser(userUploadInfo.getGovUserId());
        if (user == null){
            user = new User();
            BeanUtils.copyProperties(userUploadInfo,user);
            userDAO.insertUser(user);
        }
        return user;
    }

    @Override
    public User getUser(Integer govUserId) {
        return userDAO.getUser(govUserId);
    }

    @Override
    public void insertSoft(SoftInfo softInfo) {
        softInfoDAO.insertSoft(softInfo);
    }

    @Override
    public SoftInfo getSoftByUIdAndName(Integer uid, String softName) {
        return softInfoDAO.getSoftByUIdAndName(uid,softName);
    }

    @Override
    public void insert(SignFile signFile) {
        signFileDAO.insert(signFile);
    }

    @Override
    public void saveFile(MultipartFile[] files, UserUploadInfoVo userUploadInfoVo, FileInfo fileInfo) {

        String softName = fileInfo.getSoftName();
        String docName =fileInfo.getDocName();
        String filePath = fileInfo.getFilePath();

        //判断当前文件下是否有文件
        File currentFile = new File(filePath);
        currentFile.mkdirs();
        if (currentFile.list().length > 0 ){
            FileUtil.deleteDir(filePath);
        }

        String softDestPath = filePath + softName;
        String docDestPath = filePath + docName;
        File softDestFile = new File(softDestPath);
        File docDestFile = new File(docDestPath);
        try {
            files[0].transferTo(softDestFile);
            files[1].transferTo(docDestFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void verifyAndSave(FileInfo fileInfo,SoftInfo softInfo,UserUploadInfoVo userUploadInfo,User user) throws Exception {

        String softDestPath = fileInfo.getFilePath()+fileInfo.getSoftName();
        String docDestPath = fileInfo.getFilePath()+fileInfo.getDocName();


        String unzipName = zipCompress.unzip(softDestPath, fileInfo.getFilePath());
        String unzipFilePath = fileInfo.getFilePath() + unzipName;
        zipCompress.changeroot(unzipFilePath);

        List<Map<String, String>> maps = controlExcel.redExcel(docDestPath);
        List<String> excelPaths = new ArrayList<>();
        for (Map<String, String> map : maps){
            excelPaths.add(map.get("文件路径（相对路径）"));
        }

        ArrayList<File> allFiles = FileUtil.getAllFiles(unzipFilePath);
        ArrayList<String> allFilesPath = new ArrayList<>();
        for (File file : allFiles) {
            //file replace
            allFilesPath.add(file.getAbsolutePath().replace("\\","/"));
        }

        for (String s : excelPaths){
            String s1 = fileInfo.getFilePath() + s;
            if (!allFilesPath.contains(s1.replace("\\", "/"))){
                throw new RuntimeException("文件路径错误");
            }
        }

        String hash = HashBasicOperaterSetUtil.byteToHex(SM3Algorithm.SM3Encrypt(softDestPath));

        //存储软件相关信息
        softInfo.setSoftName(userUploadInfo.getSoftName()).setSoftPath(softDestPath).setDocPath(docDestPath).setStatus(0).setUser(user).setHash(hash);
        SoftInfo softDB = softInfoDAO.getSoftByUIdAndName(user.getUid(), userUploadInfo.getSoftName());
        if (softDB == null){
            softInfoDAO.insertSoft(softInfo);
        }

        //   存放读取正确路径
        SignFile signFile = new SignFile();
        signFile.setSoftInfo(softInfo);
        for (String s : excelPaths){
            signFile.setPath(s);
            signFileDAO.insert(signFile);
        }
    }
}