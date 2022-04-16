package com.AnnaSeverMiddleware.comp.Rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AnnaSeverMiddleware.netty.ws.server.service.WsService;
import com.alibaba.fastjson.JSONObject;


@Service
public class WebsocketTest {
	
	private static final Logger log = LoggerFactory.getLogger(WebsocketTest.class);
	
	@Autowired
	private WsService wsService;

	public void sendMessage(JSONObject str) throws Exception {
		wsService.addJsonObjToSendUnicast("db", str, "127.0.0.1");
	}
}
