package com.AnnaSeverMiddleware.netty.tcp.server.config;

public class TcpConst {
	
	// 包头第一个字符，储存的是ASCII码，默认F
	public static int HEAD_ONE = 70;

	// 包头第二个字符，储存的是ASCII码，默认E
	public static int HEAD_TWO = 69;

	// 包尾第一个字符，储存的是ASCII码，默认5
	public static int TAIL_ONE = 53;

	// 包尾第二个字符，储存的是ASCII码，默认5
	public static int TAIL_TWO = 53;

	// 字节解码包头，默认0xFE
	public static byte BYTE_HEAD = -2;

	// 字节解码包尾，默认0x55
	public static byte BYTE_TAIL = 85;


}
