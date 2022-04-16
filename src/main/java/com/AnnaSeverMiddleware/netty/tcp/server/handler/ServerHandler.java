package com.AnnaSeverMiddleware.netty.tcp.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.AnnaSeverMiddleware.comp.Rest.WebsocketTest;
import com.alibaba.fastjson.JSONObject;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

	@Autowired
	WebsocketTest websocketTest;

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

//		ByteBuf in = (ByteBuf) msg;
//		Byte[] date = (Byte[]) msg;
		byte[] date = (byte[]) msg;
//		for (int i = 0; i < date.length; i++) {
//			System.out.println("---------"+date[i] + "----------" );
//		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < date.length; i++) {
			sb.append(date[i]);
		}
		String message = sb.toString();
		log.info("接收到消息: " + message);
//		for (int i = 0; i < date.length; i++) {
//			sb.append(ConvertFactory.toHexString2(date[i]));
//		}
//
//		boolean isInstance = msg instanceof byte[];
//		System.out.println(isInstance);

		System.out.println(ctx.pipeline());
//		System.out.println("接收到消息: " + date.toString(CharsetUtil.UTF_8));
		log.info("in TestServerHandler1..............");
//		String message = in.toString();
//		ctx.fireChannelRead(message);
//		ctx.writeAndFlush("Heartbeat");

		/*
		 * 把byte转化为JSONObject
		 */
		JSONObject sendMsg = new JSONObject();
		sendMsg.put("length", date[0]);
		sendMsg.put("wight", date[1]);
		sendMsg.put("height", date[2]);
		/*
		 * 把数据给WebSocket层
		 */
		websocketTest.sendMessage(sendMsg);
	}
}








