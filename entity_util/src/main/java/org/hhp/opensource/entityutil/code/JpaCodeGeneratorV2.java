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
import org.hhp.opensource.entityutil.structure.Referenced;
import org.hhp.opensource.entityutil.util.Utils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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
		
		this.relatedEntityBuilder.put(className, classBuilder);
		return classBuilder;
	}
	
	private void createFieldBuilder(ClassBuilder classBuilder,Entity entity,EntityColumn columne,String packag){
		
		//字段以及注解 
		
		String columneName = columne.getColumnName();
		
		if(!entity.checekColumnIsUsedInReferences(columneName)) {
			String columnType = columne.getColumnType();
			String fieldName = Utils.createMemberVariable(columneName);
			ClassFieldBuilder fieldBuilder = new ClassFieldBuilder();
			FieldSpec.Builder fieldSpec = FieldSpec.builder(EntityTypeMapper.getType(columnType), fieldName,Modifier.PRIVATE);
			fieldBuilder.setFieldName(fieldName); 
			fieldBuilder.setFieldType(columnType);
			fieldBuilder.setFieldSpec(fieldSpec);
			addColnumAttn(fieldBuilder,columneName);
			classBuilder.addClassFieldBuilder(fieldBuilder);
		}
		
		//生成被引用表字段对应属性以及注解
		List<EntityReference> reference = entity.getEntityReference(columneName);
		reference.forEach(r->{
			
			TypeName referFieldTypeName = ClassName.get(packag, r.getReferenced().getClassName());
			FieldSpec.Builder referFieldSpecBuilder = FieldSpec.builder(referFieldTypeName, Utils.firstChar2LowerCase(r.getReferenced().getClassName()), Modifier.PRIVATE);
			
			ClassFieldBuilder referFieldBuilder = new ClassFieldBuilder();
			referFieldBuilder.setFieldSpec(referFieldSpecBuilder);
			referFieldBuilder.setFieldName(Utils.firstChar2LowerCase(r.getReferenced().getClassName()));
			referFieldBuilder.setFieldType(referFieldTypeName.toString());
			
			addOrmAttn(referFieldBuilder,r);
			
			classBuilder.addClassFieldBuilder(referFieldBuilder);
			
			//逆向映射相关
			Entity newEntiry = new Entity();
			newEntiry.setEntityName(r.getReferenced().getEntityName());
			ClassBuilder referencedClassBuild = this.createClass(newEntiry);
			
			FieldSpec.Builder referencedFieldSpecBuilder = null;
			String fildName = null;
			String fileType = null;
			if(r.getReferenceType().equals("1:1")) {
				fildName = Utils.firstChar2LowerCase(classBuilder.getClassName());
				fileType = classBuilder.getClassName();
				TypeName referencedFieldTypeName = ClassName.get(packag, classBuilder.getClassName());
				referencedFieldSpecBuilder = FieldSpec.builder(referencedFieldTypeName, fildName, Modifier.PRIVATE);
				
			}else if(r.getReferenceType().equals("n:1")){
				ClassName clazz = ClassName.get(packag, classBuilder.getClassName());
				ClassName list = ClassName.get("java.util", "List");
				TypeName listOfClazz = ParameterizedTypeName.get(list, clazz);
				fileType = listOfClazz.toString();
				fildName = Utils.firstChar2LowerCase(Utils.firstChar2LowerCase(classBuilder.getClassName()))+"List";
				referencedFieldSpecBuilder = FieldSpec.builder(listOfClazz, fildName, Modifier.PRIVATE);
				
			}
			
			ClassFieldBuilder referencedFieldBuilder = new ClassFieldBuilder();
			referencedFieldBuilder.setFieldSpec(referencedFieldSpecBuilder);
			referencedFieldBuilder.setFieldName(fildName);
			referencedFieldBuilder.setFieldType(fileType);
			
			addOrmReverseAttn(referencedFieldBuilder,r,classBuilder.getClassName(),referFieldBuilder.getFieldName());
			
			referencedClassBuild.addClassFieldBuilder(referencedFieldBuilder);

		});
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
		
		AnnotationSpec.Builder setterAntt = AnnotationSpec.builder(ClassName.get("lombok", "Setter"));
		AnnotationSpec.Builder getterAntt = AnnotationSpec.builder(ClassName.get("lombok", "Getter"));
		
		//添加注解
		classBuilder.addAnnotation(getterAntt.build())
			.addAnnotation(setterAntt.build())
			.addAnnotation(entityAntt.build())
			.addAnnotation(tableAntt.build());
		
		ClassBuilder b = new ClassBuilder();
		b.setClassName(className);
		b.setClassType(classBuilder);
		
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
	
	private void addOrmReverseAttn(ClassFieldBuilder fieldBuilder,EntityReference r,String className,String mappBy) {
		
		String orm = null;
		
		if ("1:1".equals(r.getReferenceType())) {
			orm = "OneToOne";
		}else if ("n:1".equals(r.getReferenceType())) {
			orm = "OneToMany";
		}
		
		AnnotationSpec.Builder ormAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence",orm));
		CodeBlock.Builder ormAnttCodeBuilder = CodeBlock.builder().add("$S",mappBy);
		ormAnttBuilder.addMember("mappedBy", ormAnttCodeBuilder.build());
		
		fieldBuilder.addAnnotationSpec(ormAnttBuilder);
		
	}
	
	private void addOrmAttn(ClassFieldBuilder fieldBuilder,EntityReference r) {
		
		String orm = null;
		
		if ("1:1".equals(r.getReferenceType())) {
			orm = "OneToOne";
		}else if ("n:1".equals(r.getReferenceType())) {
			orm = "ManyToOne";
		}
		
		Referenced rd = r.getReferenced();
		
		AnnotationSpec.Builder ormAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence",orm));
		CodeBlock.Builder ormAnttCodeBuilder = CodeBlock.builder().add(rd.getClassName()+ ".class");
		ormAnttBuilder.addMember("targetEntity", ormAnttCodeBuilder.build());
		
		AnnotationSpec.Builder columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "JoinColumn"));
		CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", r.getReferer().getName());
		CodeBlock.Builder columnAnttCodeBuilder2 = CodeBlock.builder().add("$S",rd.getEntityColumnName());
		columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		columnAnttBuilder.addMember("referencedColumnName", columnAnttCodeBuilder2.build());
		
		fieldBuilder.addAnnotationSpec(columnAnttBuilder);
		fieldBuilder.addAnnotationSpec(ormAnttBuilder);
		
	}
	
	private void addColnumAttn(ClassFieldBuilder classFieldBuilder,String columneName) {
		String fieldName = classFieldBuilder.getFieldName();
		AnnotationSpec.Builder columnAnttBuilder = null;
		if(fieldName.equals("id")) {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Id"));
		}else {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Column"));
			CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columneName);
			columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		}
		
		classFieldBuilder.addAnnotationSpec(columnAnttBuilder);
	}
}
