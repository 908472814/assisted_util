package org.hhp.opensource.entityutil.structure;

import java.util.List;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;

public class ClassFieldBuilder {
	
	private String fieldName;
	
	private List<AnnotationSpec.Builder> annotationSpec;
	
	private List<MethodSpec.Builder> methodSpec;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public List<AnnotationSpec.Builder> getAnnotationSpec() {
		return annotationSpec;
	}

	public void setAnnotationSpec(List<AnnotationSpec.Builder> annotationSpec) {
		this.annotationSpec = annotationSpec;
	}

	public List<MethodSpec.Builder> getMethodSpec() {
		return methodSpec;
	}

	public void setMethodSpec(List<MethodSpec.Builder> methodSpec) {
		this.methodSpec = methodSpec;
	}
}
