package com.vinux.push.handler;

import com.vinux.push.cache.ChatChannelCache;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * 客户端关闭后关闭CHANEL
*    
* 项目名称：netty-push   
* 类名称：OutHandel   
* 类描述：   
* 创建人：guohaixiang  
* 创建时间：2018年11月7日 上午10:41:13   
* 修改人：Administrator   
* 修改时间：2018年11月7日 上午10:41:13   
* 修改备注：   
* @version 1.0
*
 */
public class OutHandler extends ChannelOutboundHandlerAdapter {

	/**
	 * 移除channel
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		ChatChannelCache.removeChannel(ctx.channel());
	}
}
