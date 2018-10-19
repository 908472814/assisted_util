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
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class JpaCodeGenerator {
	
	public void generator(EntityStructure entityStructure,String packageName,String target) {
		
		List<EntityDefinitionBlock> blockes = entityStructure.getBlockes();
		blockes.forEach(blocke ->{
			
			//class
			String className = Utils.toHump(blocke.getName());
			Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
			
			//classs的@Table
			AnnotationSpec.Builder entityAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "Entity"));
			
			//classs的@Table
			AnnotationSpec.Builder tableAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "Table"));
			CodeBlock.Builder tableAnttCodeBuilder = CodeBlock.builder().add("$S", className);
			tableAntt.addMember("name", tableAnttCodeBuilder.build());
			
			//添加注解
			classBuilder.addAnnotation(entityAntt.build()).addAnnotation(tableAntt.build());
			
			//字段
			List<EntityColumn> columnes = blocke.getColumnes();
			columnes.forEach(columne->{
				Type colunmType = EntityTypeMapper.getType(columne.getType());
				String attrName = Utils.toHump(columne.getName());
				
				FieldSpec.Builder  fieldBuilder = FieldSpec.builder(colunmType, attrName, Modifier.PRIVATE);
				
				//@Column注解
				AnnotationSpec.Builder columnAnttBuilder = null;
				if(attrName.equals("id")) {
					columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Id"));
				}else {
					columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Column"));
					CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columne.getName());
					columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
				}
				fieldBuilder.addAnnotation(columnAnttBuilder.build());
				classBuilder.addField(fieldBuilder.build());
				
				//TODO 映射关系注解
				
				//getter,setter
				char firstChar = Character.toUpperCase(attrName.charAt(0));
				char [] bigAttrNameChars =attrName.toCharArray();
				bigAttrNameChars[0] = firstChar;
				String bigAttrName = new String(bigAttrNameChars);
				
				MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + attrName)
					    .addModifiers(Modifier.PUBLIC)
					    .returns(colunmType)
					    .addStatement("return this." + attrName)
					    .build();
				classBuilder.addMethod(getMethodSpec);
				
				ParameterSpec ps = ParameterSpec.builder(colunmType, attrName).build();
				MethodSpec setgMethodSpec = MethodSpec.methodBuilder("set" + bigAttrName)
					    .addModifiers(Modifier.PUBLIC)
					    .addParameter(ps)
					    .addStatement("this." + attrName + "= " + attrName)
					    .build();
				classBuilder.addMethod(setgMethodSpec);
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
	
}
