package com.vinux.push.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ChatChannelCache {

	// 聊天消息，存储用户ID地址和ChannelHandlerContext
	private static ConcurrentHashMap<String, ChannelHandlerContext> chatChannels = new ConcurrentHashMap<String, ChannelHandlerContext>();
	// 聊天消息，存储用户ID地址和channel
	private static ConcurrentHashMap<Channel, String> chatChannelsMap = new ConcurrentHashMap<Channel, String>();

	/**
	 * 存储channel
	 * 
	 * @param key
	 * @param ctx
	 */
	public static void putChannel(String key, ChannelHandlerContext ctx) {
		chatChannels.put(key, ctx);
		chatChannelsMap.put(ctx.channel(), key);
	}

	/**
	 * 删除channel
	 * 
	 * @param channel
	 */
	public static void removeChannel(Channel channel) {
		String key = chatChannelsMap.get(channel);
		chatChannels.remove(key);
		chatChannelsMap.remove(channel);
	}

	/**
	 * 删除channel
	 * 
	 * @param key
	 */
	public static void removeChannel(String key) {
		chatChannels.remove(key);
		chatChannelsMap.values().remove(key);
	}

	/**
	 * 获取channel
	 * 
	 * @param key
	 * @return
	 */
	public static ChannelHandlerContext getChannel(String key) {
		return chatChannels.get(key);
	}

	/**
	 * 获取所有channel
	 * 
	 * @return
	 */
	public static Collection<ChannelHandlerContext> getChannels() {
		return chatChannels.values();
	}
}
