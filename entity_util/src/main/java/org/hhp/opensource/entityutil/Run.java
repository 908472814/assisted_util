package org.hhp.opensource.entityutil;

import java.io.IOException;
import java.util.List;

import org.hhp.opensource.entityutil.code.JpaCodeGenerator;
import org.hhp.opensource.entityutil.code.JpaRepositoryGenertator;
import org.hhp.opensource.entityutil.file.NewSimpleFileReader;
import org.hhp.opensource.entityutil.structure.TableEntity;

import com.alibaba.fastjson.JSON;

public class Run {

	public static void main(String[] args) throws IOException {
		List<TableEntity> rst = new NewSimpleFileReader("D:\\hehuabing\\wkp\\stu\\assisted_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\structure\\definition.txt").read();
		System.out.println(JSON.toJSONString(rst));

		JpaCodeGenerator j = new JpaCodeGenerator();
		j.generate(rst, "org.hhb.opensource.domain", "C:\\Users\\admin\\Desktop\\tmp\\gCode");
		
		new JpaRepositoryGenertator().generate(rst, "org.hhb.opensource.repository", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\");
	}
}
