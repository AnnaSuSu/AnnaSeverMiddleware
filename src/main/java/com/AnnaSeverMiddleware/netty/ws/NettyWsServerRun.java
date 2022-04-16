package com.AnnaSeverMiddleware.netty.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

/**
 * NettyWebSocket启动
 */
@Component
public class NettyWsServerRun implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(NettyWsServerRun.class);

	@Autowired
	@Qualifier("ws")
	private ServerBootstrap serverWsBootstrap;

	
	@Override
	public void run(String... args) throws Exception {
		ChannelFuture c = serverWsBootstrap.bind(9999).sync();
		if (c.isSuccess()) {
			log.info("启动 Netty Server---------------WebSocket绑定端口为：9999-----------------");
		}
		
	}

}
