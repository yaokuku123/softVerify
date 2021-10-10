package com.ustb.softverify.service.Impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RabbitListener(queues = "sign-queue")
public class ReceiveSignMsgService {
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RabbitHandler
    public void receiveMsg(String msg){
        //TODO 处理文件签名和上链的逻辑
        System.out.println("receive msg: "+ msg);
    }
}
