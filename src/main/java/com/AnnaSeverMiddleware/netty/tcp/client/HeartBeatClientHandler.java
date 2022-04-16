package com.AnnaSeverMiddleware.netty.tcp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

	private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
			.unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat", CharsetUtil.UTF_8));

	private static final int TRY_TIMES = 3;

	private int currentTime = 0;

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.WRITER_IDLE) {
				if (currentTime <= TRY_TIMES) {
					System.out.println("currentTime:" + currentTime);
					currentTime++;
					ctx.channel().writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
				}
			}
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("-=-----接收到服务端消息------" + msg);
			ctx.write("has read message from server");
			ctx.flush();
		ReferenceCountUtil.release(msg);
	}
}
