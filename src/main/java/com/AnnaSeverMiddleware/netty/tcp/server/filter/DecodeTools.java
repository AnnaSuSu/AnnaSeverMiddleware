package com.AnnaSeverMiddleware.netty.tcp.server.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.AnnaSeverMiddleware.util.ConvertFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DecodeTools extends ByteToMessageDecoder {

	static final Logger log = LoggerFactory.getLogger(DecodeTools.class);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		byte[] bytes = new byte[in.readableBytes()];
		in.readBytes(bytes);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			sb.append(ConvertFactory.toHexString2(bytes[i]));
		}
		String message = sb.toString().toUpperCase();
		int position = 0;
		int limit = message.length();
		while (position < limit) {
			if (message.substring(position, position + 2).equals("FE")) {
				/*
				 * 数据部分
				 */
				String content = message.substring(2, 8);
				/*
				 * 帧尾
				 */
				String foot = message.substring(8, 10);
				System.out.println("message：" + message);
				System.out.println("content：" + content);
				System.out.println("foot：" + foot);
				if (foot.equals("55")) {
					String s = content;
					if (s.length() == 10) {
						out.add(s);
					}
					System.out.println("发送成功，发送内容：" + content);
					break;
				} else {
					System.out.println("包尾不是55,去除破损包等待下一包...");
					break;
				}
			} else {
				break;
			}
		}
	}

}
