package org.hhp.opensource.entityutil.project;

import java.io.File;

public class ProjectCreator {

	public void createMavenProject(String dest,MavenPrjInfo info) {

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
		
		//生成代码
	}

}
