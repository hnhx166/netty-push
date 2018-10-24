package com.vinux.push.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.vinux.mq.Producer;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MQ_CHANNEL;

/**
 * 
 * 消息处理工具类型
*    
* 项目名称：netty-push   
* 类名称：MessageUtils   
* 类描述：   
* 创建人：guohaixiang  
* 创建时间：2018年10月24日 下午3:41:29   
* 修改人：Administrator   
* 修改时间：2018年10月24日 下午3:41:29   
* 修改备注：   
* @version 1.0
*
 */
public class MessageUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);
	
	
	/**
	 * 
	 * @param uid
	 * @param message
	 * @param cachePrefix
	 */
	public static void cacheMessage(String uid, Message message, String cachePrefix) {
		String cacheKey = cachePrefix + uid;
		cacheMessage(cacheKey, message);
	}

	/**
	 * 
	 * 缓存没有发送出去的消息
	 * 
	 * @param uid
	 * @param groupId
	 * @param message
	 * @param cachePrefix
	 */
	public static void cacheMessage(String uid, String groupId, Message message, String cachePrefix) {
		String cacheKey = cachePrefix + uid + "_" + groupId;
		cacheMessage(cacheKey, message);
	}

	/**
	 * 
	 * 缓存没有发送出去的消息
	 * 
	 * @param cacheKey 缓存key
	 * @param message 消息
	 */
	private static void cacheMessage(String cacheKey, Message message) {
		JSONObject cache = new JSONObject();
		cache.put("cacheKey", cacheKey);
		cache.put("message", message);
		
		try {
			Producer.pushMessage(cache, MQ_CHANNEL.CHANNEL_CACHE);
		}catch(Exception e){
			logger.error("保存消息出错：" + cache.toJSONString());
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 发送消息
	 * 
	 * @param message 消息主体
	 * @param serverSendTime 服务器收到并发送时间
	 * @param receiveTime 接收时间
	 * @param status 推送状态
	 */
	public static void saveMessage(Message message, Date serverSendTime, Date receiveTime, String status) {
		JSONObject pushData = new JSONObject();
		pushData.put("appId", message.getAppId());
		pushData.put("groupId", message.getGroupId());
		pushData.put("msg", message.getMsg());
		pushData.put("msgType", message.getMsgType());
		pushData.put("receiveId", message.getReceiveId());
		pushData.put("receiveType", message.getReceiveType());
		pushData.put("uid", message.getUid());
		pushData.put("version", message.getVersion());
		pushData.put("sendTime", message.getSendTime());
		pushData.put("serverSendTime", serverSendTime);
		pushData.put("receiveTime", receiveTime);
		pushData.put("sendTime", status);
		
		try {
			logger.info("开始推送消息：" + pushData.toJSONString());
			Producer.pushMessage(pushData, MQ_CHANNEL.CHANNEL_SAVE);
			logger.info("消息推送成功：" + pushData.toJSONString());
		} catch (Exception e) {
			logger.error("保存消息出错：" + pushData.toJSONString());
			e.printStackTrace();
		}
	}
}
