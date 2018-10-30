package org.hhp.opensource.entityutil.structure;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class ClassBuilder {
	
	private String className;
	
	private List<ClassFieldBuilder> classFieldBuilderes = new ArrayList<>();
	
	private List<AnnotationSpec.Builder> annotationSpec = new ArrayList<>();
	
	private TypeSpec.Builder classBuilder;
	
	public TypeSpec build() {
		
		List<MethodSpec> allGetterAndSetter = new ArrayList<>();
		for(ClassFieldBuilder f :this.classFieldBuilderes) {
			allGetterAndSetter.addAll(f.buildSetterAndGetter());
			this.classBuilder.addField(f.builder());
		}
		
		allGetterAndSetter.forEach(m->{
			this.classBuilder.addMethod(m);
		});
		
		annotationSpec.forEach(a->{
			this.classBuilder.addAnnotation(a.build());
		});
		
		return this.classBuilder.build();
	}
	
	public void generate(String packageName, String target) throws IOException {
		JavaFile javaFile = JavaFile.builder(packageName, this.build()).build();
		Path path = Paths.get(target + "/" + className + ".java");
		PrintStream p = new PrintStream(Files.newOutputStream(path));
		javaFile.writeTo(p);
	}
	
	public List<ClassFieldBuilder> getClassFieldBuilderes() {
		return classFieldBuilderes;
	}

	public void setClassFieldBuilderes(List<ClassFieldBuilder> classFieldBuilderes) {
		this.classFieldBuilderes = classFieldBuilderes;
	}

	public void addAnnotationSpec(ClassFieldBuilder classFieldBuilder) {
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

	public TypeSpec.Builder getClassBuilder() {
		return classBuilder;
	}

	public void setClassBuilder(TypeSpec.Builder classBuilder) {
		this.classBuilder = classBuilder;
	}
	
	
}
