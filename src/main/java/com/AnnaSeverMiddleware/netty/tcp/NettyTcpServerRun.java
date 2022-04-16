package com.AnnaSeverMiddleware.netty.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

/**
 * NettyTcp启动
 */
@Component
public class NettyTcpServerRun implements CommandLineRunner {
	
	private static final Logger log = LoggerFactory.getLogger(NettyTcpServerRun.class);
	
	@Autowired
	@Qualifier("tcp")
	private ServerBootstrap serverTcpBootstrap;

	@Override
	public void run(String... args) throws Exception {
		ChannelFuture f = serverTcpBootstrap.bind(8888).sync();
		if (f.isSuccess()) {
			log.info("启动 Netty Server---------------Tcp绑定端口为：8888-----------------");
		}
	}
	

}













