package com.AnnaSeverMiddleware.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertFactory {

	private static final Logger log = LoggerFactory.getLogger(ConvertFactory.class);

	/**
	 * 16进制字符串转浮点数(4个字节)
	 * 
	 * @param hexString
	 * @return float
	 */
	public static float hexString2Float(String hexString) {
		int intVal = Integer.valueOf(hexString.trim(), 16);
		float floatVal = Float.intBitsToFloat(intVal);
		return floatVal;
	}

	/**
	 * 浮点数转16进制字符串
	 * 
	 * @param floatVal
	 * @return String
	 */
	public static String float2HexString(float floatVal) {
		int intVal = Float.floatToIntBits(floatVal);
		String hexString = Integer.toHexString(intVal).trim().toUpperCase();
		return hexString;
	}

	public static byte[] intToByteArray(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	/*
	 * 
	 */
	public static byte[] shortToBytes(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) ((n >> 8) & 0xff);
		return b;
	}

	public static short bytesToShort(byte[] b) {
		return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
	}

	public static byte[] intToBytes(int num) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (num >>> (24 - i * 8));
		}
		return b;
	}

	public static int bytes2int(byte[] b) {

		int mask = 0xff;
		int temp = 0;
		int res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & mask;
			res |= temp;
		}
		return res;
	}

	public static byte[] longToBytes(long num) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (num >>> (56 - i * 8));
		}
		return b;
	}

	public static String str2HexStr(String str) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		byte[] bs = str.getBytes();
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	public static String bytesToHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static String byteToHexString(byte b) {
		String sTemp = Integer.toHexString(0xFF & b);
		return sTemp.toUpperCase();
	}

	public static byte[] hexStringToBytes(String hexString) {
		String chars = "0123456789ABCDEF";
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (chars.indexOf(hexChars[pos]) << 4 | chars.indexOf(hexChars[pos + 1]));
		}
		return d;
	}

	public static int HexToInt(String strHex) {
		int nResult = 0;
		if (!IsHex(strHex))
			return nResult;
		String str = strHex.toUpperCase();
		if (str.length() > 2) {
			if (str.charAt(0) == '0' && str.charAt(1) == 'X') {
				str = str.substring(2);
			}
		}
		int nLen = str.length();
		for (int i = 0; i < nLen; ++i) {
			char ch = str.charAt(nLen - i - 1);
			try {
				nResult += (GetHex(ch) * GetPower(16, i));
			} catch (Exception e) {
				log.error("error", e);
			}
		}
		return nResult;
	}

	/**
	 * 计算16进制对应的数值
	 * 
	 * @param ch 十六进制数
	 * @return
	 * @throws Exception
	 */
	private static int GetHex(char ch) throws Exception {
		if (ch >= '0' && ch <= '9')
			return (int) (ch - '0');
		if (ch >= 'a' && ch <= 'f')
			return (int) (ch - 'a' + 10);
		if (ch >= 'A' && ch <= 'F')
			return (int) (ch - 'A' + 10);
		throw new Exception("error param");
	}

	/**
	 * 计算幂
	 * 
	 * @param nValue
	 * @param nCount
	 * @return
	 * @throws Exception
	 */
	private static int GetPower(int nValue, int nCount) throws Exception {
		if (nCount < 0)
			throw new Exception("nCount can't small than 1!");
		if (nCount == 0)
			return 1;
		int nSum = 1;
		for (int i = 0; i < nCount; ++i) {
			nSum = nSum * nValue;
		}
		return nSum;
	}

	/**
	 * 判断是否是16进制数
	 * 
	 * @param strHex
	 * @return
	 */
	private static boolean IsHex(String strHex) {
		int i = 0;
		if (strHex.length() > 2) {
			if (strHex.charAt(0) == '0' && (strHex.charAt(1) == 'X' || strHex.charAt(1) == 'x')) {
				i = 2;
			}
		}
		for (; i < strHex.length(); ++i) {
			char ch = strHex.charAt(i);
			if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f'))
				continue;
			return false;
		}
		return true;
	}

	/**
	 * @Description 获取long的低位上后3个字节，返回16进制的字节数组
	 * @param
	 * @return
	 * @throws @author gaozhen
	 */
	public static byte[] longTo3HexByte(long number) {
		byte[] res = new byte[3];
		byte[] temp = ConvertFactory.longToBytes(number);
		for (int i = 5, j = 0; i <= 7; i++, j++) {
			res[j] = temp[i];
		}
		return res;
	}

	/**
	 * 16进制字符串->字节数组
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	/**
	 * 将byte转化为2位16进制的字符
	 * 
	 * @param b
	 * @return white
	 */
	public static String toHexString2(byte b) {
		String s = Integer.toHexString(b & 0xFF);
		if (s.length() == 1) {
			return "0" + s;
		} else {
			return s;
		}
	}
	
    final static Base64.Encoder encoder = Base64.getEncoder();
    final static Base64.Decoder decoder = Base64.getDecoder();
	
	/**
	 * @Title: encodeBASE64
	 * @Description: 将字符串进行Base64加密
	 * @Author yuankaiqiang
	 * @DateTime 2021-03-10 19:54:21
	 * @param content
	 * @return
	 */
	public static String encodeBASE64(String content) { 
		if (content == null) {
			return null; 
		}
		return encoder.encodeToString(content.getBytes(StandardCharsets.UTF_8)); 
	} 

	/**
	 * @Title: decodeBASE64
	 * @Description: 将字符串进行Base64解密
	 * @Author yuankaiqiang
	 * @DateTime 2021-03-10 19:54:41
	 * @param content
	 * @return
	 */
	public static String decodeBASE64(String content) { 
		if (content == null) {
			return null; 
		}
		return new String(decoder.decode(content), StandardCharsets.UTF_8); 
	}
	
}