/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:31
 */
package cn.edu.csu.iteliter.util;

import weibo4j.http.BASE64Decoder;
import weibo4j.http.BASE64Encoder;

/**
 * @filename EncryptDecrypt.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description 加密解密的工具类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午10:58:40
 * @version 1.0
 * 
 */
public class EncryptDecryptUtil {

	/**
	 * 加密.
	 * 
	 * @param s
	 *            要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String encrypt(String s) {
		try {
			byte[] buffer = s.getBytes("utf-8");
			for (int i = 0; i < buffer.length - 1; i += 2) {
				byte temp = buffer[i];
				buffer[i] = buffer[i + 1];
				buffer[i + 1] = temp;
			}
			for (int i = 0; i < buffer.length; i += 3) {
				buffer[i] = (byte) ((byte) (0x0F & buffer[i]) << 4 | (((byte) (0x70 & buffer[i])) >> 4) | (byte) ((((byte) (0x80 & buffer[i])) == -128) ? 0x08
						: 0));
			}
			new BASE64Encoder();
			return new String(BASE64Encoder.encode(buffer));
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 简单加密方法
	 * 
	 * @param s
	 *            要加密的字符串
	 * @return 加密后的字符串
	 */
	public static String simpleEncrypt(String s) {
		try {
			byte[] buffer = s.getBytes("utf-8");
			for (int i = 0; i < buffer.length - 1; i += 2) {
				byte temp = buffer[i];
				buffer[i] = buffer[i + 1];
				buffer[i + 1] = temp;
			}
			new BASE64Encoder();
			return new String(BASE64Encoder.encode(buffer));
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 解密.
	 * 
	 * @param s
	 *            要解密的字符串
	 * @return 解密后的字符串
	 */
	public static String decrypt(String s) {
		try {
			byte[] buffer = new BASE64Decoder().decode(s.getBytes());
			for (int i = 0; i < buffer.length; i += 3) {
				buffer[i] = (byte) ((byte) (0x0F & buffer[i]) << 4 | (((byte) (0x70 & buffer[i])) >> 4) | (byte) ((((byte) (0x80 & buffer[i])) == -128) ? 0x08
						: 0));
			}
			for (int i = 0; i < buffer.length - 1; i += 2) {
				byte temp = buffer[i];
				buffer[i] = buffer[i + 1];
				buffer[i + 1] = temp;
			}
			return new String(buffer, "utf-8");
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * 简单解密
	 * 
	 * @param base64Str
	 *            要解密的字符串
	 * @return 解密后的字符串
	 */
	public static String simpleDecrypt(String base64Str) {
		try {
			byte[] buffer = new BASE64Decoder().decode(base64Str.getBytes());
			for (int i = 0; i < buffer.length - 1; i += 2) {
				byte temp = buffer[i];
				buffer[i] = buffer[i + 1];
				buffer[i + 1] = temp;
			}
			return new String(buffer, "utf-8");
		} catch (Exception e) {
		}
		return "";
	}
}
