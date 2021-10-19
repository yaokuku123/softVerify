package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.dto.SignFileInfo;
import com.ustb.softverify.entity.vo.SoftInfoVo;
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
    public List<SoftInfoVo> getUploadInfo(Integer govUserId) {
        return softInfoDAO.getUploadInfo(govUserId);
    }

    @Override
    public List<SoftInfoVo> getUnFiledSoftInfo() {
        return softInfoDAO.getUnFiledSoftInfo();
    }

    @Override
    public List<SoftInfoVo> getAllUploadInfo() {
        return softInfoDAO.getAllUploadInfo();
    }

    @Override
    public List<SignFileInfo> SignFileInfos(Integer govUserId) {
        return softInfoDAO.SignFileInfos(govUserId);
    }
}
