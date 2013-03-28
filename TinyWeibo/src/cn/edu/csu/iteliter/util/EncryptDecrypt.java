package cn.edu.csu.iteliter.util;

import weibo4j.http.BASE64Decoder;
import weibo4j.http.BASE64Encoder;

/**
 * EncryptDecrypt tool
 * 
 * @author hjw
 * 
 */
public class EncryptDecrypt {

	/**
	 * decrypt
	 */
	public static String decrypt(String s) {
		try {
			byte[] buffer = new BASE64Decoder().decode(s.getBytes());
			for (int i = 0; i < buffer.length; i += 3) {
				buffer[i] = (byte) (((byte) (0x0F & buffer[i]) << 4) | (((byte) (0x70 & buffer[i])) >> 4) | (byte) ((((byte) (0x80 & buffer[i])) == -128) ? 0x08
						: 0));
			}
			for (int i = 0; i < (buffer.length - 1); i += 2) {
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
	 * encrypt
	 */
	public static String encrypt(String s) {
		try {
			byte[] buffer = s.getBytes("utf-8");
			for (int i = 0; i < (buffer.length - 1); i += 2) {
				byte temp = buffer[i];
				buffer[i] = buffer[i + 1];
				buffer[i + 1] = temp;
			}
			for (int i = 0; i < buffer.length; i += 3) {
				buffer[i] = (byte) (((byte) (0x0F & buffer[i]) << 4) | (((byte) (0x70 & buffer[i])) >> 4) | (byte) ((((byte) (0x80 & buffer[i])) == -128) ? 0x08
						: 0));
			}
			new BASE64Encoder();
			return new String(BASE64Encoder.encode(buffer));
		} catch (Exception e) {
		}
		return "";
	}

	/**
	 * simpleDecrypt
	 */
	public static String simpleDecrypt(String base64Str) {
		try {
			byte[] buffer = new BASE64Decoder().decode(base64Str.getBytes());
			for (int i = 0; i < (buffer.length - 1); i += 2) {
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
	 * simpleEncrypt
	 */
	public static String simpleEncrypt(String s) {
		try {
			byte[] buffer = s.getBytes("utf-8");
			for (int i = 0; i < (buffer.length - 1); i += 2) {
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
}
