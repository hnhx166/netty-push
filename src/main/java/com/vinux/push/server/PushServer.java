package com.vinux.push.server;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.vinux.mq.Producer;
import com.vinux.push.cache.SnBoxChannelCache;
import com.vinux.push.entity.Message;
import com.vinux.push.enu.MQ_CHANNEL;
import com.vinux.push.enu.MessageType;
import com.vinux.push.handler.ConnectHandler;
import com.vinux.push.handler.ExceptionHandler;
import com.vinux.push.handler.HeartBeatHandler;
import com.vinux.push.handler.PushHandler;
import com.vinux.push.handler.UserInfoHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
@Order(value = 1)
public class PushServer implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(PushServer.class);
	
	public void bind() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		ServerBootstrap bs = new ServerBootstrap();
		bs.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1000)
				.option(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel channel) throws Exception {
						ChannelPipeline p = channel.pipeline();
						p.addLast(new ObjectEncoder());
						p.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
						// 心跳超时
						p.addLast(new ReadTimeoutHandler(100));
						p.addLast(new ConnectHandler());
						p.addLast(new HeartBeatHandler());
						p.addLast(new UserInfoHandler());
						// 数据处理
						p.addLast(new PushHandler());
						// 异常处理
						p.addLast(new ExceptionHandler());

					}
				});
		bs.bind(8000).sync();
		System.out.println("server 8000 start....");
	}

	// 狻猊宝盒消息推送
	public void push(Message message) {
		// Message message = new Message();
		// message.setMsg(msg);
		// message.set
		// message.setMsgType(MessageType.MSG_BOX_PUSH.getValue());
		// int sendType = message.getSendType();
		// if(sendType == 1) {//点对点
		// String receiverId = message.getReceiveId();
		// channels.get(receiverId).writeAndFlush(message);
		// }else {//推送
		// Integer groupId = message.getGroupId();
		//
		// }

		

		Collection<ChannelHandlerContext> channels = SnBoxChannelCache.getChannels();
		for (ChannelHandlerContext ctx : channels) {
			JSONObject jData = new JSONObject();
			jData.put("appId", message.getAppId());
			jData.put("groupId", message.getGroupId());
			jData.put("msg", message.getMsg());
			jData.put("msgType", message.getMsgType());
//			jData.put("receiveId", message.getReceiveId());
			jData.put("receiveType", message.getReceiveType());
			jData.put("uid", message.getUid());
			jData.put("version", message.getVersion());
			jData.put("sendTime", message.getSendTime());
			jData.put("serverSendTime", System.currentTimeMillis());
			
			String receId = SnBoxChannelCache.getKey(ctx.channel());
			jData.put("receiveId", receId);
			System.out.println("#########服务器发送消息，接收者ID:" + receId);
			if (receId != null) {
				message.setReceiveId(receId);
			} else {
				message.setReceiveId(null);
			}
			ctx.writeAndFlush(message).addListener(new ChannelFutureListener() {

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
						jData.put("status", 200);
						jData.put("receiveTime", System.currentTimeMillis());
					}else {
						jData.put("status", 300);
					}
					saveMessage(jData);
				}
			});
		}
	}

	// 点对点聊天消息推送
	public void pushChatSingle(String msg) {
		Message message = new Message();
		message.setMsg(msg);
		message.setMsgType(MessageType.MSG_CHAT_SINGLE_PUSH.getValue());
		// int sendType = message.getSendType();
		// if(sendType == 1) {//点对点
		// String receiverId = message.getReceiveId();
		// channels.get(receiverId).writeAndFlush(message);
		// }else {//推送
		// Integer groupId = message.getGroupId();
		//
		// }
		Collection<ChannelHandlerContext> channels = SnBoxChannelCache.getChannels();
		for (ChannelHandlerContext ctx : channels) {
			ctx.writeAndFlush(message);
		}
	}

	// 群组聊天消息推送
	public void pushChatGroup(String msg) {
		Message message = new Message();
		message.setMsg(msg);
		message.setMsgType(MessageType.MSG_CHAT_GROUP_PUSH.getValue());
		// int sendType = message.getSendType();
		// if(sendType == 1) {//点对点
		// String receiverId = message.getReceiveId();
		// channels.get(receiverId).writeAndFlush(message);
		// }else {//推送
		// Integer groupId = message.getGroupId();
		//
		// }
		Collection<ChannelHandlerContext> channels = SnBoxChannelCache.getChannels();
		for (ChannelHandlerContext ctx : channels) {
			ctx.writeAndFlush(message);
		}
	}

	// public static void main(String[] args) throws Exception{
	// PushServer pushServer = new PushServer();
	// pushServer.bind();
	// }

	// 发送消息
	private void saveMessage(JSONObject message) {
		// MqPublicInfo.processMsg(message, queueName);
		try {
			Producer.pushMessage(message, MQ_CHANNEL.CHANNEL_SAVE);
		} catch (Exception e) {
			logger.info("保存消息出错：" + message.toJSONString());
			e.printStackTrace();
		}
	}

	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("服务器端开始启动.........");
		this.bind();
		System.out.println("服务器端启动完成.........");

		// System.out.println("启动消费者.........");
		// MessageCustomer.initCustomer();
		// System.out.println("消费者启动完成.........");
	}
}
