package com.eaglec.win.utils;

import java.util.Random;

public class RandomUtils {
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static final String numberChar = "0123456789";

	public static String generateMixString(int length) {

		StringBuffer sb = new StringBuffer();

		Random random = new Random();

		for (int i = 0; i < length; i++) {

			sb.append(allChar.charAt(random.nextInt(letterChar.length())));

		}

		return sb.toString();

	}

	public static String generateNumberString(int length) {

		StringBuffer sb = new StringBuffer();

		Random random = new Random();

		for (int i = 0; i < length; i++) {

			sb.append(numberChar.charAt(random.nextInt(numberChar.length())));

		}

		return sb.toString();

	}

	public static String generateUpperString(int length) {

		return generateMixString(length).toUpperCase();

	}

	// 随机产生一个10位的随机数
	public static String generateRandomNumber() {
		String s = "";
		Random random = new Random();
		for (int i = 0; i < 10; i++) {
			int n = random.nextInt(36);
			if (n >= 0 && n <= 9) {
				s += String.valueOf(n);
			} else {
				n = n - 10;
				s += (char) (97 + n);
			}
		}
		return s;
	}
}
