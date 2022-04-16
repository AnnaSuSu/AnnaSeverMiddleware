package com.AnnaSeverMiddleware.netty.ws.server.handler;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.AnnaSeverMiddleware.netty.ws.server.channel.ChannelManagement;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Sharable
public class WsServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	
	private static final Logger log = LoggerFactory.getLogger(WsServerHandler.class);

	@Autowired
	ChannelManagement channelManagement;

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame)
			throws Exception {
		// 打印接收到的消息
		System.out.println("服务端接受到的消息:" + textWebSocketFrame.text());
		String message = textWebSocketFrame.text();
		String[] strs = message.split("-");
		String ip = strs[0];
		String msg = strs[1];

		// 返回所有的pipeline可以对pipeline动态的删除或者修改
//		System.out.println(channelHandlerContext.pipeline());
//		ChannelPipeline a = channelHandlerContext.pipeline();
//		a.removeLast();
//		System.out.println(channelHandlerContext.pipeline());
		// 返回消息给客户端
//		channelHandlerContext.writeAndFlush(
//				new TextWebSocketFrame("服务器时间: " + LocalDateTime.now() + "  ： " + textWebSocketFrame.text()));
		// 回复消息
		channelManagement.getChannel().get(ip).writeAndFlush(new TextWebSocketFrame(msg));

	}

	/**
	 * 客户端连接的时候触发
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// LongText() 唯一的 ShortText() 不唯一
//		System.out.println("handlerAdded：" + ctx.channel().id().asLongText());
//		System.out.println("handlerAdded：" + ctx.channel().id().asShortText());
		// 得到连接的ip
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		log.info("连接客户端的ip为：---------" + ip);
		channelManagement.getChannelGroup().add(ctx.channel());
		channelManagement.getChannel().put(ip, ctx.channel());
		log.info("****** WS Connect token:" + ip + " 在线人数:" + channelManagement.getChannel().size() + " ******");
	}

	/**
	 * 客户端断开连接的时候触发
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
		// 移除sessios
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = insocket.getAddress().getHostAddress();
		channelManagement.getChannel().remove(ip);
		log.warn("****** WS Close   token:" + ip + " 在线人数:" + channelManagement.getChannel().size() + " ******");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("异常发生了...");
		ctx.close();
	}

}
