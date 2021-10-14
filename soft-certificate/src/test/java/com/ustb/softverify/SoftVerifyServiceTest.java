package com.ustb.softverify;

import com.ustb.softverify.service.SoftVerifyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SoftVerifyServiceTest {
    @Autowired
    private SoftVerifyService service;

    @Test
    public void testSendMsg(){
        service.verifySuccess(1,"test");
    }
}


