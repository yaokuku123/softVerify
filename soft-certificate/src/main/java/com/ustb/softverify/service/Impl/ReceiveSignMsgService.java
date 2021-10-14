package com.ustb.softverify.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ustb.softverify.entity.dto.IdentityInfo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RabbitListener(queues = "sign-queue")
public class ReceiveSignMsgService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitHandler
    public void receiveMsg(String msg){
        try {
            //获取传输对象数据
            ObjectMapper mapper = new ObjectMapper();
            IdentityInfo identityInfo = mapper.readValue(msg, IdentityInfo.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
