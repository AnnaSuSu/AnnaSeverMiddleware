package com.AnnaSeverMiddleware.netty.ws.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class TestHandler extends ChannelInboundHandlerAdapter {

	
	static final Logger log = LoggerFactory.getLogger(TestHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {	
		//返回所有的pipeline可以对pipeline动态的删除或者修改
		log.info("hello");
		System.out.println("TestHandler中的过滤器"+ctx.pipeline());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	}

}
