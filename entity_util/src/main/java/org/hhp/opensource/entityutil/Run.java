package org.hhp.opensource.entityutil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hhp.opensource.entityutil.code.CommonGenerator;
import org.hhp.opensource.entityutil.code.ControllerGenerator;
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
		mvnInfo.setPackaging(PackagingEnum.jar);
		mvnInfo.setDescription("test");
		mvnInfo.setVersion("1.0.0");
		
		new ProjectCreator().createMavenProject("C:\\Users\\admin\\Desktop\\tmp\\gCode", mvnInfo);
		
		List<TableEntity> rst = new NewSimpleFileReader("C:\\Users\\admin\\Desktop\\tmp\\definition.txt").read();
		
		System.out.println(JSON.toJSONString(rst));

		new JpaCodeGenerator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".domain", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new JpaRepositoryGenertator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".repository", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		new ServiceGenerator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId(),mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".service", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		//bo
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".service.bo", "Order.tmp", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".service.bo", "Page.tmp", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".service.bo", "QueryParameters.tmp", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
		
		//vo
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".controller.vo", "CommonRsp.tmp", "C:\\\\Users\\\\admin\\\\Desktop\\\\tmp\\\\gCode\\\\src\\\\main\\\\java");
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".controller.vo", "DataRsp.tmp", "C:\\\\Users\\\\admin\\\\Desktop\\\\tmp\\\\gCode\\\\src\\\\main\\\\java");
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".controller.vo", "ListRsp.tmp", "C:\\\\Users\\\\admin\\\\Desktop\\\\tmp\\\\gCode\\\\src\\\\main\\\\java");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("serviceBoPkg",mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".service.bo");
		new CommonGenerator().generate(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".controller.vo", "PageRsp.tmp", "C:\\\\Users\\\\admin\\\\Desktop\\\\tmp\\\\gCode\\\\src\\\\main\\\\java",param);
		
		new ControllerGenerator().generate(rst, mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId(),mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".controller", "C:\\Users\\admin\\Desktop\\tmp\\gCode\\src\\main\\java");
	}
}
