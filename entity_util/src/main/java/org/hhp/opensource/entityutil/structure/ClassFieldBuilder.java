package org.hhp.opensource.entityutil.structure;

import java.util.ArrayList;
import java.util.List;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

public class ClassFieldBuilder {
	
	private String fieldName;
	
	private String fieldType;
	
	private FieldSpec.Builder fieldSpec;
	
	private List<AnnotationSpec.Builder> annotationSpec = new ArrayList<>();
	
	private List<MethodSpec.Builder> methodSpec = new ArrayList<>();
	
	public FieldSpec builder() {
		for(AnnotationSpec.Builder b: this.annotationSpec) {
			this.fieldSpec.addAnnotation(b.build());
		}
		return this.fieldSpec.build();
	}
	
	public List<MethodSpec> buildSetterAndGetter(){
		List<MethodSpec> s = new ArrayList<>();
		this.methodSpec.forEach(m->{
			s.add(m.build());
		});
		return s;
	}
	
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public FieldSpec.Builder getFieldSpec() {
		return fieldSpec;
	}

	public void setFieldSpec(FieldSpec.Builder fieldSpec) {
		this.fieldSpec = fieldSpec;
	}

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

	public void addAnnotationSpec(AnnotationSpec.Builder annotationSpec) {
		this.annotationSpec.add(annotationSpec);
	}
	
	public List<MethodSpec.Builder> getMethodSpec() {
		return methodSpec;
	}

	public void setMethodSpec(List<MethodSpec.Builder> methodSpec) {
		this.methodSpec = methodSpec;
	}
}
