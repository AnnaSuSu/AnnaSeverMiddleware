package com.AnnaSeverMiddleware.netty.tcp.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

	private int loss_connect_time = 0;

	static private final String HEART_BEAT = "Heartbeat";

	static final Logger log = LoggerFactory.getLogger(HeartBeatServerHandler.class);

	// 心跳时间
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				loss_connect_time++;
				System.out.println("5 秒没有接收到客户端的信息了");
				ctx.writeAndFlush(HEART_BEAT);
				if (loss_connect_time > 2) {
					System.out.println("关闭这个不活跃的channel");
					ctx.channel().close();
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 判断包的类型
		ByteBuf date = (ByteBuf) msg;
		if (date.toString(CharsetUtil.UTF_8).equals("Heartbeat")) {
			log.info("包为心跳包");
		} else {
			// 判断为字节包，流向下一层
			ctx.fireChannelRead(msg);
			log.info("包为字节包，流向下一个handler");
		}
	}
}
