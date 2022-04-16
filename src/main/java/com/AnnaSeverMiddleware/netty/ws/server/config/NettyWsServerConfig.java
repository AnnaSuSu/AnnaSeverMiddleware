package com.AnnaSeverMiddleware.netty.ws.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.AnnaSeverMiddleware.netty.ws.server.handler.BinaryWebSocketFrameHandler;
import com.AnnaSeverMiddleware.netty.ws.server.handler.TestHandler;
import com.AnnaSeverMiddleware.netty.ws.server.handler.WebSocketHandle;
import com.AnnaSeverMiddleware.netty.ws.server.handler.WsServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.stream.ChunkedWriteHandler;

@Configuration
public class NettyWsServerConfig {

	static final Logger log = LoggerFactory.getLogger(NettyWsServerConfig.class);

	@Bean
	public WsServerHandler wsServerHandler() {
		return new WsServerHandler();
	}

	@Bean
	public BinaryWebSocketFrameHandler binaryWebSocketFrameHandler() {
		return new BinaryWebSocketFrameHandler();
	}

	@Bean
	public WebSocketHandle webSocketHandle() {
		return new WebSocketHandle();
	}

	/*
	 * 创建连接
	 */
	@Bean("ws")
	public ServerBootstrap serverWsBootstrap() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
				.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						// 因为基于http协议，使用http的编码和解码器
						pipeline.addLast(new HttpServerCodec());
						// 是以块方式写，添加ChunkedWriteHandler处理器
						pipeline.addLast(new ChunkedWriteHandler());
						pipeline.addLast(new HttpObjectAggregator(8192));
						pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
						/*
						 * 说明 1. 对应websocket ，它的数据是以 帧(frame) 形式传递 2. 可以看到WebSocketFrame 下面有六个子类 3.
						 * 浏览器请求时 ws://localhost:9999/ws 表示请求的uri 4. WebSocketServerProtocolHandler
						 * 核心功能是将 http协议升级为 ws协议 , 保持长连接 5. 是通过一个 状态码 101
						 */
						pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
						pipeline.addLast(webSocketHandle());
//						pipeline.addLast(binaryWebSocketFrameHandler());
//						log.info("调用testHandler---------");
//						pipeline.addLast(testHandler());
						System.out.println("pipeline为：" + pipeline);
					}
				}).option(ChannelOption.SO_BACKLOG, 128) // (5)
				.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
		return bootstrap;
	}

}
