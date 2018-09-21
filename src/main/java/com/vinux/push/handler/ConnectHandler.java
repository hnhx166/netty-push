package com.vinux.push.handler;

import java.net.InetSocketAddress;

import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectHandler extends SimpleChannelInboundHandler<Message>{

	//增加黑名单功能
    private String[] blackIps = {};
    
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
		
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Message message = (Message) msg;
        //如果是连接信息，判断是否是黑名单ip
        if(message != null && message.getMsgType() == MessageType.CONNECT_REQ.getValue()){
            Message response = null;
            boolean ok = true;
            for (String ip : blackIps) {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                if(address.getHostName().equals(ip)){
                    ok = false;
                }
            }
            response = ok ? buildMessage((byte)MessageType.CONNECT_SUCCESS.getValue()) : buildMessage((byte) MessageType.CONNECT_FAIL.getValue());
            ctx.writeAndFlush(response);
        }else{
            ctx.fireChannelRead(message);
        }
    }

	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        ctx.fireExceptionCaught(cause);
        //通道断开移除当前连接
        SnBoxChannelCache.removeChannel(ctx.channel());
    }

    private Message buildMessage(byte result){
        Message msg = new Message();
        msg.setMsgType(result);
        return msg;
    }
}
