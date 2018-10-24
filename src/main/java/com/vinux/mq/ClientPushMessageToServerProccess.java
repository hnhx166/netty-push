package com.vinux.mq;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class ClientPushMessageToServerProccess implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		//启动客户端推送数据消费者
		MessageCustomer.initCustomer();
	}

}
