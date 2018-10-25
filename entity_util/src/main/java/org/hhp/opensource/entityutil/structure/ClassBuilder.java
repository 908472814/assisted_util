package org.hhp.opensource.entityutil.structure;

import java.util.List;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

public class ClassBuilder {
	
	private String className;
	
	private List<ClassFieldBuilder> classFieldBuilderes;
	
	private List<AnnotationSpec.Builder> annotationSpec;
	
	private TypeSpec.Builder classBuilder;
	
	public List<ClassFieldBuilder> getClassFieldBuilderes() {
		return classFieldBuilderes;
	}

	public void setClassFieldBuilderes(List<ClassFieldBuilder> classFieldBuilderes) {
		this.classFieldBuilderes = classFieldBuilderes;
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
