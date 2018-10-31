package org.hhp.opensource.entityutil.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	private static Map<String, Pattern> patternCache = new HashMap<>();
	
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
	
	/**
	 * 首字母大写
	 * @param str string
	 * @return String
	 */
	public static String firstChar2UpperCase(String str) {
		char firstChar = Character.toUpperCase(str.charAt(0));
		char [] bigAttrNameChars =str.toCharArray();
		bigAttrNameChars[0] = firstChar;
		String bigAttrName = new String(bigAttrNameChars);
		return bigAttrName;
	}
	
	/**
	 * 首字母小写
	 * @param str string
	 * @return String
	 */
	public static String firstChar2LowerCase(String str) {
		char firstChar = Character.toLowerCase(str.charAt(0));
		char [] bigAttrNameChars =str.toCharArray();
		bigAttrNameChars[0] = firstChar;
		String bigAttrName = new String(bigAttrNameChars);
		return bigAttrName;
	}
	
	/**
	 * 根据正则匹配当字符串
	 * @param string 字符串
	 * @param pattern 正则字符串
	 * @return string
	 */
	public static String matchString(String string,String pattern) {
		Pattern p = patternCache.get("pattern");
		if(null==p) {
			p = Pattern.compile(pattern);
			patternCache.put(pattern, p);
		}
		Matcher m = p.matcher(string);
		
		String result = null;
		while(m.find()) {
			result = m.group(0);
			return result;
		}
		return result;
	}
	
	/**
	 * 根据正则匹配当字符串
	 * @param string 字符串
	 * @param pattern 正则字符串
	 * @return string[] 匹配到的所有字符串
	 */
	public static List<String> matchStringes(String string,String pattern) {
		Pattern p = patternCache.get("pattern");
		if(null==p) {
			p = Pattern.compile(pattern);
			patternCache.put(pattern, p);
		}
		Matcher m = p.matcher(string);
		
		List<String> result = new ArrayList<String>();  
		while(m.find()) {
			result.add(m.group(0));
		}
		return result;
	}
	
	public static String createClassName(String str) {
		return Utils.firstChar2UpperCase(Utils.toHump(str));
	}
	
	public static String createMemberVariable(String str) {
		return Utils.toHump(str);
	}
}
