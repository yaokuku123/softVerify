package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.mapper.UserDAO;
import com.ustb.softverify.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Override
    public User getUser(Integer govUserId) {
        return userDAO.getUser(govUserId);
    }

    @Override
    public List<SoftInfo> listSoft(Integer govUserId) {
        return softInfoDAO.listSoft(govUserId);
    }

    @Override
    public void insertUser(User user) {
        userDAO.insertUser(user);
    }

    @Override
    public void insertSoft(SoftInfo softInfo) {
        softInfoDAO.insertSoft(softInfo);
    }

    @Override
    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    @Override
    public void updateSoft(SoftInfo softInfo) {
        softInfoDAO.updateSoft(softInfo);
    }

    @Override
    public void updateStatus(Integer govUserId, Integer status) {
        softInfoDAO.updateStatus(govUserId,status);
    }
}
