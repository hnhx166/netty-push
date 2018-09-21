package com.vinux.push.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.vinux.push.cache.ChatChannelCache;
import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;
import com.vinux.service.PushMessageService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class UserInfoHandler extends SimpleChannelInboundHandler<Message>{

	@Autowired
	PushMessageService pushMessageService;
	
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        
        //处理用户信息
        if(message != null){
        	if(message.getMsgType() == MessageType.MSG_BOX_USER_INFO.getValue()) {
        		//发送人
            	String sendId = message.getUid();
	        	System.out.println("server收到" + message.getMsg()  + "的信息。");
	        	if(null == SnBoxChannelCache.getChannel(sendId)) {
	        		SnBoxChannelCache.putChannel(sendId, ctx);
	        	}
	        	message.setMsgType(MessageType.MSG_BOX_PUSH.getValue());
	        	ctx.channel().writeAndFlush(message);
        	}else if(message.getMsgType() == MessageType.MSG_CHAT_USER_INFO.getValue()) {
        		//发送人
            	String sendId = message.getUid();
        		System.out.println("server收到" + message.getMsg()  + "的信息。");
	        	if(null == ChatChannelCache.getChannel(sendId)) {
	        		ChatChannelCache.putChannel(sendId, ctx);
	        	}
	        	message.setMsgType(MessageType.MSG_CHAT_SINGLE_PUSH.getValue());
	        	ctx.channel().writeAndFlush(message);
        	}else {
        		ctx.fireChannelRead(message);
        	}
        }else{
            ctx.fireChannelRead(message);
        }
    }
}
