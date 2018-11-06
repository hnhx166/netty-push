package com.vinux.mq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.vinux.push.enu.MQ_CHANNEL;
import com.vinux.web.controller.PushMessageToSnBoxController;

public class MessageCustomer {

	private final static String QUEUE_NAME = MQ_CHANNEL.CHANNEL_PUSH_TO_SERVER.getName();

    public static void initCustomer() throws IOException, TimeoutException {
    	
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ地址
        factory.setHost("192.168.0.150");
        factory.setUsername("tong");
        factory.setPassword("tong");
        factory.setVirtualHost("/");
        factory.setAutomaticRecoveryEnabled(true);// 网络异常connection重连
		// To enable automatic consumers recovery
		factory.setTopologyRecoveryEnabled(true);//  网络异常consumers重连 
		factory.setNetworkRecoveryInterval(10000);// 10秒间隔重接
		factory.setRequestedHeartbeat(5);// 5秒心跳保持连接
        //创建一个新的连接
        Connection connection = factory.newConnection();
        //创建一个通道
        Channel channel = connection.createChannel();
        //声明要关注的队列
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);
        System.out.println("Customer Waiting Received messages");
        //DefaultConsumer类实现了Consumer接口，通过传入一个频道，
        // 告诉服务器我们需要那个频道的消息，如果频道中有消息，就会执行回调函数handleDelivery
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
            	JSONObject message = (JSONObject)JSONObject.parse(new String(body, "UTF-8"));
                System.out.println("Customer Received '" + message + "'");
                PushMessageToSnBoxController.pushToBox(message);
            }
        };
        //自动回复队列应答 -- RabbitMQ中的消息确认机制
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
