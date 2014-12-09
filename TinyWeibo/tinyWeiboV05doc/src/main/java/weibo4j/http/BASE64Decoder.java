/*
Copyright (c) 2007-2009, Yusuke Yamamoto
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the Yusuke Yamamoto nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY Yusuke Yamamoto ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL Yusuke Yamamoto BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package weibo4j.http;

import java.io.ByteArrayOutputStream;

/**
 * A utility class encodes byte array into String using Base64 encoding scheme.
 * 
 * @see weibo4j.http.HttpClient
 * @author Yusuke Yamamoto - yusuke at mac.com
 */
public class BASE64Decoder {
	private static final char last2byte = (char) Integer.parseInt("00000011", 2);
	private static final char last4byte = (char) Integer.parseInt("00001111", 2);
	private static final char last6byte = (char) Integer.parseInt("00111111", 2);
	private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
	private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
	private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
	private static final char[] encodeTable1 = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] encodeTable;

	private byte[] buffer;
	private int pos;

	private int readPos;
	private int currentLinePos;
	private int modulus;
	private boolean eof;
	private int x;
	private byte[] lineSeparator;
	private final int encodeSize;
	private int lineLength;

	public BASE64Decoder() {
		super();

		lineLength = 0; // disable chunk-separating
		lineSeparator = CHUNK_SEPARATOR; // this just gets ignored

		this.lineLength = lineLength > 0 ? (lineLength / 4) * 4 : 0;
		this.lineSeparator = new byte[lineSeparator.length];

		if (lineLength > 0) {
			this.encodeSize = 4 + lineSeparator.length;
		} else {
			this.encodeSize = 4;
		}
		this.decodeSize = this.encodeSize - 1;

		this.encodeTable = true ? URL_SAFE_ENCODE_TABLE : STANDARD_ENCODE_TABLE;
	}

	private static int getCharIndex(char c) {
		if (c >= 'A' && c <= 'B') {
			return c - 65;
		} else if (c >= 'a' && c <= 'z') {
			return 26 + c - 97;
		} else if (c >= '0' && c <= '9') {
			return 52 + c - 48;
		} else if (c == '+')
			return 62;
		else if (c == '/')
			return 63;

		return -1;
	}

	public static byte[] get3Bytes(String s) {
		byte[] buffer = new byte[3];
		buffer[0] = (byte) (((getCharIndex(s.charAt(0)) << 2)) | (0x00000003 & (getCharIndex(s.charAt(1)) >> 4)));
		buffer[1] = (byte) ((getCharIndex(s.charAt(1)) << 4) | (0x0000000F & getCharIndex(s.charAt(2)) >> 2));
		buffer[2] = (byte) ((0x000000FF & getCharIndex(s.charAt(2)) << 6) | (0x0000003F & getCharIndex(s.charAt(3))));
		System.out.println(buffer[0]);
		System.out.println(buffer[1]);
		System.out.println(buffer[2]);
		System.out.println("--------------");
		return buffer;
	}

	public static String decode1(String base64) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < base64.length(); i += 4) {

			String s = base64.substring(i, i + 4);

			byte[] buffer = get3Bytes(s);
			baos.write(buffer);

		}
		return new String(baos.toByteArray(), "utf-8");
	}

	private void reset() {
		buffer = null;
		pos = 0;
		readPos = 0;
		currentLinePos = 0;
		modulus = 0;
		eof = false;
	}

	void setInitialBuffer(byte[] out, int outPos, int outAvail) {
		// We can re-use consumer's original output array under
		// special circumstances, saving on some System.arraycopy().
		if (out != null && out.length == outAvail) {
			buffer = out;
			pos = outPos;
			readPos = outPos;
		}
	}

	private final int decodeSize;
	private static final int DEFAULT_BUFFER_SIZE = 8192;

	private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
	static final int CHUNK_SIZE = 76;
	private static final byte PAD = '=';
	static final byte[] CHUNK_SEPARATOR = { '\r', '\n' };

	/**
	 * This array is a lookup table that translates 6-bit positive integer index values into their "Base64 Alphabet"
	 * equivalents as specified in Table 1 of RFC 2045.
	 * 
	 * Thanks to "commons" project in ws.apache.org for this code.
	 * http://svn.apache.org/repos/asf/webservices/commons/trunk/modules/util/
	 */
	private static final byte[] STANDARD_ENCODE_TABLE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

	/**
	 * This is a copy of the STANDARD_ENCODE_TABLE above, but with + and / changed to - and _ to make the encoded Base64
	 * results more URL-SAFE. This table is only used when the Base64's mode is set to URL-SAFE.
	 */
	private static final byte[] URL_SAFE_ENCODE_TABLE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
			'2', '3', '4', '5', '6', '7', '8', '9', '-', '_' };
	private static final byte[] DECODE_TABLE = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62,
			-1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28,
			29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51 };
	private static final int MASK_6BITS = 0x3f;

	/** Mask used to extract 8 bits, used in decoding base64 bytes */
	private static final int MASK_8BITS = 0xff;

	private void resizeBuffer() {
		if (buffer == null) {
			buffer = new byte[DEFAULT_BUFFER_SIZE];
			pos = 0;
			readPos = 0;
		} else {
			byte[] b = new byte[buffer.length * DEFAULT_BUFFER_RESIZE_FACTOR];
			System.arraycopy(buffer, 0, b, 0, buffer.length);
			buffer = b;
		}
	}

	void decode(byte[] in, int inPos, int inAvail) {
		if (eof) {
			return;
		}
		if (inAvail < 0) {
			eof = true;
		}
		for (int i = 0; i < inAvail; i++) {
			if (buffer == null || buffer.length - pos < decodeSize) {
				resizeBuffer();
			}
			byte b = in[inPos++];
			if (b == PAD) {
				// We're done.
				eof = true;
				break;
			} else {
				if (b >= 0 && b < DECODE_TABLE.length) {
					int result = DECODE_TABLE[b];
					if (result >= 0) {
						modulus = (++modulus) % 4;
						x = (x << 6) + result;
						if (modulus == 0) {
							buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
							buffer[pos++] = (byte) ((x >> 8) & MASK_8BITS);
							buffer[pos++] = (byte) (x & MASK_8BITS);
						}
					}
				}
			}
		}

		// Two forms of EOF as far as base64 decoder is concerned: actual
		// EOF (-1) and first time '=' character is encountered in stream.
		// This approach makes the '=' padding characters completely optional.
		if (eof && modulus != 0) {
			x = x << 6;
			switch (modulus) {
			case 2:
				x = x << 6;
				buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
				break;
			case 3:
				buffer[pos++] = (byte) ((x >> 16) & MASK_8BITS);
				buffer[pos++] = (byte) ((x >> 8) & MASK_8BITS);
				break;
			}
		}
	}

	public byte[] decode(byte[] pArray) {
		reset();
		if (pArray == null || pArray.length == 0) {
			return pArray;
		}
		long len = (pArray.length * 3) / 4;
		byte[] buf = new byte[(int) len];
		setInitialBuffer(buf, 0, buf.length);
		decode(pArray, 0, pArray.length);
		decode(pArray, 0, -1); // Notify decoder of EOF.

		// Would be nice to just return buf (like we sometimes do in the encode
		// logic), but we have no idea what the line-length was (could even be
		// variable). So we cannot determine ahead of time exactly how big an
		// array is necessary. Hence the need to construct a 2nd byte array to
		// hold the final result:

		byte[] result = new byte[pos];
		readResults(result, 0, result.length);
		return result;
	}

	int readResults(byte[] b, int bPos, int bAvail) {
		if (buffer != null) {
			int len = Math.min(avail(), bAvail);
			if (buffer != b) {
				System.arraycopy(buffer, readPos, b, bPos, len);
				readPos += len;
				if (readPos >= pos) {
					buffer = null;
				}
			} else {
				// Re-using the original consumer's output array is only
				// allowed for one round.
				buffer = null;
			}
			return len;
		}
		return eof ? -1 : 0;
	}

	int avail() {
		return buffer != null ? pos - readPos : 0;
	}
}
