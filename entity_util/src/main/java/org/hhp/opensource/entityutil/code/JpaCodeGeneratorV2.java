package org.hhp.opensource.entityutil.code;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import org.hhp.opensource.entityutil.structure.ClassBuilder;
import org.hhp.opensource.entityutil.structure.ClassFieldBuilder;
import org.hhp.opensource.entityutil.structure.Entity;
import org.hhp.opensource.entityutil.structure.EntityColumn;
import org.hhp.opensource.entityutil.structure.EntityReference;
import org.hhp.opensource.entityutil.structure.EntityTypeMapper;
import org.hhp.opensource.entityutil.util.Utils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class JpaCodeGeneratorV2 {
	
	public Map<String, ClassBuilder> relatedEntityBuilder = new HashMap<>();
	
	public void generate(List<Entity> entityList,String packageName,String target) {
		
		entityList.forEach(entiry ->{
			
			ClassBuilder classBuilder = createClass(entiry);
			
			for(EntityColumn column : entiry.getEntityColumnes()) {
				createFieldBuilder(classBuilder,entiry,column,packageName);
			}
			
			try {
				classBuilder.generate(packageName, target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private ClassBuilder createClass(Entity entiry) {
		
		String className = Utils.createClassName(entiry.getEntityName().trim());
		ClassBuilder classBuilder = this.relatedEntityBuilder.get(className);
		if(null == classBuilder) {
			classBuilder = createClassBuilder(className,entiry.getEntityName());
		}
		
		return classBuilder;
	}
	
	private void createFieldBuilder(ClassBuilder classBuilder,Entity entity,EntityColumn columne,String packag){
		
		String columneName = columne.getColumnName();
		String columnType = columne.getColumnType();
		
		List<EntityReference> reference = entity.getEntityReference(columneName);
		
		//字段，以及注解,getter,setter
		String fieldName = Utils.createMemberVariable(columneName);
		
		ClassFieldBuilder fieldBuilder = new ClassFieldBuilder();
		fieldBuilder.setFieldName(fieldName);
		fieldBuilder.setFieldType(columnType);
		
		FieldSpec.Builder fieldSpec = FieldSpec.builder(EntityTypeMapper.getType(columnType), fieldName,Modifier.PRIVATE);
		fieldBuilder.setFieldSpec(fieldSpec);
		addColnumAttn(fieldBuilder);
		classBuilder.addClassFieldBuilder(fieldBuilder);
		
		//引用以及注解
		
		
		//被引用的对象
		
	}
	
	private ClassBuilder createClassBuilder(String className,String entityName) {
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
		
		ClassBuilder b = new ClassBuilder();
		b.setClassName(className);
		b.setClassBuilder(classBuilder);
		
		return b;
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
	
	private void addColnumAttn(ClassFieldBuilder classFieldBuilder) {
		String fieldName = classFieldBuilder.getFieldName();
		
		AnnotationSpec.Builder columnAnttBuilder = null;
		if(fieldName.equals("id")) {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Id"));
		}else {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Column"));
			CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", fieldName);
			columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		}
		
		classFieldBuilder.addAnnotationSpec(columnAnttBuilder);
	}
}
