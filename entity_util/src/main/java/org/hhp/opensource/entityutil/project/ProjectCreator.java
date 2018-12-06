package org.hhp.opensource.entityutil.project;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.hhp.opensource.entityutil.util.Utils;

public class ProjectCreator {

	public void createMavenProject(String dest,MavenPrjInfo mvnInfo) {

		String sourceDir = dest + "/src/main/java";
		String resourceDir = dest + "/src/main/resources";
		String resourceStaticDir = resourceDir + "/static";
		String resourceTemplatesDir = resourceDir + "/templates";
		String testDir = dest + "/src/test/java";
		
		File source = new File(sourceDir);
		source.mkdirs();
		
		File resource = new File(resourceDir);
		resource.mkdirs();
		
		File resourceStatic = new File(resourceStaticDir);
		resourceStatic.mkdirs();
		
		File resourceTemplates = new File(resourceTemplatesDir);
		resourceTemplates.mkdirs();
		
		File test = new File(testDir);
		test.mkdirs();
		
		//生成pom文件
		Utils.generatorFileFromClassPathTemplate(dest, "pom.xml", "org.hhp.opensource.entityutil.project", "pom.xml.tmp", mvnInfo);
		
		//生成LOG4J配置文件
		Map<String, String> log4jParam = new HashMap<>();
		log4jParam.put("basePath", "logs/" + mvnInfo.getName());
		log4jParam.put("loggerFileName", "logs/" + mvnInfo.getName() + "/" + mvnInfo.getName() + "-info.log");
		Utils.generatorFileFromClassPathTemplate(resourceDir, "log4j2.xml", "org.hhp.opensource.entityutil.project", "log4j2.xml.tmp", log4jParam);
		
		//生成application.properties配置文件
		Map<String, String> appParam = new HashMap<>();
		Utils.generatorFileFromClassPathTemplate(resourceDir, "application.properties", "org.hhp.opensource.entityutil.project", "application.properties.tmp", appParam);
		
		//入口主类
		Map<String, String> mainClass = new HashMap<>();
		mainClass.put("mapper.pkg", mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId());
		mainClass.put("package", mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId());
		mainClass.put("entity.pkg", mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId() + ".domain");
		Utils.generatorFileFromClassPathTemplate(sourceDir + "/" + Utils.package2path(mvnInfo.getGroupId() + "." + mvnInfo.getArtifactId()), "Application.java", "org.hhp.opensource.entityutil.project", "Application.java.tmp", mainClass);
	}

}
