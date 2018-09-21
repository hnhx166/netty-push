package com.vinux.push.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.vinux.dao.entity.PushMessage;
import com.vinux.push.cache.CacheService;
import com.vinux.push.cache.ChatChannelCache;
import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.constants.Contant_Prefix;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;
import com.vinux.service.PushMessageService;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PushHandler extends SimpleChannelInboundHandler<Message> {

	@Autowired
	PushMessageService pushMessageService;

	// redis 使用
	@Autowired
	private CacheService<String, List<Message>> cacheService;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
		
		if (message != null && message.getMsgType() == MessageType.MSG_BOX_PUSH.getValue()) {//宝盒消息
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
								cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SN_BOX);
							}
							
						}
						
					});
				}
//				// TODO start 此处以后放到MQ中处理
//				PushMessage pushMessage = new PushMessage();
//				pushMessage.setAppId(message.getAppId());
//				pushMessage.setMessage(message.getMsg());
//				pushMessage.setMessageType((int) message.getMsgType());
//				pushMessage.setReceiverId(message.getReceiveId());
//				pushMessage.setSendId(message.getUid());
//				pushMessage.setVersion(message.getVersion());
//				pushMessage.setStatus(status);
//				// 数据库执行信息
//				status = pushMessageService.insert(pushMessage);
//				if (status > 0) {// 数据库记录成功
//					System.out.println("消息插入数据库成功");
//				} else {// 数据库记录失败，记入日志，以备后续处理
//					System.out.println("消息插入数据库失败，消息内容：" + JSONObject.toJSON(pushMessage));
//				}
				// TODO end
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

					// 将消息放入缓存
					cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SINGLE);
					ctx.writeAndFlush(message);
					// TODO 以下需要处理未发送消息
					//
					//
				} else {
					ChannelHandlerContext cc = ChatChannelCache.getChannel(receiveId);
					cc.writeAndFlush(message).addListener(new ChannelFutureListener() {

						@Override
						public void operationComplete(ChannelFuture future) throws Exception {
							if (future.isSuccess()) {// 发送成功
//								ctx.writeAndFlush(message);
							} else {// 发送失败
//								message.setMsg("发送失败");
								cacheMessage(receiveId, message, Contant_Prefix.PREFX_MESSAGE_SINGLE);
//								ctx.writeAndFlush(message);
							}
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
									//发送成功不处理
								} else {//接收失败，存储消息
									cacheMessage(receiveId, goupId, message, Contant_Prefix.PREFX_MESSAGE_GROUP);
								}
							}
						});
					} else {//不在线，存储消息
						cacheMessage(receiveId, goupId, message, Contant_Prefix.PREFX_MESSAGE_GROUP);
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
		
		List<Message> cacheMessage = cacheService.get(cacheKey);
		if (null == cacheMessage)
			cacheMessage = new ArrayList<Message>();
		cacheMessage.add(message);
		cacheService.put(cacheKey, cacheMessage);
	}
}
