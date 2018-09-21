package com.vinux.push.server;

import java.util.Collection;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MessageType;
import com.vinux.push.handler.ConnectHandler;
import com.vinux.push.handler.ExceptionHandler;
import com.vinux.push.handler.HeartBeatHandler;
import com.vinux.push.handler.PushHandler;
import com.vinux.push.handler.UserInfoHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

@Component
@Order(value=1)
public class PushServer implements CommandLineRunner {

	public void bind() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bs = new ServerBootstrap();
        bs.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline p = channel.pipeline();
                        p.addLast(new ObjectEncoder());
                        p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                        //心跳超时
                        p.addLast(new ReadTimeoutHandler(100));
                        p.addLast(new ConnectHandler());
                        p.addLast(new HeartBeatHandler());
                        p.addLast(new UserInfoHandler());
                        //数据处理
                        p.addLast(new PushHandler());
                        //异常处理
                        p.addLast(new ExceptionHandler());
                        
                    }
                });
        bs.bind(8000).sync();
        System.out.println("server 8000 start....");
    }

    //狻猊宝盒消息推送
    public void push(String msg){
        Message message = new Message();
        message.setMsg(msg);
        message.setMsgType(MessageType.MSG_BOX_PUSH.getValue());
//        int sendType = message.getSendType();
//        if(sendType == 1) {//点对点
//        	String receiverId = message.getReceiveId();
//        	channels.get(receiverId).writeAndFlush(message);
//        }else {//推送
//        	Integer groupId = message.getGroupId();
//        	
//        }
        Collection<ChannelHandlerContext> channels = SnBoxChannelCache.getChannels();
        for (ChannelHandlerContext ctx : channels){
            ctx.writeAndFlush(message);
        }
    }
    
    //点对点聊天消息推送
    public void pushChatSingle(String msg){
        Message message = new Message();
        message.setMsg(msg);
        message.setMsgType(MessageType.MSG_CHAT_SINGLE_PUSH.getValue());
//        int sendType = message.getSendType();
//        if(sendType == 1) {//点对点
//        	String receiverId = message.getReceiveId();
//        	channels.get(receiverId).writeAndFlush(message);
//        }else {//推送
//        	Integer groupId = message.getGroupId();
//        	
//        }
        Collection<ChannelHandlerContext> channels = SnBoxChannelCache.getChannels();
        for (ChannelHandlerContext ctx : channels){
            ctx.writeAndFlush(message);
        }
    }
    
    //群组聊天消息推送
    public void pushChatGroup(String msg){
        Message message = new Message();
        message.setMsg(msg);
        message.setMsgType(MessageType.MSG_CHAT_GROUP_PUSH.getValue());
//        int sendType = message.getSendType();
//        if(sendType == 1) {//点对点
//        	String receiverId = message.getReceiveId();
//        	channels.get(receiverId).writeAndFlush(message);
//        }else {//推送
//        	Integer groupId = message.getGroupId();
//        	
//        }
        Collection<ChannelHandlerContext> channels = SnBoxChannelCache.getChannels();
        for (ChannelHandlerContext ctx : channels){
            ctx.writeAndFlush(message);
        }
    }

//    public static void main(String[] args) throws Exception{
//        PushServer pushServer = new PushServer();
//        pushServer.bind();
//    }

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("服务器端开始启动.........");
		this.bind();
		System.out.println("服务器端启动完成.........");
	}
}
