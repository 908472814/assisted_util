package org.hhp.opensource.entityutil.code;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.lang.model.element.Modifier;
import javax.persistence.GenerationType;

import org.hhp.opensource.entityutil.structure.PojoBuilder;
import org.hhp.opensource.entityutil.structure.PojoFieldBuilder;
import org.hhp.opensource.entityutil.structure.Referenced;
import org.hhp.opensource.entityutil.structure.TableEntity;
import org.hhp.opensource.entityutil.structure.TableEntityColumn;
import org.hhp.opensource.entityutil.structure.TableEntityReference;
import org.hhp.opensource.entityutil.structure.TableEntityTypeMapper;
import org.hhp.opensource.entityutil.util.Utils;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;

public class JpaCodeGenerator {
	
	public Map<String, PojoBuilder> relatedEntityBuilder = new HashMap<>();
	
	public void generate(List<TableEntity> entityList,String packageName,String target) {
		
		entityList.forEach(entiry ->{
			
			//pojo
			PojoBuilder classBuilder = createClass(entiry);
			
			//pojo属性，以及JPA注解
			for(TableEntityColumn column : entiry.getEntityColumnes()) {
				createFieldBuilder(classBuilder,entiry,column,packageName);
			}
			
			entiry.setPackag(packageName);
		});
		
		relatedEntityBuilder.forEach((k,v)->{
			try {
				v.generate(packageName, target);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
	
	private PojoBuilder createClass(TableEntity entiry) {
		
		String className = Utils.createClassName(entiry.getEntityName().trim());
		PojoBuilder classBuilder = this.relatedEntityBuilder.get(className);
		
		if(null == classBuilder) {
			classBuilder = createClassBuilder(className,entiry.getEntityName());
			this.relatedEntityBuilder.put(className, classBuilder);
		}
		
		return classBuilder;
	}
	
	private void createFieldBuilder(PojoBuilder classBuilder,TableEntity entity,TableEntityColumn columne,String packag){
		
		//字段以及注解 
		
		String columneName = columne.getColumnName();
		
		if(!entity.checekColumnIsUsedInReferences(columneName)) {
			String columnType = columne.getColumnType();
			String fieldName = Utils.createMemberVariable(columneName);
			PojoFieldBuilder fieldBuilder = new PojoFieldBuilder();
			FieldSpec.Builder fieldSpec = FieldSpec.builder(TableEntityTypeMapper.getType(columnType), fieldName,Modifier.PRIVATE);
			fieldBuilder.setFieldName(fieldName); 
			fieldBuilder.setFieldType(columnType);
			fieldBuilder.setFieldSpec(fieldSpec);
			addColnumAttn(fieldBuilder,columneName);
			classBuilder.addClassFieldBuilder(fieldBuilder);
		}
		
		//生成被引用表字段对应属性以及注解
		List<TableEntityReference> reference = entity.getEntityReference(columneName);
		reference.forEach(r->{
			
			TypeName referFieldTypeName = ClassName.get(packag, r.getReferenced().getClassName());
			FieldSpec.Builder referFieldSpecBuilder = FieldSpec.builder(referFieldTypeName, Utils.firstChar2LowerCase(r.getReferenced().getClassName()), Modifier.PRIVATE);
			
			PojoFieldBuilder referFieldBuilder = new PojoFieldBuilder();
			referFieldBuilder.setFieldSpec(referFieldSpecBuilder);
			referFieldBuilder.setFieldName(Utils.firstChar2LowerCase(r.getReferenced().getClassName()));
			referFieldBuilder.setFieldType(referFieldTypeName.toString());
			
			addOrmAttn(referFieldBuilder,r);
			
			classBuilder.addClassFieldBuilder(referFieldBuilder);
			
			//逆向映射相关
			TableEntity newEntiry = new TableEntity();
			newEntiry.setEntityName(r.getReferenced().getEntityName());
			
			PojoBuilder referencedClassBuild = this.createClass(newEntiry);
			
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
			
			PojoFieldBuilder referencedFieldBuilder = new PojoFieldBuilder();
			referencedFieldBuilder.setFieldSpec(referencedFieldSpecBuilder);
			referencedFieldBuilder.setFieldName(fildName);
			referencedFieldBuilder.setFieldType(fileType);
			
			addOrmReverseAttn(referencedFieldBuilder,r,classBuilder.getClassName(),referFieldBuilder.getFieldName());
			
			referencedClassBuild.addClassFieldBuilder(referencedFieldBuilder);

		});
	}
	
	private PojoBuilder createClassBuilder(String className,String entityName) {
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
		
		PojoBuilder b = new PojoBuilder();
		b.setClassName(className);
		b.setClassType(classBuilder);
		
		return b;
	}
	
//	private void addGetter(Builder classBuilder,String attrName,TypeName colunmType) {
//		MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + Utils.firstChar2UpperCase(attrName))
//			    .addModifiers(Modifier.PUBLIC)
//			    .returns(colunmType)
//			    .addStatement("return this." + attrName)
//			    .build();
//		classBuilder.addMethod(getMethodSpec);
//	}
//	
//	private void addGetter(Builder classBuilder,String attrName,Type colunmType) {
//		MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + Utils.firstChar2UpperCase(attrName))
//			    .addModifiers(Modifier.PUBLIC)
//			    .returns(colunmType)
//			    .addStatement("return this." + attrName)
//			    .build();
//		classBuilder.addMethod(getMethodSpec);
//	}
//	
//	private void addSetter(Builder classBuilder,String attrName,TypeName colunmType) {
//		ParameterSpec ps = ParameterSpec.builder(colunmType, attrName).build();
//		MethodSpec setgMethodSpec = MethodSpec.methodBuilder("set" + Utils.firstChar2UpperCase(attrName))
//			    .addModifiers(Modifier.PUBLIC)
//			    .addParameter(ps)
//			    .addStatement("this." + attrName + "= " + attrName)
//			    .build();
//		classBuilder.addMethod(setgMethodSpec);
//	}
//	
//	private void addSetter(Builder classBuilder,String attrName,Type colunmType) {
//		ParameterSpec ps = ParameterSpec.builder(colunmType, attrName).build();
//		MethodSpec setgMethodSpec = MethodSpec.methodBuilder("set" + Utils.firstChar2UpperCase(attrName))
//			    .addModifiers(Modifier.PUBLIC)
//			    .addParameter(ps)
//			    .addStatement("this." + attrName + "= " + attrName)
//			    .build();
//		classBuilder.addMethod(setgMethodSpec);
//	}
	
	private void addOrmReverseAttn(PojoFieldBuilder fieldBuilder,TableEntityReference r,String className,String mappBy) {
		
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
	
	private void addOrmAttn(PojoFieldBuilder fieldBuilder,TableEntityReference r) {
		
		String orm = null;
		
		if ("1:1".equals(r.getReferenceType())) {
			orm = "OneToOne";
		}else if ("n:1".equals(r.getReferenceType())) {
			orm = "ManyToOne";
		}
		
		Referenced rd = r.getReferenced();
		
		AnnotationSpec.Builder ormAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence",orm));
		CodeBlock.Builder ormAnttCodeBuilder = CodeBlock.builder().add(rd.getClassName() + ".class");
		ormAnttBuilder.addMember("targetEntity", ormAnttCodeBuilder.build());
		
		AnnotationSpec.Builder columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "JoinColumn"));
		CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", r.getReferer().getName());
		CodeBlock.Builder columnAnttCodeBuilder2 = CodeBlock.builder().add("$S",rd.getEntityColumnName());
		columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		columnAnttBuilder.addMember("referencedColumnName", columnAnttCodeBuilder2.build());
		
		fieldBuilder.addAnnotationSpec(columnAnttBuilder);
		fieldBuilder.addAnnotationSpec(ormAnttBuilder);
		
	}
	
	private void addColnumAttn(PojoFieldBuilder classFieldBuilder,String columneName) {
		String fieldName = classFieldBuilder.getFieldName();
		AnnotationSpec.Builder columnAnttBuilder = null;
		if(fieldName.equals("id")) {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Id"));
			
			AnnotationSpec.Builder generatedValueAntt = AnnotationSpec.builder(ClassName.get("javax.persistence", "GeneratedValue"));
			CodeBlock.Builder generatedValueAnttCodeBuilder = CodeBlock.builder().add("$T.IDENTITY",GenerationType.class);
			generatedValueAntt.addMember("strategy", generatedValueAnttCodeBuilder.build());
			
			classFieldBuilder.addAnnotationSpec(generatedValueAntt);
		}else {
			columnAnttBuilder = AnnotationSpec.builder(ClassName.get("javax.persistence", "Column"));
			CodeBlock.Builder columnAnttCodeBuilder = CodeBlock.builder().add("$S", columneName);
			columnAnttBuilder.addMember("name", columnAnttCodeBuilder.build());
		}
		
		classFieldBuilder.addAnnotationSpec(columnAnttBuilder);
	}
}
