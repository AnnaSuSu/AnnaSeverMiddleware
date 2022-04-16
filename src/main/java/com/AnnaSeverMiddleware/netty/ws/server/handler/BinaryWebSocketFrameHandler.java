package com.AnnaSeverMiddleware.netty.ws.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

@Sharable
public class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

	private static final Logger log = LoggerFactory.getLogger(BinaryWebSocketFrameHandler.class);
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
		log.info("开始处理二进制数据");
		log.info("服务器接收到二进制消息,消息长度:[{}]", msg.content().capacity());
        ByteBuf byteBuf = Unpooled.directBuffer(msg.content().capacity());
        byteBuf.writeBytes(msg.content());
        ctx.writeAndFlush(new BinaryWebSocketFrame(byteBuf));
	}

}
