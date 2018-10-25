package org.hhp.opensource.entityutil.code;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Modifier;

import org.hhp.opensource.entityutil.structure.Entity;
import org.hhp.opensource.entityutil.structure.EntityColumn;
import org.hhp.opensource.entityutil.structure.EntityReference;
import org.hhp.opensource.entityutil.structure.EntityTypeMapper;
import org.hhp.opensource.entityutil.util.Utils;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class JpaCodeGenerator {
	
	private Map<String, Integer> hasDealedfields = new HashMap<>();
	
	public Map<String,Builder> createClass(List<Entity> entityList,String packageName,String target) {
		
		Map<String,Builder> result = new HashMap<>();
		entityList.forEach(entiry ->{
			
			String className = Utils.firstChar2UpperCase(Utils.toHump(entiry.getEntityName())).trim();
			Builder classBuilder = createClassBuilder(className,entiry.getEntityName());
			
			//@Column注解 映射关系注解 getter,setter
			List<EntityColumn> columnes = entiry.getEntityColumnes();
			
			//遍历定义文件中的所有字段定义,创建字段
			columnes.forEach(columne->{
				createFieldBuilder(classBuilder,entiry,columne,packageName);
				hasDealedfields.put(columne.getColumnName(), 1);
			});
			
			//查询references定义中的n:n,1:n创建字段
			List<EntityReference> references = entiry.getEntityReferences();
			references.forEach(r ->{
				if(!hasDealedfields.containsKey(r.getReferer().getName())){
					
				}
			});
			
			result.put(className, classBuilder);
		});
		
		return result;
	}
	
	public void generator(List<Entity> entityList,String packageName,String target) {
		
		//定义字段生成entity类以及与字段相关的JPA注解
		Map<String,Builder> bList = createClass(entityList,packageName,target);
		
		
		bList.forEach((k,v)->{
			JavaFile javaFile = JavaFile.builder(packageName, v.build()).build();
			Path path = Paths.get(target + "/" + k + ".java");
			try {
				PrintStream p = new PrintStream(Files.newOutputStream(path));
				javaFile.writeTo(p);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private void createFieldBuilder(Builder classBuilder,Entity entity,EntityColumn columne,String packag){
		
		List<EntityReference> rList = entity.getEntityReferences();
		
		EntityReference fieldReference = null;
		for(EntityReference entityReference : rList) {
			if(entityReference.getReferer().getName().equals(columne.getColumnName())) {
				fieldReference = entityReference;
				break;
			}
		}
		
		FieldSpec.Builder fieldSpec = null;
		if(null == fieldReference) {
			String attrName = Utils.toHump(columne.getColumnName());
			Type type = EntityTypeMapper.getType(columne.getColumnType());
			fieldSpec = FieldSpec.builder(type,attrName, Modifier.PRIVATE);
			
			addColnumAttn(fieldSpec,columne.getColumnName(),type);
			addGetter(classBuilder,attrName,type);
			
		}else {
			String memberVariable = Utils.createMemberVariable(fieldReference.getReferenced().getEntityName());
			TypeName type = ClassName.get(packag, Utils.createClassName(memberVariable));
			fieldSpec = FieldSpec.builder(type,memberVariable, Modifier.PRIVATE);
			
			String referencedClassName = Utils.createClassName(fieldReference.getReferenced().getEntityName());
			String referenceColumnName = fieldReference.getReferenced().getEntityColumnName();
			addOrmAttn(fieldSpec,referencedClassName,columne.getColumnName(),referenceColumnName, fieldReference.getReferenceType());
			addGetter(classBuilder,memberVariable,type);
		}
		
		
		
		classBuilder.addField(fieldSpec.build());
		
	}
	
	private Builder createClassBuilder(String className,String entityName) {
		//class
		Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);
		
		//classs的@Table
		AnnotationSpec.Builder entityAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "Entity"));
		
		//classs的@Table
		AnnotationSpec.Builder tableAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "Table"));
		CodeBlock.Builder tableAnttCodeBuilder = CodeBlock.builder().add("$S", entityName);
		tableAntt.addMember("name", tableAnttCodeBuilder.build());
		
		//添加注解
		classBuilder.addAnnotation(entityAntt.build()).addAnnotation(tableAntt.build());
		return classBuilder;
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
	
	private void addOrmAttn(FieldSpec.Builder fieldBuilder,String referencedClassName,String columnName,String referencedColumnName,String ormType) {
		
		String orm = null;
		
		if ("1:1".equals(ormType)) {
			orm = "OneToOne";
		}else if ("n:1".equals(ormType)) {
			orm = "ManyToOne";
		}else if ("1:n".equals(ormType)) {
			orm = "OneToMany";
		}else if ("n:n".equals(ormType)) {
			orm = "ManyToMany";
		}
		
		AnnotationSpec.Builder ormAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence",orm));
		CodeBlock.Builder ormAnttCodeBuilder = CodeBlock.builder().add(referencedClassName + ".class");
		ormAnttBuilder.addMember("targetEntity", ormAnttCodeBuilder.build());
		
		AnnotationSpec.Builder columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "JoinColumn"));
		CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columnName);
		CodeBlock.Builder columnAnttCodeBuilder2 = CodeBlock.builder().add("$S",referencedColumnName);
		columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		columnAnttBuilder.addMember("referencedColumnName", columnAnttCodeBuilder2.build());
		fieldBuilder.addAnnotation(columnAnttBuilder.build());
		
		fieldBuilder.addAnnotation(ormAnttBuilder.build());
	}
	
	private void addColnumAttn(FieldSpec.Builder fieldBuilder,String columneName,Type colunmType) {
		AnnotationSpec.Builder columnAnttBuilder = null;
		if(columneName.equals("id")) {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Id"));
		}else {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Column"));
			CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columneName);
			columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		}
		fieldBuilder.addAnnotation(columnAnttBuilder.build());
	}
}
