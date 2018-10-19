package com.vinux.push.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.vinux.mq.Producer;
import com.vinux.push.cache.CacheService;
import com.vinux.push.cache.ChatChannelCache;
import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.constants.Contant_Prefix;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PushHandler extends SimpleChannelInboundHandler<Message> {
	
	String queueName = "queue_netty";

	// redis 使用
	@Autowired
	private CacheService<String, List<Message>> cacheService;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
		
		if (message != null) {
			JSONObject jData = new JSONObject();
			jData.put("appId", message.getAppId());
			jData.put("groupId", message.getGroupId());
			jData.put("msg", message.getMsg());
			jData.put("msgType", message.getMsgType());
			jData.put("receiveId", message.getReceiveId());
			jData.put("receiveType", message.getReceiveType());
			jData.put("uid", message.getUid());
			jData.put("version", message.getVersion());
			jData.put("sendTime", System.currentTimeMillis());
			
			
			
			System.out.println("server收到" + message.getMsg() + "的信息。");
			// 狻猊宝盒消息
			if (message.getMsgType() == MessageType.MSG_BOX_PUSH.getValue()) {
				// 发送人
				String sendId = message.getUid();
				if (null == SnBoxChannelCache.getChannel(sendId)) {
					SnBoxChannelCache.putChannel(sendId, ctx);
				}
				// 接收人
				String receiveId = message.getReceiveId();
				if (null == SnBoxChannelCache.getChannel(receiveId)) {// 不在线
					jData.put("status", 300);
					saveMessage(jData);
					// ChannelCache.channels.put(receiveId, ctx);
					cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SN_BOX);
					message.setMsg("对方不在线");
					ctx.writeAndFlush(message);
				} else {
					ChannelHandlerContext cc = SnBoxChannelCache.getChannel(receiveId);
					cc.writeAndFlush(message).addListener(new ChannelFutureListener() {

						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if(!future.isSuccess()) {
								jData.put("status", 300);
								cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SN_BOX);
							}else {
								jData.put("status", 200);
								jData.put("receiveTime", new Date(System.currentTimeMillis()));
							}
							saveMessage(jData);
						}
						
					});
				}
			} else if (message.getMsgType() == MessageType.MSG_CHAT_SINGLE_PUSH.getValue()) {// 单人聊天消息
				// 发送人
				String sendId = message.getUid();
				if (null == ChatChannelCache.getChannel(sendId)) {
					ChatChannelCache.putChannel(sendId, ctx);
				}

				// 接收人
				String receiveId = message.getReceiveId();
				if (null == ChatChannelCache.getChannel(receiveId)) {// 不在线
					message.setMsg("对方不在线");
					jData.put("status", 300);
					saveMessage(jData);
					// 将消息放入缓存
					cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SINGLE);
					ctx.writeAndFlush(message);
				} else {
					ChannelHandlerContext cc = ChatChannelCache.getChannel(receiveId);
					cc.writeAndFlush(message).addListener(new ChannelFutureListener() {

						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if (future.isSuccess()) {// 发送成功
								jData.put("status", 200);
								jData.put("receiveTime", new Date(System.currentTimeMillis()));
							} else {// 发送失败
								jData.put("status", 300);
								cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SINGLE);
							}
							saveMessage(jData);
						}

					});
				}
			} else if (message.getMsgType() == MessageType.MSG_CHAT_GROUP_PUSH.getValue()) {// 群聊消息
				// 发送人
				String sendId = message.getUid();
				if (null == ChatChannelCache.getChannel(sendId)) {
					ChatChannelCache.putChannel(sendId, ctx);
				}

				// 群组ID
				String goupId = message.getGroupId();
				
				//给用户自己先发消息
				//ctx.writeAndFlush(message);
				/**
				 * 获取Group全部
				 */
				List<String> groupUserList = new ArrayList<String>();// 群组成员
				for (String receiveId : groupUserList) {
					if(receiveId.equals(sendId))//不给自己发
						continue;
					ChannelHandlerContext guCtx = ChatChannelCache.getChannel(receiveId);
					if (guCtx != null) {
						guCtx.writeAndFlush(message).addListener(new ChannelFutureListener() {
							@Override
							public void operationComplete(ChannelFuture channerlFuture) {
								if (channerlFuture.isSuccess()) {
									jData.put("status", 200);
									jData.put("receiveTime", new Date(System.currentTimeMillis()));
									//发送成功不处理
								} else {//接收失败，存储消息
									jData.put("status", 300);
									cacheMessage(receiveId, goupId, message, Contant_Prefix.PREFX_MESSAGE_GROUP);
								}
								saveMessage(jData);
							}
						});
					} else {//不在线，存储消息
						jData.put("status", 300);
						cacheMessage(receiveId, goupId, message, Contant_Prefix.PREFX_MESSAGE_GROUP);
						saveMessage(jData);
					}
				}
			} else {
				ctx.fireChannelRead(message);
			}
		} else {
			ctx.fireChannelRead(message);
		}
	}

	private void cacheMessage(String uid, Message message, String cachePrefix) {
		String cacheKey = cachePrefix + uid;
		cacheMessage(cacheKey, message);
	}

	private void cacheMessage(String uid, String groupId, Message message, String cachePrefix) {
		String cacheKey = cachePrefix + uid + "_" + groupId;
		cacheMessage(cacheKey, message);
	}

	private void cacheMessage(String cacheKey, Message message) {
		
		List<Message> cacheMessage = null;//cacheService.get(cacheKey);
		if (null == cacheMessage)
			cacheMessage = new ArrayList<Message>();
		cacheMessage.add(message);
//		cacheService.put(cacheKey, cacheMessage);
	}
	
	//发送消息
	private void saveMessage(JSONObject message) {
//		MqPublicInfo.processMsg(message, queueName);
		try {
			Producer.pushMessage(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
