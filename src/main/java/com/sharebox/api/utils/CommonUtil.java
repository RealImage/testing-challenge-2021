package com.sharebox.api.utils;

import java.util.Random;

public class CommonUtil {

	private static Random random = new Random();

	public static int getRandomNumber() {
		return random.nextInt(500000) + 1;
	}

}
