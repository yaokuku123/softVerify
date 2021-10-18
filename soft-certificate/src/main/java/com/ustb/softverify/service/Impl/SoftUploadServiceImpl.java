package com.ustb.softverify.service.Impl;

import com.ustb.softverify.algorithm.sm3.SM3Algorithm;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.FileInfo;
import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.entity.vo.UserUploadInfoVo;
import com.ustb.softverify.exception.DocPathMisMatchException;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.mapper.SignFileDAO;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.SoftUploadService;
import com.ustb.softverify.utils.FileUtil;
import com.ustb.softverify.utils.HashBasicOperaterSetUtil;
import com.ustb.softverify.utils.ZipUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            throw new FileReadWriteException();
        }
    }

    @Override
    public void verifyAndSave(FileInfo fileInfo,SoftInfo softInfo,UserUploadInfoVo userUploadInfo,User user) {
        try {
            String softDestPath = fileInfo.getFilePath() + fileInfo.getSoftName();
            String docDestPath = fileInfo.getFilePath() + fileInfo.getDocName();
            String unzipFilePath = fileInfo.getFilePath() + "file/";
            ZipUtil.decompressZip(softDestPath,unzipFilePath);
            zipCompress.changeroot(unzipFilePath);
            List<Map<String, String>> maps = controlExcel.redExcel(docDestPath);
            List<String> excelPaths = new ArrayList<>();
            List<String> docNumbers = new ArrayList<>();
            List<String> docTypes = new ArrayList<>();
            List<String> docDescs = new ArrayList<>();
            for (Map<String, String> map : maps) {
                excelPaths.add(map.get("文件路径（相对路径）"));
                docNumbers.add(map.get("编号"));
                docTypes.add(map.get("文件类型"));
                docDescs.add(map.get("文件说明"));
            }
            ArrayList<File> allFiles = FileUtil.getAllFiles(unzipFilePath);
            ArrayList<String> allFilesPath = new ArrayList<>();
            for (File file : allFiles) {
                //file replace
                allFilesPath.add(file.getAbsolutePath().replace("\\", "/"));
            }

            for (String s : excelPaths) {
                String s1 = unzipFilePath + s;
                if (!allFilesPath.contains(s1.replace("\\", "/"))) {
                    throw new DocPathMisMatchException();
                }
            }

            String hash = HashBasicOperaterSetUtil.byteToHex(SM3Algorithm.SM3Encrypt(softDestPath));

            //存储软件相关信息
            softInfo.setSoftName(userUploadInfo.getSoftName()).setSoftPath(softDestPath).setDocPath(docDestPath).setStatus(0).setUser(user).setHash(hash);
            SoftInfo softDB = softInfoDAO.getSoftByUIdAndName(user.getUid(), userUploadInfo.getSoftName());
            if (softDB == null) {
                softInfoDAO.insertSoft(softInfo);
            } else {
                softInfo.setSid(softDB.getSid());
            }

            for (int i = 0; i < excelPaths.size(); i++) {
                SignFile signFile = new SignFile();
                signFile.setSoftInfo(softInfo).setPath(excelPaths.get(i)).setDocNumber(docNumbers.get(i)).setDocType(docTypes.get(i)).setDocDesc(docDescs.get(i));
                signFileDAO.insert(signFile);
            }
        } catch (Exception e) {
            throw new DocPathMisMatchException();
        }
    }

    @Override
    public void clear(Integer uid, String softName) {
        softInfoDAO.clear(uid,softName);
    }
}
