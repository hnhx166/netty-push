package com.vinux.push.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ExceptionHandler extends ChannelDuplexHandler {

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
            	System.out.println("发送失败。。。。。。。");
//               System.out.println(future.cause());
            	future.cause().printStackTrace();
            }
        }));
    }
}
