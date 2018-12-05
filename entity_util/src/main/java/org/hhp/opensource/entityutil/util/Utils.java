package org.hhp.opensource.entityutil.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jodd.bean.BeanTemplateParser;
import jodd.io.FileUtil;
import jodd.template.ContextTemplateParser;
import jodd.template.MapTemplateParser;

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
	
	public static String readFileFromClassPath(String fileName) throws IOException {
		return readFileFromClassPath("org.hhp.opensource.entityutil.code.template",fileName);
	}
	
	public static String readFileFromClassPath(String pkg,String fileName) throws IOException {
		
		ClassLoader classLoader = Utils.class.getClassLoader();
		URL url = classLoader.getResource("");
		File file = new File(url.getFile() + Utils.package2path(pkg) + "/" + fileName);
		
		return FileUtil.readString(file);
	}
	
	public static String package2path(String pkg) {
		return pkg.replace(".", "/");
	}
	
	public static void writrFile(String content,String basePath,String fileName) {
		
		System.out.println("basePath : " + basePath);
		System.out.println("fileName : " + fileName);
		System.out.println("content : " + content);
		
		try {
			File dir = new File(basePath);
			dir.mkdirs();
			
			Path fpath = Paths.get(basePath  + fileName);
			if(!Files.exists(fpath)) {
				Files.createFile(fpath);
			}
			
			BufferedWriter writer = Files.newBufferedWriter(fpath);
			writer.write(content);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generatorJavaCodeFromClassPathTemplate(String pkg, String className, String tmpltName ,String target, Map<String, String> param) {
		generatorFileFromClassPathTemplate(target + "/" + Utils.package2path(pkg) + "/", className + ".java", tmpltName, param);
	}
	
	public static <T> void generatorJavaCodeFromClassPathTemplate(String pkg, String className, String tmpltName ,String target, T param) {
		generatorFileFromClassPathTemplate(target + "/" + Utils.package2path(pkg) + "/", className + ".java", tmpltName, param);
	}
	
	public static void generatorFileFromClassPathTemplate(String targetPath, String fileName, String tmpltName, Map<String, String> param) {
		
		String tmplt = null;
		try {
			tmplt = Utils.readFileFromClassPath(tmpltName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		ContextTemplateParser ctpImpl = new MapTemplateParser().of(param);
		String resultImpl = ctpImpl.parse(tmplt);
		
		Utils.writrFile(resultImpl, targetPath, fileName);
	}
	
	public static <T> void generatorFileFromClassPathTemplate(String targetPath, String fileName, String tmpltName, T param) {
		
		String tmplt = null;
		try {
			tmplt = Utils.readFileFromClassPath(tmpltName);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		ContextTemplateParser ctpImpl = new BeanTemplateParser().of(param);
		String resultImpl = ctpImpl.parse(tmplt);
		
		Utils.writrFile(resultImpl, targetPath, fileName);
	}
}
