package com.AnnaSeverMiddleware.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

/**
 * Netty启动
 */
@Component
public class NettyServerRun implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(NettyServerRun.class);

	@Autowired
	@Qualifier("tcp")
	private ServerBootstrap serverTcpBootstrap;
	
	@Autowired
	@Qualifier("ws")
	private ServerBootstrap serverWsBootstrap;

	@Override
	public void run(String... args) throws Exception {
		ChannelFuture f = serverTcpBootstrap.bind(8888).sync();
		if (f.isSuccess()) {
			log.info("启动 Netty Server---------------Tcp绑定端口为：8888-----------------");
		}
		ChannelFuture c = serverWsBootstrap.bind(9999).sync();
		if (c.isSuccess()) {
			log.info("启动 Netty Server---------------WebSocket绑定端口为：9999-----------------");
		}
	}
}







