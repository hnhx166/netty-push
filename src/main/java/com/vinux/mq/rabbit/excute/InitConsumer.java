package com.vinux.mq.rabbit.excute;

import com.vinux.mq.rabbit.publics.MqPublicInfo;

/**
 * 
 * @author 初始化消费者
 * 
 */
public class InitConsumer {
	public static void executeCustomerInfo() {
		//初始化可能多个消费者
		MqPublicInfo.consumerMsg("queue_netty");
	}

	public void init() {
		executeCustomerInfo();
	}

	// how validate the destory method is a question
	public void cleanup() {
		System.out.println("cleanUp");
	}

}
