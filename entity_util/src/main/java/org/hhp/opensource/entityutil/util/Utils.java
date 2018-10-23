package org.hhp.opensource.entityutil.util;

public class Utils {
	
	/**
	 * 下划线分隔转驼峰
	 * @param name name
	 * @return String
	 */
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
	
	public static String firstChar2UpperCase(String str) {
		char firstChar = Character.toUpperCase(str.charAt(0));
		char [] bigAttrNameChars =str.toCharArray();
		bigAttrNameChars[0] = firstChar;
		String bigAttrName = new String(bigAttrNameChars);
		return bigAttrName;
	}
}
