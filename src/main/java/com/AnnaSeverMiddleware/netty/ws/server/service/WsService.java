package com.AnnaSeverMiddleware.netty.ws.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.AnnaSeverMiddleware.netty.ws.server.channel.ChannelManagement;
import com.alibaba.fastjson.JSONObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * WebSocket 服务
 */
@Service
public class WsService {

	private static final Logger log = LoggerFactory.getLogger(WsService.class);

	@Autowired
	ChannelManagement channelManagement;

	/**
	 * 推送 Websocket 数据(单播模式)
	 */
	@Async
	public <T> void addJsonObjToSendUnicast(String type, T data, String ip) throws Exception {
		// 1、封装数据
		JSONObject message = new JSONObject();
		message.put("type", type);
		message.put("date", data);

		// 2、获取会话
		Channel channel = (ip != null) ? channelManagement.getChannel().get(ip) : null;
		if (channel == null) {
			log.error("WS单播通信失败，对方没有在线，token：" + ip);
			return;
		}

		// 3、单播通信
		ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(1024);
		byte[] msg = message.toJSONString().getBytes();
		buffer.writeBytes(msg);
		synchronized (channel) {
			channel.writeAndFlush(new BinaryWebSocketFrame(buffer));
			log.info("WS单播通信：token:" + ip + ",发送数据:" + message.toJSONString());
		}

	}
}
