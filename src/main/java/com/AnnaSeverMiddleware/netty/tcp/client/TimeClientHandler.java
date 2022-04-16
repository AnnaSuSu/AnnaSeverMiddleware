package com.AnnaSeverMiddleware.netty.tcp.client;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	static final Logger log = LoggerFactory.getLogger(TimeClientHandler.class);

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.debug("sending...");
		Random r = new Random();
		char c = 'a';
		for (int i = 0; i < 10; i++) {
			ByteBuf buffer = ctx.alloc().buffer(16);
			buffer.writeBytes(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 });
			ctx.writeAndFlush(buffer);
		}
	}

}