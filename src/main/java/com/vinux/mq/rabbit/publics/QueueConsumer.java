package com.vinux.mq.rabbit.publics;

import java.io.IOException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import com.vinux.mq.rabbit.excute.MQExecute;

public class QueueConsumer extends EndPoint implements Runnable, Consumer {

	public QueueConsumer(String queueName) {
		super(queueName);
	}

	public void run() {
		try {
			channel.basicConsume(queueName, true, this);
			// channel.basicQos(1);
			// channel.basicConsume(queueName, false, this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when consumer is registered.
	 */
	public void handleConsumeOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + " registered");
	}

	/**
	 * Called when new message is available.
	 */
	public void handleDelivery(String consumerTag, Envelope env, BasicProperties props, byte[] body)
			throws IOException {
		MQExecute.excMqManager(body, queueName);
		// channel.basicAck(env.getDeliveryTag(), false);
	}

	public void handleCancel(String consumerTag) {
		System.out.println("Consumer " + consumerTag + "handleCancel");
	}

	public void handleCancelOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + "handleCancelOk");
	}

	public void handleRecoverOk(String consumerTag) {
		System.out.println("Consumer " + consumerTag + "handleRecoverOk");
	}

	public void handleShutdownSignal(String consumerTag, ShutdownSignalException arg1) {
		System.out.println("Consumer " + consumerTag + "handleShutdownSignal");
		MQExecute.recoverconsumer();
	}

}
