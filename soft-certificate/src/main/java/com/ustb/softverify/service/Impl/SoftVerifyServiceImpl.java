package com.ustb.softverify.service.Impl;

import com.ustb.softverify.entity.SoftInfo;
import com.ustb.softverify.entity.VO.PageRequest;
import com.ustb.softverify.entity.VO.PageResult;
import com.ustb.softverify.mapper.SoftVerifyDao;
import com.ustb.softverify.service.SoftVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SoftVerifyServiceImpl implements SoftVerifyService {

    @Autowired
    private SoftVerifyDao softVerifyDao;

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        Integer pageNum = pageRequest.getPageNum();
        Integer pageSize = pageRequest.getPageSize();
        List<SoftInfo> softInfoList = softVerifyDao.findByPager((pageNum - 1) * pageSize,pageSize);
        Long total = softVerifyDao.countUnVerifiedSoft();
        return new PageResult<>(pageNum, pageSize, softInfoList, total);
    }

}
