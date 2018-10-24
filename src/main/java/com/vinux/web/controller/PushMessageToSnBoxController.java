package com.vinux.web.controller;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.constants.Contant_Prefix;
import com.vinux.push.entity.Config;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;
import com.vinux.push.enu.ReceiveType;
import com.vinux.push.utils.MessageUtils;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

public class PushMessageToSnBoxController {
	
	/**
	 * 
	 * 应用端向狻猊宝盒推送消息
	 * 
	 * @param message
	 */
	public static void pushToBox(JSONObject message) {
		
		String receiveId = message.getString("receiveId");
		Message boxMessage = new Message();
		boxMessage.setAppId(message.getString("appId"));
//		boxMessage.setGroupId(groupId);
		boxMessage.setMsg(message.getString("message"));
		boxMessage.setUid(message.getString("memberId"));
		boxMessage.setSendTime(message.getDate("sendTime"));
		
		boxMessage.setMsgType(MessageType.MSG_BOX_PUSH.getValue());
		boxMessage.setReceiveId(receiveId);
		boxMessage.setReceiveType(ReceiveType.TYPE_SERVER.getValue());
		boxMessage.setVersion(Config.SERVER_VERSION);
		Date serverSendTime = new Date(System.currentTimeMillis());
		
		ChannelHandlerContext ctx = SnBoxChannelCache.getChannel(receiveId);
		if(ctx != null) {
			
			if(ctx.isRemoved()) {
				ctx.close();
				SnBoxChannelCache.removeChannel(receiveId);
				MessageUtils.cacheMessage(receiveId, boxMessage, Contant_Prefix.PREFX_MESSAGE_SN_BOX);
				MessageUtils.saveMessage(boxMessage, serverSendTime, null, "300");
				return;
			}
			
			ctx.writeAndFlush(boxMessage).addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if(future.isSuccess()) {
						MessageUtils.saveMessage(boxMessage, serverSendTime, new Date(System.currentTimeMillis()), "200");
					}else {
						MessageUtils.saveMessage(boxMessage, serverSendTime, null, "300");
					}
				}
			});
		}else {
			SnBoxChannelCache.removeChannel(receiveId);
			MessageUtils.cacheMessage(receiveId, boxMessage, Contant_Prefix.PREFX_MESSAGE_SN_BOX);
			MessageUtils.saveMessage(boxMessage, serverSendTime, null, "300");
		}
	}
	
}
