package com.vinux.mq.rabbit.excute;

import java.io.Serializable;
import java.util.Map;

import com.vinux.common.utils.SerializeUtil;
import com.vinux.mq.rabbit.publics.MqPublicInfo;
import com.vinux.push.entity.Message;



public class MQExecute implements Serializable {
	private static final long serialVersionUID = 5669902966340931851L;

	/**
	 * 
	 * 处理系统日志
	 * 
	 * @param object
	 *            队列存储对象
	 * @param queueName
	 *            队列名称
	 */
	@SuppressWarnings("unchecked")
	public static void excMqManager(byte[] body, String queueName) {

		// 队列名称 ： queueName
		// 队列类型 ： mqEntity.getType()
		/**
		 * 1.可以根据queueName[队列名称]进行判断要执行的业务。 2.如果一个队列里有多个业务，可以用mqEntity.getType() 来区分。
		 */
		// TODO --------------------------------
		try {
			// 接口中心异步通知
			if (queueName.equals("queue_netty")) {
				Message message = SerializeUtil.deserialize(body, Message.class);
				
				System.out.println(message);
			}
			// 樱桃铺推送服务
			if (queueName.equals("vinuxstorepushmsg")) {
				Map<String, Object> pushMap = SerializeUtil.deserialize(body, Map.class);
				// PushtoSingle.doPush(pushMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送关闭连接信号时重新创建消费者
	 */
	public static void recoverconsumer() {

	}

	

}
