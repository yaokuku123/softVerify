package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.po.SignFile;
import com.ustb.softverify.mapper.SignFileDAO;
import com.ustb.softverify.service.SignFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author WYP
 * @date 2021-10-11 10:19
 */
@Service
public class SignFileServiceImpl implements SignFileService {

    @Autowired
    private SignFileDAO signFileDAO;

    @Override
    public void insert(SignFile signFile) {
        signFileDAO.insert(signFile);
    }
}
