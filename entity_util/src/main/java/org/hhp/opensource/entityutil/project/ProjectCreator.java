package org.hhp.opensource.entityutil.project;

public class ProjectCreator {

	public void createMavenProject(String dest,MavenPrjInfo info) {

		String sourceDir = dest + "/src/main/java";
		
		String resourceDir = dest + "/src/main/resources";
		String resourceStatic = resourceDir + "/static";
		String resourceTemplates = resourceDir + "/templates";
		
		String testDir = dest + "/src/test/java";
		
		//生成pom文件
		
		//生成代码
	}

}
