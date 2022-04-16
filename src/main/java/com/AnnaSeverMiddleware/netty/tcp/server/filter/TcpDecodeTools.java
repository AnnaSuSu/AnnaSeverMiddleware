package com.AnnaSeverMiddleware.netty.tcp.server.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TcpDecodeTools extends ByteToMessageDecoder {

	static final Logger logger = LoggerFactory.getLogger(TcpDecodeTools.class);

	// 最小的数据长度：开头标准位1字节
	private static int MIN_DATA_LEN = 6;
	// 数据解码协议的开始标志
	private static byte PROTOCOL_HEADER = -2;
	// 数据解码协议的结束标志
	private static byte PROTOCOL_TAIL = 85;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		System.out.println(in.readableBytes());
//		Byte len = in.readByte();
//		System.out.println("------len的值为------" + len);
		if (in.readableBytes() >= MIN_DATA_LEN) {
			logger.info("开始解码数据……");
			// 标记读操作的指针
			in.markReaderIndex();
			byte header = in.readByte();
			if (header == PROTOCOL_HEADER) {
				logger.info("数据开头格式正确");
				// 读取字节数据的长度
				Byte len = in.readByte();
				System.out.println("-------len--------的长度为：" + len);
				// 数据可读长度必须要大于len，因为结尾还有一字节的解释标志位
				if (len >= in.readableBytes()) {
					logger.info(
							String.format("数据长度不够，数据协议len长度为：%1$d,数据包实际可读内容为：%2$d正在等待处理拆包……", len, in.readableBytes()));
					in.resetReaderIndex();
					/*
					 ** 结束解码，这种情况说明数据没有到齐，在父类ByteToMessageDecoder的callDecode中会对out和in进行判断
					 * 如果in里面还有可读内容即in.isReadable为true,cumulation中的内容会进行保留，，直到下一次数据到来，将两帧的数据合并起来，
					 * 再解码。 以此解决拆包问题
					 */
					return;
				}
				// 当数据对的时候一次读取全部的数据
				// 创建16进制的byte数组。
				byte[] data = new byte[len];
				in.readBytes(data);// 读取核心的数据
				// 在读取一个最末尾的数据
				byte tail = in.readByte();
				if (tail == PROTOCOL_TAIL) {
					logger.info("数据解码成功");
//					for (int i = 0; i < data.length; i++) {
//						System.out.println("data的值为------" + data[i]);
//					}
					out.add(data);
					// 如果out有值，且in仍然可读，将继续调用decode方法再次解码in中的内容，以此解决粘包问题
				} else {
					logger.info(String.format("数据解码协议结束标志位:%1$d [错误!]，期待的结束标志位是：%2$d", tail, PROTOCOL_TAIL));
					return;
				}
			} else {
				logger.info("开头不对，可能不是期待的客服端发送的数，将自动略过这一个字节");
			}
		} else {
			logger.info("数据长度不符合要求，期待最小长度是：" + MIN_DATA_LEN + " 字节");
			return;
		}
	}

}
