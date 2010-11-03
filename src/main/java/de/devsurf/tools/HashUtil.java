/**
 * Copyright (C) 2010 Daniel Manzke <daniel.manzke@saperion.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.devsurf.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.devsurf.tools.io.StreamUtil;

public class HashUtil {
	private static final Logger LOGGER = Logger.getLogger(HashUtil.class.getName());
	public static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
			'B', 'C', 'D', 'E', 'F' };

	public static String newHash(String content, String algorithm) throws Exception {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(algorithm);

			byte[] bytes = content.getBytes();
			byte[] digest = md5.digest(bytes);
			return bytesToHex(digest);
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	public static HashResult newHash(InputStream stream, String algorithm) throws Exception {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(algorithm);

			InputStream digestStream = new DigestInputStream(stream, md5);
			byte[] content = StreamUtil.readBytes(digestStream, 65536, true);

			byte[] digest = md5.digest();
			String computed = bytesToHex(digest);
			return new HashResult(computed, computed, content);
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	public static HashResult verify(String content, String algorithm, String expected)
			throws Exception {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(algorithm);

			byte[] bytes = content.getBytes();
			byte[] digest = md5.digest(bytes);
			String computed = bytesToHex(digest);

			return new HashResult(computed, expected, bytes);
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	public static HashResult verify(InputStream contentStream, String algorithm, String expected)
			throws Exception {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(algorithm);
			InputStream digestStream = new DigestInputStream(contentStream, md5);
			byte[] content = StreamUtil.readBytes(digestStream, 65536, true);

			byte[] digest = md5.digest();
			String computed = bytesToHex(digest);

			return new HashResult(computed, expected, content);
		} catch (Throwable e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
			throw new Exception(e.getMessage());
		}
	}

	public static String bytesToHex(byte[] raw) {
		int length = raw.length;
		char[] hex = new char[length * 2];
		for (int i = 0; i < length; i++) {
			int value = (raw[i] + 256) % 256;
			int highIndex = value >> 4;
			int lowIndex = value & 0x0f;
			int j = i * 2;
			hex[j + 0] = DIGITS[highIndex];
			hex[j + 1] = DIGITS[lowIndex];
		}
		return new String(hex);
	}

	public static void main(String[] args) throws Exception {
		String input = "The quick brown fox jumps over the lazy dog";
		String hash = "9e107d9d372bb6826bd81d3542a419d6";
		HashResult result = verify(new ByteArrayInputStream(input.getBytes()), "MD5", hash);
		System.out.println("Expected: " + result.expected + " computed: " + result.computed
				+ " equal? " + result.isEqual());

		input = "The quick brown fox jumps over the lazy dog.";
		hash = "e4d909c290d0fb1ca068ffaddf22cbd0";
		result = verify(new ByteArrayInputStream(input.getBytes()), "MD5", hash);
		System.out.println("Expected: " + result.expected + " computed: " + result.computed
				+ " equal? " + result.isEqual());

		input = "./edifactnachricht.txt";
		hash = "98FCE0136A8F99047A59489CAC3BAA9D";
		result = verify(new BufferedInputStream(new FileInputStream(new File(input))), "MD5", hash);
		System.out.println("Expected: " + result.expected + " computed: " + result.computed
				+ " equal? " + result.isEqual());

		input = "./rohmail.eml";
		hash = "F65D715A5D669E241540D85BBC11B0F4";
		result = verify(new BufferedInputStream(new FileInputStream(new File(input))), "MD5", hash);
		System.out.println("Expected: " + result.expected + " computed: " + result.computed
				+ " equal? " + result.isEqual());

		// input = "./TODO.txt";
		// hash = "13BA9FE48697C1D2272080040FE7C215";
		// result = verify(new BufferedInputStream(new FileInputStream(new
		// File(input))), "MD5", hash);
		// System.out.println("Expected: "+result.expected+" computed: "+result.computed+" equal? "+result.isEqual());

		input = "C:/Users/dam/Desktop/Hashing/eventbus-1.4.jar";
		hash = "7cd65f1561d35842d348bbc5b57cdb51";
		result = verify(new BufferedInputStream(new FileInputStream(new File(input))), "MD5", hash);
		System.out.println("MD5 - Expected: " + result.expected + " computed: " + result.computed
				+ " equal? " + result.isEqual());

		hash = "e30d4442e9628fb2ddc1e544d801505f83bf77b2";
		result = verify(new BufferedInputStream(new FileInputStream(new File(input))), "SHA1", hash);
		System.out.println("SHA1 - Expected: " + result.expected + " computed: " + result.computed
				+ " equal? " + result.isEqual());
	}

	public static class HashResult {
		public final String computed;
		public final String expected;
		private byte[] content;

		public HashResult(String computed, String expected, byte[] content) {
			super();
			this.computed = computed;
			this.expected = expected.toUpperCase();
			this.content = content;
		}

		public boolean isEqual() {
			return computed.equalsIgnoreCase(expected);
		}

		public byte[] getContent() {
			return content;
		}
	}
}
