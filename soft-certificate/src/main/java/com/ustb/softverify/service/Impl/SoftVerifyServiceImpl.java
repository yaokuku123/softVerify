package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.SoftInfo;
import com.ustb.softverify.entity.VO.PageRequest;
import com.ustb.softverify.entity.VO.PageResult;
import com.ustb.softverify.mapper.SoftInfoDAO;
import com.ustb.softverify.service.SoftVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoftVerifyServiceImpl implements SoftVerifyService {

    @Autowired
    private SoftInfoDAO softInfoDAO;

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

}
