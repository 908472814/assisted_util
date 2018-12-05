package org.hhp.opensource.entityutil;

import java.io.IOException;
import java.util.List;

import org.hhp.opensource.entityutil.code.CommonGenerator;
import org.hhp.opensource.entityutil.code.JpaCodeGenerator;
import org.hhp.opensource.entityutil.code.JpaRepositoryGenertator;
import org.hhp.opensource.entityutil.code.ServiceGenerator;
import org.hhp.opensource.entityutil.file.NewSimpleFileReader;
import org.hhp.opensource.entityutil.project.MavenPrjInfo;
import org.hhp.opensource.entityutil.project.PackagingEnum;
import org.hhp.opensource.entityutil.project.ProjectCreator;
import org.hhp.opensource.entityutil.structure.TableEntity;

import com.alibaba.fastjson.JSON;

public class Run {

	public static void main(String[] args) throws IOException {
		
		MavenPrjInfo mvnInfo = new MavenPrjInfo();
		mvnInfo.setGroupId("org.hhp.study");
		mvnInfo.setArtifactId("test");
		mvnInfo.setName("test");
		mvnInfo.setPackaging(PackagingEnum.JAR);
		mvnInfo.setDescription("test");
		mvnInfo.setVersion("1.0.0");
		
		new ProjectCreator().createMavenProject("C:\\Users\\admin\\Desktop\\tmp\\gCode", mvnInfo);
		
		List<TableEntity> rst = new NewSimpleFileReader("D:\\hehuabing\\wkp\\stu\\assisted_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\structure\\definition.txt").read();
		
		System.out.println(JSON.toJSONString(rst));

		new JpaCodeGenerator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".domain", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new JpaRepositoryGenertator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".repository", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new ServiceGenerator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".service", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".dto", "Orders.tmp", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".dto", "Page.tmp", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".dto", "QueryParameters.tmp", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
	}
}
