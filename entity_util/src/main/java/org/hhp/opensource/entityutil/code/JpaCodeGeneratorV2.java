package org.hhp.opensource.entityutil.code;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.lang.model.element.Modifier;

import org.hhp.opensource.entityutil.structure.EntityColumn;
import org.hhp.opensource.entityutil.structure.EntityDefinitionBlock;
import org.hhp.opensource.entityutil.structure.EntityConnectionLine;
import org.hhp.opensource.entityutil.structure.EntityStructure;
import org.hhp.opensource.entityutil.structure.EntityTypeMapper;
import org.hhp.opensource.entityutil.util.Utils;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class JpaCodeGeneratorV2 {
	
	public void generator(EntityStructure entityStructure,String packageName,String target) {
		
		List<EntityDefinitionBlock> blockes = entityStructure.getBlockes();
		blockes.forEach(blocke ->{
			
			//class
			String className = Utils.firstChar2UpperCase(Utils.toHump(blocke.getName())).trim();
			Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);
			
			//classs的@Table
			AnnotationSpec.Builder entityAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "Entity"));
			
			//classs的@Table
			AnnotationSpec.Builder tableAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "Table"));
			CodeBlock.Builder tableAnttCodeBuilder = CodeBlock.builder().add("$S", blocke.getName());
			tableAntt.addMember("name", tableAnttCodeBuilder.build());
			
			//添加注解
			classBuilder.addAnnotation(entityAntt.build()).addAnnotation(tableAntt.build());
			
			//@Column注解 映射关系注解 getter,setter
			List<EntityColumn> columnes = blocke.getColumnes();
			columnes.forEach(columne->{
				EntityConnectionLine connectionLine = columne.getConnectionLine();
				FieldSpec.Builder fieldBuilder = null;
				classBuilder.addField(fieldBuilder.build());
			});

			JavaFile javaFile = JavaFile.builder(packageName, classBuilder.build()).build();
			
			try {
				Path path = Paths.get(target + "/" + className + ".java");
				PrintStream p = new PrintStream(Files.newOutputStream(path));
				javaFile.writeTo(p);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		
	}
	
	private void addGetter(Builder classBuilder,String attrName,TypeName colunmType) {
		MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + Utils.firstChar2UpperCase(attrName))
			    .addModifiers(Modifier.PUBLIC)
			    .returns(colunmType)
			    .addStatement("return this." + attrName)
			    .build();
		classBuilder.addMethod(getMethodSpec);
	}
	
	private void addGetter(Builder classBuilder,String attrName,Type colunmType) {
		MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + Utils.firstChar2UpperCase(attrName))
			    .addModifiers(Modifier.PUBLIC)
			    .returns(colunmType)
			    .addStatement("return this." + attrName)
			    .build();
		classBuilder.addMethod(getMethodSpec);
	}
	
	private void addSetter(Builder classBuilder,String attrName,TypeName colunmType) {
		ParameterSpec ps = ParameterSpec.builder(colunmType, attrName).build();
		MethodSpec setgMethodSpec = MethodSpec.methodBuilder("set" + Utils.firstChar2UpperCase(attrName))
			    .addModifiers(Modifier.PUBLIC)
			    .addParameter(ps)
			    .addStatement("this." + attrName + "= " + attrName)
			    .build();
		classBuilder.addMethod(setgMethodSpec);
	}
	
	private void addSetter(Builder classBuilder,String attrName,Type colunmType) {
		ParameterSpec ps = ParameterSpec.builder(colunmType, attrName).build();
		MethodSpec setgMethodSpec = MethodSpec.methodBuilder("set" + Utils.firstChar2UpperCase(attrName))
			    .addModifiers(Modifier.PUBLIC)
			    .addParameter(ps)
			    .addStatement("this." + attrName + "= " + attrName)
			    .build();
		classBuilder.addMethod(setgMethodSpec);
	}
	
	private void addOrmAttn(FieldSpec.Builder fieldBuilder,String referenceClassName,String columnName,String referenceColumnName,String ormAttnName) {
		AnnotationSpec.Builder ormAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence",ormAttnName));
		CodeBlock.Builder ormAnttCodeBuilder = CodeBlock.builder().add(referenceClassName + ".class");
		ormAnttBuilder.addMember("targetEntity", ormAnttCodeBuilder.build());
		
		if(!ormAttnName.equals("oneToMany")) {
			AnnotationSpec.Builder columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "JoinColumn"));
			CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columnName);
			CodeBlock.Builder columnAnttCodeBuilder2 = CodeBlock.builder().add("$S",referenceColumnName);
			columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
			columnAnttBuilder.addMember("referencedColumnName", columnAnttCodeBuilder2.build());
			fieldBuilder.addAnnotation(columnAnttBuilder.build());
		}
		
		fieldBuilder.addAnnotation(ormAnttBuilder.build());
	}
	
}
