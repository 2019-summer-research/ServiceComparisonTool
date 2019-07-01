package gui;

import java.security.SecureRandom;

public class Utilities {

	public static String generateString(int len) {

		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		SecureRandom rnd = new SecureRandom();

		StringBuilder builder = new StringBuilder(len);
		for(int i = 0; i < len; i++) {
			builder.append(AB.charAt(rnd.nextInt(AB.length())));
		}
		return builder.toString();
	}

}
