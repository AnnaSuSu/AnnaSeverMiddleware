package com.AnnaSeverMiddleware.netty.tcp.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyTcpClient {

	static final Logger log = LoggerFactory.getLogger(NettyTcpClient.class);

	public static void main(String[] args) throws Exception {

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap(); // (1)
			b.group(workerGroup); // (2)
			b.channel(NioSocketChannel.class); // (3)
			b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					log.debug("connetted...");
//					ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
//						@Override
//						public void channelActive(ChannelHandlerContext ctx) throws Exception {
//							log.debug("sending...");
//							ByteBuf buffer = ctx.alloc().buffer();
//							for (int i = 0; i < 10; i++) {
//							    buffer.writeBytes(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
//							}
//						    ctx.writeAndFlush(buffer);
//						}
//					});
					ch.pipeline().addLast("ping", new IdleStateHandler(0, 3, 0, TimeUnit.SECONDS));
					ch.pipeline().addLast(new StringEncoder());
					ch.pipeline().addLast(new HeartBeatClientHandler());
				}
			});

			// 启动客户端
			ChannelFuture f = b.connect("127.0.0.1", 8888).sync(); // (5)

			// 等待连接关闭
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
