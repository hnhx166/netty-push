package com.vinux.mq.rabbit.controller;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Sender {

	@Autowired
    private AmqpTemplate template;
    
	@RequestMapping("send")
    public void send(String msg) {
		
    	template.convertAndSend("queue", msg);
    	System.out.println("发送消息：" + msg);
    }
}
