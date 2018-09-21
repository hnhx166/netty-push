package com.vinux.mq.rabbit.controller;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	//监听指定的队列
		@RabbitListener(queues = "queue")
		public void processMessage(String content) {
			System.out.println("接收消息：" + content);
		}
		
//		@RabbitListener(queues = "abc")
//		public void processabc(String content) {
//			System.out.println("接收消息：" + content);
//		}
}
