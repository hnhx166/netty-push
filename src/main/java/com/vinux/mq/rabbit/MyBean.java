//package com.vinux.mq.rabbit;
//
//import org.springframework.amqp.core.AmqpAdmin;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MyBean {
//
//	private final AmqpAdmin amqpAdmin;
//	private final AmqpTemplate amqpTemplate;
//
//	@Autowired
//	public MyBean(AmqpAdmin amqpAdmin, AmqpTemplate amqpTemplate) {
//		this.amqpAdmin = amqpAdmin;
//		this.amqpTemplate = amqpTemplate;
//	}
//	
//	//监听指定的队列
//	@RabbitListener(queues = "queue")
//	public void processMessage(String content) {
//		System.out.println();
//	}
//}
