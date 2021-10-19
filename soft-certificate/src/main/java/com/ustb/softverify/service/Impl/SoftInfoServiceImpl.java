package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.vo.UserSoftInfoVo;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.service.SoftInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WYP
 * @date 2021-10-19 15:40
 */
@Service
public class SoftInfoServiceImpl implements SoftInfoService {

    @Autowired
    private SoftInfoDAO softInfoDAO;

    @Override
    public List<UserSoftInfoVo> getUploadInfo(Integer govUserId) {
        return softInfoDAO.getUploadInfo(govUserId);
    }
}
