package org.hhp.opensource.entityutil.structure;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.hhp.opensource.entityutil.util.Utils;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

public class PojoBuilder {
	
	private String className;
	
	private TypeSpec.Builder classType; 
	
	private List<PojoFieldBuilder> classFieldBuilderes = new ArrayList<>();
	
	private List<AnnotationSpec.Builder> annotationSpec = new ArrayList<>();
	
	public TypeSpec build() {
		
		for(PojoFieldBuilder f :this.classFieldBuilderes) {
			this.classType.addField(f.builder());
		}
		
		annotationSpec.forEach(a->{
			this.classType.addAnnotation(a.build());
		});
		
		return this.classType.build();
	}
	
	public void generate(String packageName, String target) throws IOException {
		String basePath = target + "/" + Utils.package2path(packageName) + "/";
		File dir = new File(basePath);
		dir.mkdirs();
		
		JavaFile javaFile = JavaFile.builder(packageName, this.build()).build();
		Path path = Paths.get( basePath + className + ".java");
		PrintStream p = new PrintStream(Files.newOutputStream(path));
		javaFile.writeTo(p);
	}
	
	public List<PojoFieldBuilder> getClassFieldBuilderes() {
		return classFieldBuilderes;
	}

	public void setClassFieldBuilderes(List<PojoFieldBuilder> classFieldBuilderes) {
		this.classFieldBuilderes = classFieldBuilderes;
	}

	public void addClassFieldBuilder(PojoFieldBuilder classFieldBuilder) {
		classFieldBuilderes.add(classFieldBuilder);
	}
	
	public List<AnnotationSpec.Builder> getAnnotationSpec() {
		return annotationSpec;
	}

	public void setAnnotationSpec(List<AnnotationSpec.Builder> annotationSpec) {
		this.annotationSpec = annotationSpec;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public TypeSpec.Builder getClassType() {
		return classType;
	}

	public void setClassType(TypeSpec.Builder classType) {
		this.classType = classType;
	}
	
}
