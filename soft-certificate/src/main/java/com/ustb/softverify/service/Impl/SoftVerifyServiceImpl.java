package com.ustb.softverify.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.entity.dto.IdentityInfo;
import com.ustb.softverify.entity.dto.PageInfo;
import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.StatusEnum;
import com.ustb.softverify.entity.vo.PageRequest;
import com.ustb.softverify.entity.vo.PageResult;
import com.ustb.softverify.exception.JsonTransferException;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.SoftVerifyService;
import com.ustb.softverify.utils.FileUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SoftVerifyServiceImpl implements SoftVerifyService {

    @Autowired
    private UserDAO userDAO;

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
        List<SoftInfo> softInfoList = softInfoDAO.findByPager((pageNum - 1) * pageSize,pageSize,
                Arrays.asList(StatusEnum.UNVERIFIED.getCode(),StatusEnum.REJECTED.getCode()));
        //对象类型转换
        List<PageInfo> pageInfoList = new ArrayList<>();
        for (SoftInfo softInfo : softInfoList) {
            PageInfo pageInfo = new PageInfo();
            BeanUtils.copyProperties(softInfo,pageInfo);
            BeanUtils.copyProperties(softInfo.getUser(),pageInfo);
            pageInfoList.add(pageInfo);
        }
        Long total = softInfoDAO.countUnVerifiedSoft(Arrays.asList(StatusEnum.UNVERIFIED.getCode(),StatusEnum.REJECTED.getCode()));
        return new PageResult<>(pageNum, pageSize, pageInfoList, total);
    }

    @Override
    public String getSoftPath(Integer govUserId, String softName) {
        return softInfoDAO.getSoftPath(govUserId,softName);
    }


    @Override
    public PageResult findPageSuccess(PageRequest pageRequest) {
        Integer pageNum = pageRequest.getPageNum();
        Integer pageSize = pageRequest.getPageSize();
        List<SoftInfo> softInfoList = softInfoDAO.findByPagerSuccess((pageNum - 1) * pageSize,pageSize,StatusEnum.VERIFIED.getCode());
        //对象类型转换
        List<PageInfo> pageInfoList = new ArrayList<>();
        for (SoftInfo softInfo : softInfoList) {
            PageInfo pageInfo = new PageInfo();
            BeanUtils.copyProperties(softInfo,pageInfo);
            BeanUtils.copyProperties(softInfo.getUser(),pageInfo);
            pageInfoList.add(pageInfo);
        }
        Long total = softInfoDAO.countVerifySuccess(StatusEnum.VERIFIED.getCode());
        return new PageResult<>(pageNum, pageSize, pageInfoList, total);
    }

    @Override
    public PageResult findPageFail(PageRequest pageRequest) {
        Integer pageNum = pageRequest.getPageNum();
        Integer pageSize = pageRequest.getPageSize();
        List<SoftInfo> softInfoList = softInfoDAO.findByPagerFail((pageNum - 1) * pageSize,pageSize,StatusEnum.REJECTED.getCode());
        List<PageInfo> pageInfoList = new ArrayList<>();
        for (SoftInfo softInfo : softInfoList) {
            PageInfo pageInfo = new PageInfo();
            BeanUtils.copyProperties(softInfo,pageInfo);
            BeanUtils.copyProperties(softInfo.getUser(),pageInfo);
            pageInfoList.add(pageInfo);
        }
        Long total = softInfoDAO.countVerifyFail(StatusEnum.REJECTED.getCode());
        return new PageResult<>(pageNum, pageSize, pageInfoList, total);
    }

    @Transactional
    @Override
    public void verifySuccess(Integer govUserId, String softName) {
        //修改数据的状态信息(1-表示已审核)
        updateSoftStatusToSuccess(govUserId,softName);
        //签名和上链，采用队列异步处理
        signAndUpChain(govUserId,softName);
    }

    @Transactional
    @Override
    public void verifyFail(Integer govUserId, String softName) {
        //修改数据的状态信息并清除路径和hash信息(2-表示驳回)
        updateSoftStatusToFail(govUserId,softName);
    }

    /**
     * 对指定程序文件进行签名并上链
     * @param govUserId 用户标识
     * @param softName 软件名称
     * @return
     */
    private void signAndUpChain(Integer govUserId, String softName) {
        try {
            // 对象与json转换
            ObjectMapper mapper = new ObjectMapper();
            IdentityInfo identityInfo = new IdentityInfo(govUserId, softName);
            String s = mapper.writeValueAsString(identityInfo);
            //RabbitMq工作模式
            amqpTemplate.convertAndSend(queueName,s);
        } catch (JsonProcessingException e) {
            throw new JsonTransferException();
        }
    }

    /**
     * 修改软件状态为审核通过 待审核：0，审核通过：1，审核驳回：2
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    private void updateSoftStatusToSuccess(Integer govUserId, String softName) {
        softInfoDAO.updateSoftStatusToSuccess(govUserId, softName,StatusEnum.VERIFIED.getCode());
    }

    /**
     * 修改软件状态为审核驳回 待审核：0，审核通过：1，审核驳回：2
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    private void updateSoftStatusToFail(Integer govUserId, String softName) {
        softInfoDAO.updateSoftStatusToFail(govUserId, softName,StatusEnum.REJECTED.getCode());
    }

    /**
     * 删除指定路径下的文件
     * @param govUserId  用户标识
     * @param softName 软件名称
     */
    private void deleteUserFile(Integer govUserId, String softName) {
        //查找用户名
        String username = userDAO.getUsername(govUserId);
        //判断当前文件下是否有文件
        String filePath = System.getProperty("user.dir") + "/data/" + username + "/" + softName + "/";
        //判断当前文件下是否有文件
        File file = new File(filePath);
        FileUtil.deleteDir(filePath);
    }

}
