package org.hhp.opensource.entityutil.util;

public class Utils {
	public static String toHump(String name) {
		StringBuilder sb = new StringBuilder();

		boolean low = true;
		for (int i = 0; i < name.length(); i++) {
			if (name.charAt(i) == '_') {
				low = false;
			} else {
				if (low) {
					sb.append(name.charAt(i));
				} else {
					sb.append(Character.toUpperCase(name.charAt(i)));
					low = true;
				}
			}
		}
		return sb.toString();
	}
}
