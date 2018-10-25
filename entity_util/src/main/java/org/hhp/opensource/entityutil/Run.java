package org.hhp.opensource.entityutil;

import java.io.IOException;
import java.util.List;
import org.hhp.opensource.entityutil.code.JpaCodeGenerator;
import org.hhp.opensource.entityutil.file.NewSimpleFileReader;
import org.hhp.opensource.entityutil.structure.Entity;
import com.alibaba.fastjson.JSON;

public class Run {

	public static void main(String[] args) throws IOException {
//		EntityStructure es = new SimpleFileReader(
//				"D:\\hehuabing\\wkp\\stu\\assisted_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\structure\\definition.txt")
//						.read();
//		
		List<Entity> rst = new NewSimpleFileReader("D:\\hehuabing\\wkp\\stu\\assisted_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\structure\\definition.txt").read();
		System.out.println(JSON.toJSONString(rst));

		JpaCodeGenerator j = new JpaCodeGenerator();
		j.generator(rst, "org.hhb.opensource.stusystem", "D:\\hehuabing\\wkp\\stu\\entity_util_test\\src\\main\\java\\org\\hhb\\opensource\\stusystem\\");
	}
}
