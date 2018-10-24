package com.vinux.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.vinux.push.enu.MQ_CHANNEL;

public class Producer {

	public final static String QUEUE_NAME="queue_netty";
	public final static String exchange = "";
	
    public static void pushMessage(JSONObject message, MQ_CHANNEL queueName) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        factory.setHost("192.168.0.150");
        factory.setUsername("tong");
        factory.setPassword("tong");
        factory.setVirtualHost("/");
        /*
         * 不需要重连，原因，可能会影响系统整体性能，
        factory.setAutomaticRecoveryEnabled(true);// 网络异常connection重连
		// To enable automatic consumers recovery
		factory.setTopologyRecoveryEnabled(true);//  网络异常consumers重连 
		factory.setNetworkRecoveryInterval(10000);// 10秒间隔重接
		*/
		factory.setRequestedHeartbeat(5);// 5秒心跳保持连接
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //  声明一个队列        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        String message = "Hello RabbitMQ";
//        JSONObject message = new JSONObject();
//        message.put("a", "你好啊");
        //发送消息到队列中
        channel.basicPublish("", queueName.getName(), null, message.toJSONString().getBytes("UTF-8"));
        System.out.println("Producer Send +'" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
