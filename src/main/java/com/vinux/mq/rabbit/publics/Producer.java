package com.vinux.mq.rabbit.publics;

import java.io.IOException;

import com.rabbitmq.client.MessageProperties;
import com.vinux.common.utils.SerializeUtil;

public class Producer extends EndPoint implements Runnable {

	protected Object obj;
	protected String routingKey;

	public Producer(String queueName, Object obj) {

		super(queueName);
		this.obj = obj;
	}
	
	public Producer(Object obj,String routingKey) {
		super("");
		this.routingKey = routingKey;
		this.obj = obj;
	}

	// 生产者发送消息
	public void sendMessage(Object object) throws IOException {
		if (null != routingKey && !routingKey.equals("")) {
			channel.exchangeDeclare(exchange, exchangeType);
			channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_BASIC, SerializeUtil.serialize(object));
		} 
		else {
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_BASIC, SerializeUtil.serialize(object));
		}
	}
	
	public void run() {
		try {
			sendMessage(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}