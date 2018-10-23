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
import org.hhp.opensource.entityutil.structure.EntityReferenceColnum;
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

public class JpaCodeGenerator {
	
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
				EntityReferenceColnum reference = columne.getReferenceColnum();
				FieldSpec.Builder fieldBuilder = null;
				
				if(null!=reference && null!= reference.getReferencedEntity() && null!= reference.getReferencedColumn() && null!=reference.getType()) {
					
					//属性设置为被引用的table对应的类名
					String attrName = Utils.toHump(reference.getReferencedEntity());
					
					//被引用的table对应的类名
					String referenceClassName = Utils.firstChar2UpperCase(Utils.toHump(reference.getReferencedEntity()));
					
					if(reference.getType().equals("(1:1)")) {
						
						TypeName colunmType = ClassName.get(packageName, referenceClassName);
						fieldBuilder = FieldSpec.builder(colunmType, attrName, Modifier.PRIVATE);
						addOrmAttn(fieldBuilder,referenceClassName,columne.getName(),reference.getReferencedColumn(),"OneToOne");
						addGetter(classBuilder,attrName,colunmType);
						addSetter(classBuilder,attrName,colunmType);
						
					}else if(reference.getType().equals("(1:n)")) {
						
						ClassName colunmType = ClassName.get(packageName, referenceClassName);
						ClassName list = ClassName.get("java.util", "List");
						TypeName listColumn = ParameterizedTypeName.get(list, colunmType);
						
						fieldBuilder = FieldSpec.builder(listColumn, attrName, Modifier.PRIVATE);
						
						addOrmAttn(fieldBuilder,referenceClassName,columne.getName(),reference.getReferencedColumn(),"OneToMany");
						
						addGetter(classBuilder,attrName,listColumn);
						addSetter(classBuilder,attrName,listColumn);
						
					}else if(reference.getType().equals("(n:1)")) {
						
						TypeName colunmType = ClassName.get(packageName, referenceClassName);
						fieldBuilder = FieldSpec.builder(colunmType, attrName, Modifier.PRIVATE);
						addOrmAttn(fieldBuilder,referenceClassName,columne.getName(),reference.getReferencedColumn(),"ManyToOne");
						addGetter(classBuilder,attrName,colunmType);
						addSetter(classBuilder,attrName,colunmType);
						
					}else if(reference.getType().equals("(n:n)")) {
						
					}
					
				}else {
					String attrName = Utils.toHump(columne.getName());
					Type colunmType = EntityTypeMapper.getType(columne.getType());
					
					fieldBuilder = FieldSpec.builder(colunmType, attrName, Modifier.PRIVATE);
					
					AnnotationSpec.Builder columnAnttBuilder = null;
					if(attrName.equals("id")) {
						columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Id"));
					}else {
						columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Column"));
						CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columne.getName());
						columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
					}
					fieldBuilder.addAnnotation(columnAnttBuilder.build());
					
					addGetter(classBuilder,attrName,colunmType);
					addSetter(classBuilder,attrName,colunmType);
				}
				
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
