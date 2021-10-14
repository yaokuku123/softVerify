package com.ustb.softverify;

import com.ustb.softverify.entity.po.User;
import com.ustb.softverify.service.Impl.ChainService;
import com.ustb.softverify.service.SoftVerifyService;
import com.ustb.softverify.utils.SerializeUtil;
import edu.ustb.shellchainapi.shellchain.command.ShellChainException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SoftVerifyServiceTest {
    @Autowired
    private SoftVerifyService service;

    @Autowired
    private ChainService chainService;

    @Value("${chainobj.address}")
    private String chainAddresses;

    User user = new User(1,"wyp","ustb","111",1,new ArrayList<>(),new ArrayList<>());





    @Test
    public void testSendMsg(){
        service.verifySuccess(1,"test");
    }


    // 上链   object
    @Test
    public void upToChain() throws ShellChainException, SQLException, ClassNotFoundException {
        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("UserInfo",user);
        attributes.put("SoftInfo","ustb");
       // String toAddress = "1MwnMpYGXy5TKJHMqiDy6MXorkQ5Nh6UoWCFEs";
        String txid = chainService.send2Obj(chainAddresses, 0, attributes);
        System.out.println(txid);

    }
}





