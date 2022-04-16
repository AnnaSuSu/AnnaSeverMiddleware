package com.AnnaSeverMiddleware.netty.tcp.server.config;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.AnnaSeverMiddleware.netty.tcp.server.filter.DecodeTools;
import com.AnnaSeverMiddleware.netty.tcp.server.filter.TcpDecodeTools;
import com.AnnaSeverMiddleware.netty.tcp.server.handler.HeartBeatServerHandler;
import com.AnnaSeverMiddleware.netty.tcp.server.handler.ServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

@Configuration
public class NettyTcpServerConfig {

	static final Logger log = LoggerFactory.getLogger(NettyTcpServerConfig.class);

	@Bean
	public ServerHandler serverHandler() {
		return new ServerHandler();
	}

	@Bean
	public DecodeTools decodeTools() {
		return new DecodeTools();
	}
	
	@Bean
	public TcpDecodeTools tcpDecodeTools() {
		return new TcpDecodeTools();
	}

	/*
	 * 创建连接
	 */
	@Bean("tcp")
	public ServerBootstrap serverTcpBootstrap() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); 
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) 
				.childHandler(new ChannelInitializer<SocketChannel>() { 
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new LoggingHandler(LogLevel.INFO));
						pipeline.addLast(new StringEncoder());
						pipeline.addLast(new IdleStateHandler(10, 0, 0, TimeUnit.SECONDS));
						pipeline.addLast(new HeartBeatServerHandler());
						pipeline.addLast(new TcpDecodeTools());
						/*
						 * 不管是出的过滤器还是入的过滤器，addLast的顺序都很重要
						 * StringEncoder虽然是出得，但是如果一个handler出消息，那个出过滤器也要在handler之前 不然handle的消息也出不去
						 */
						pipeline.addLast(serverHandler());
						System.out.println("主函数的pipeline" + pipeline);
					}
				}).option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true); 
		return bootstrap;
	}
}
