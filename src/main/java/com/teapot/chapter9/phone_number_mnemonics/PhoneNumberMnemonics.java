package com.teapot.chapter9.phone_number_mnemonics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

/**
 * P192~193
 * 电话号码助记符
 */
public class PhoneNumberMnemonics {
	// 9键手机键盘的字母排列
	Map<Character, String[]> phoneMapping = Map.of(
			'1', new String[] { "1" },
			'2', new String[] { "a", "b", "c" },
			'3', new String[] { "d", "e", "f" },
			'4', new String[] { "g", "h", "i" },
			'5', new String[] { "j", "k", "l" },
			'6', new String[] { "m", "n", "o" },
			'7', new String[] { "p", "q", "r", "s" },
			'8', new String[] { "t", "u", "v" },
			'9', new String[] { "w", "x", "y", "z" },
			'0', new String[] { "0", });
	private final String phoneNumber;

	public PhoneNumberMnemonics(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 得到两个号码的笛卡尔积
	 */
	public static String[] cartesianProduct(String[] first, String[] second) {
		ArrayList<String> product = new ArrayList<>(first.length * second.length);
		for (String item1 : first) {
			for (String item2 : second) {
				product.add(item1 + item2);
			}
		}
		return product.toArray(String[]::new);
	}

	public String[] getMnemonics() {
		String[] mnemonics = { "" };
		for (Character digit : phoneNumber.toCharArray()) {
			String[] combo = phoneMapping.get(digit);
			if (combo != null) {
				// 重复进行笛卡尔积
				mnemonics = cartesianProduct(mnemonics, combo);
			}
		}
		// 得到所有电话号码的笛卡尔积（助记符）
		return mnemonics;
	}

	public static void main(String[] args) {
		System.out.println("Enter a phone number:");
		Scanner scanner = new Scanner(System.in);
		String phoneNumber = scanner.nextLine();
		scanner.close();
		System.out.println("The possible mnemonics are:");
		PhoneNumberMnemonics pnm = new PhoneNumberMnemonics(phoneNumber);
		System.out.println(Arrays.toString(pnm.getMnemonics()));
	}
}
