package org.hhp.opensource.entityutil;

import java.io.IOException;

import org.hhp.opensource.entityutil.code.JpaCodeGenerator;
import org.hhp.opensource.entityutil.file.SimpleFileReader;
import org.hhp.opensource.entityutil.structure.EntityStructure;

import com.alibaba.fastjson.JSON;

public class Run {
	
	public static void main(String[] args) throws IOException {
		
//		String url = "D:\\hehuabing\\wkp\\stu\\entity_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\courseSystem.txt";
//		EntutyFileAnalyzer a = new EntutyFileAnalyzer(url);
//		a.analyse().generateJpaSource();
//		System.out.println(a.print());
		
		
		EntityStructure es = new SimpleFileReader("D:\\hehuabing\\wkp\\stu\\assisted_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\structure\\definition.txt").read();
		System.out.println(JSON.toJSONString(es));
		
		JpaCodeGenerator j = new JpaCodeGenerator();
		j.generator(es, "org.hhb.opensource.stusystem", "C:\\generateSource");
		
		
	}
}
