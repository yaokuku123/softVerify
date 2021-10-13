package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.po.SoftInfo;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.service.SoftInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WYP
 * @date 2021-10-09 11:02
 */
@Service
public class SoftInfoServiceImpl implements SoftInfoService {

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Override
    public void insertSoft(SoftInfo softInfo) {
        softInfoDAO.insertSoft(softInfo);
    }
}
