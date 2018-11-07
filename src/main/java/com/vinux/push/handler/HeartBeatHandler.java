package com.vinux.push.handler;

import com.vinux.push.cache.ChatChannelCache;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HeartBeatHandler extends SimpleChannelInboundHandler<Message>{

	//加入到在线列表，只有在线用户才可以实时推送
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	ctx.fireChannelActive();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
    	//如果是心跳包ping，则返回pong
        if(message != null && message.getMsgType() == MessageType.HEARTBEAT_REQ.getValue()){
            Message response = buildMessage(MessageType.HEARTBEAT_RESP.getValue());
            ctx.writeAndFlush(response);
        }else{
            ctx.fireChannelRead(message);
        }
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        ctx.fireExceptionCaught(cause);
        //通道断开移除channel
        ChatChannelCache.removeChannel(ctx.channel());
    }

    private Message buildMessage(byte result){
        Message msg = new Message();
        msg.setMsgType(result);
        return msg;
    }

}
