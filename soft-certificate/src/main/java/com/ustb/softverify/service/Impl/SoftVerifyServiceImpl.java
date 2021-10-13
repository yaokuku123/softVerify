package com.ustb.softverify.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.vo.PageRequest;
import com.ustb.softverify.entity.vo.PageResult;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.service.SoftVerifyService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class SoftVerifyServiceImpl implements SoftVerifyService {

    private static final String SOFT_PATH = "softPath";
    private static final String DOC_PATH = "docPath";

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${spring.rabbitmq.my-queue-name}")
    private String queueName;

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        Integer pageNum = pageRequest.getPageNum();
        Integer pageSize = pageRequest.getPageSize();
        List<SoftInfo> softInfoList = softInfoDAO.findByPager((pageNum - 1) * pageSize,pageSize);
        Long total = softInfoDAO.countUnVerifiedSoft();
        return new PageResult<>(pageNum, pageSize, softInfoList, total);
    }

    @Override
    public String getSoftPath(Integer govUserId, String softName) {
        return softInfoDAO.getSoftPath(govUserId,softName);
    }

    @Override
    public void updateSoftStatusToSuccess(Integer govUserId, String softName) {
        softInfoDAO.updateSoftStatusToSuccess(govUserId, softName);
    }

    @Override
    public void updateSoftStatusToFail(Integer govUserId, String softName) {
        softInfoDAO.updateSoftStatusToFail(govUserId, softName);
    }

    @Override
    public void deleteSoftAndDoc(Integer govUserId, String softName) {
        //查找两个文件的路径
        Map<String, String> pathMap = softInfoDAO.getSoftPathAndDocPath(govUserId, softName);
        Path softPath = Paths.get(pathMap.get(SOFT_PATH));
        Path docPath = Paths.get(pathMap.get(DOC_PATH));
        //删除软件压缩包和文档
        try {
            Files.deleteIfExists(softPath);
            Files.deleteIfExists(docPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageResult findPageSuccess(PageRequest pageRequest) {
        Integer pageNum = pageRequest.getPageNum();
        Integer pageSize = pageRequest.getPageSize();
        List<SoftInfo> softInfoList = softInfoDAO.findByPagerSuccess((pageNum - 1) * pageSize,pageSize);
        Long total = softInfoDAO.countVerifySuccess();
        return new PageResult<>(pageNum, pageSize, softInfoList, total);
    }

    @Override
    public PageResult findPageFail(PageRequest pageRequest) {
        Integer pageNum = pageRequest.getPageNum();
        Integer pageSize = pageRequest.getPageSize();
        List<SoftInfo> softInfoList = softInfoDAO.findByPagerFail((pageNum - 1) * pageSize,pageSize);
        Long total = softInfoDAO.countVerifyFail();
        return new PageResult<>(pageNum, pageSize, softInfoList, total);
    }

    @Override
    public void signAndUpChain(Integer govUserId, String softName) {
        //TODO 完善传输对象格式
        ObjectMapper mapper = new ObjectMapper();
        //RabbitMq工作模式
        amqpTemplate.convertAndSend("",queueName,"msg-hello world!");
    }

}
