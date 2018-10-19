package com.vinux.push.cache;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * 通道存储
*    
* 项目名称：netty-push   
* 类名称：ChannelCache   
* 类描述：   
* 创建人：guohaixiang  
* 创建时间：2018年9月17日 下午1:27:05   
* 修改人：Administrator   
* 修改时间：2018年9月17日 下午1:27:05   
* 修改备注：   
* @version 1.0
*
 */
public class SnBoxChannelCache {

	//狻猊宝盒，存储用户MAC地址和ChannelHandlerContext
	private static ConcurrentHashMap<String, ChannelHandlerContext> snBoxChannels = new ConcurrentHashMap<String, ChannelHandlerContext>();
	//狻猊宝盒，存储用户MAC地址和channel
	private static ConcurrentHashMap<Channel, String> snBoxChannelsMap = new ConcurrentHashMap<Channel, String>();
	
	
	/**
	 * 存储channel
	 * @param key
	 * @param ctx
	 */
	public static void putChannel(String key,ChannelHandlerContext ctx) {
		snBoxChannels.put(key, ctx);
		snBoxChannelsMap.put(ctx.channel(), key);
	}
	
	/**
	 * 删除channel
	 * @param channel
	 */
	public static void removeChannel(Channel channel) {
		String key = snBoxChannelsMap.get(channel);
		snBoxChannels.remove(key);
		snBoxChannelsMap.remove(channel);
	}
	
	/**
	 * 删除channel
	 * @param key
	 */
	public static void removeChannel(String key) {
		snBoxChannels.remove(key);
		snBoxChannelsMap.values().remove(key);
	}
	
	/**
	 * 获取channel
	 * @param key
	 * @return
	 */
	public static ChannelHandlerContext getChannel(String key) {
		return snBoxChannels.get(key);
	}
	
	/**
	 * 获取channel
	 * @param key
	 * @return
	 */
	public static String getKey(Channel channel) {
		return snBoxChannelsMap.get(channel);
	}
	
	/**
	 * 获取所有channel
	 * @return
	 */
	public static Collection<ChannelHandlerContext> getChannels() {
		return snBoxChannels.values();
	}
	
}
