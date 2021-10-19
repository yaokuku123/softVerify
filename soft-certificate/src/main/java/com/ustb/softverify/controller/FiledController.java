package com.ustb.softverify.controller;

import com.ustb.softverify.algorithm.blind.BlindAlgorithm;
import com.ustb.softverify.algorithm.blind.impl.BlindVerifyAlgorithmImpl1;
import com.ustb.softverify.domain.PublicKey;
import com.ustb.softverify.domain.ResponseResult;
import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
import com.ustb.softverify.exception.FileReadWriteException;
import com.ustb.softverify.service.SoftInfoService;
import com.ustb.softverify.utils.FileUtil;
import com.ustb.softverify.utils.ListStringUtils;
import it.unisa.dia.gas.jpbc.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * @author WYP
 * @date 2021-10-19 14:31
 */
@RestController
public class FiledController {

    @Autowired
    private SoftInfoService softInfoService;


    /**
     * 用户上传软件列表
     * @param govUserId
     * @return
     */
    @GetMapping("/userFileInfo")
    public ResponseResult getUserUploadInfo(@RequestParam("govUserId") Integer govUserId){
        List<SoftInfoVo> uploadInfo = softInfoService.getUploadInfo(govUserId);
        return ResponseResult.success().data("softInfo",uploadInfo);

    }


    /**
     * 已归档软件信息列表
     * @param
     * @return
     */
    @GetMapping("/fileInfos")
    public ResponseResult getAllUploadInfo(){
        List<SoftInfoVo> uploadInfo = softInfoService.getAllUploadInfo();
        return ResponseResult.success().data("softInfo",uploadInfo);

    }

    /**
     * 文件保存待归档软件信息列表
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
            String[] split = signFileInfo.getServerLocalName().split(".");
            String signFileName = signFileInfo.getServerLocalName() + ".sign";
            String signFilePath = "D:\\file\\" + signFileName;
            //将list集合变为String字符串后存储至指定路径下
            try {
                String str = ListStringUtils.listToString(signStringList);
                FileUtil.write(signFilePath, str);
            } catch (IOException e) {
                throw new FileReadWriteException();
            }
        }

        return ResponseResult.success().data("info",signFileInfos);
    }




}
