package org.hhp.opensource.entityutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;
import com.alibaba.fastjson.JSONObject;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import jodd.util.StringUtil;

@SuppressWarnings("restriction")
public class EntutyFileAnalyzer {

	private String classNamePattern = "^([a-z]|[A-Z]|$|_)([0-9]|[a-z]|[A-Z]|$|_)*";

	private Scanner sc;

	private EntityStructure e;
	
	private Map<String, Type> typeMap;

	public EntutyFileAnalyzer(String filePathAndName) throws FileNotFoundException {
		File file = new File(filePathAndName);
		this.sc = new Scanner(file);
		this.e = new EntityStructure();
		List<EntityDefinitionBlock> blockList = new ArrayList<EntityDefinitionBlock>();
		this.e.setBlockes(blockList);
		this.typeMap = new HashMap<>();
		this.typeMap.put("String", String.class);
		this.typeMap.put("Timestamp", Timestamp.class);
		this.typeMap.put("Long", Long.class);
	}

	public String print() {
		return JSONObject.toJSONString(this.e);
	}

	public void generateJpaSource() throws IOException {
		
		
		for(EntityDefinitionBlock b : this.e.getBlockes()) {
			
			String className = b.getName();
			Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC, Modifier.FINAL);
			
			List<EntityColumn> clmns = b.getColumnes();
			clmns.forEach(c ->{
				
				//字段
				String colunmName = c.getName();
				String colunmType = c.getType();
				String columRange = c.getRangeOfValue();
				
				FieldSpec fieldSpec = FieldSpec.builder(this.typeMap.get(colunmType), colunmName, Modifier.PRIVATE).build();
				classBuilder.addField(fieldSpec);
				
				//TODO 注解
				
				//方法
				char firstChar = Character.toUpperCase(colunmName.charAt(0));
				char [] bigColunmNameChars =colunmName.toCharArray();
				bigColunmNameChars[0] = firstChar;
				String bigColunmName = new String(bigColunmNameChars);
				
				MethodSpec getMethodSpec = MethodSpec.methodBuilder("get" + bigColunmName)
					    .addModifiers(Modifier.PUBLIC)
					    .returns(this.typeMap.get(colunmType))
					    .addStatement("return this." + colunmName)
					    .build();
				
				
				ParameterSpec ps = ParameterSpec.builder(this.typeMap.get(colunmType), colunmName).build();
				MethodSpec setgMethodSpec = MethodSpec.methodBuilder("set" + bigColunmName)
					    .addModifiers(Modifier.PUBLIC)
					    .addParameter(ps)
					    .addStatement("this." + colunmName + "= " + colunmName)
					    .build();
				
				classBuilder.addMethod(getMethodSpec);
				classBuilder.addMethod(setgMethodSpec);
				
				JavaFile javaFile = JavaFile.builder("com.example.helloworld", classBuilder.build()).build();
				try {
					Path path = Paths.get("C:\\generateSource" + "\\" + className + ".java");
					PrintStream p = new PrintStream(Files.newOutputStream(path));
					javaFile.writeTo(p);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			
		}
		
	}

	public void generateMybatisSource() {

	}

	public void generateSql() {

	}

	/**
	 * 分析定义文件
	 * 需重构专有分析类
	 * @return EntutyFileAnalyzer
	 * @throws FileNotFoundException
	 */
	public EntutyFileAnalyzer analyse() throws FileNotFoundException {
		if (!this.sc.hasNextLine()) {
			return this;
		}

		String line = sc.nextLine();
		if (Pattern.matches(this.classNamePattern, line) && StringUtil.isNotBlank(line)) {

			EntityDefinitionBlock block = new EntityDefinitionBlock();
			block.setName(line);

			List<EntityColumn> columnes = new ArrayList<EntityColumn>();
			block.setColumnes(columnes);

			this.e.getBlockes().add(block);
		} else if (StringUtil.isNotBlank(line)) {

			line = line.trim();
			String[] oColumnes = line.split(" ");
			EntityColumn columnes = new EntityColumn();

			String columnName = oColumnes[0];
			String type = oColumnes[1].split("\\(")[0];

			String rangeOfValue = oColumnes[1].split("\\(").length <= 1 ? ""
					: StringUtil.cutBetween(oColumnes[1], "(", ")");

			columnes.setName(columnName);
			columnes.setType(type);
			columnes.setRangeOfValue(rangeOfValue);
			if (line.indexOf("=>") != -1) {
				ReferenceColnum rc = new ReferenceColnum();
				String rcString = line.split("=>")[1].trim();
				rc.setReferencedEntity(rcString.split("\\.")[0]);
				rc.setReferencedColumn(rcString.split("\\.")[1]);
				rc.setType(StringUtil.cutBetween(rcString, "(", ")"));
				columnes.setReferenceColnum(rc);
			}

			this.e.getBlockes().get(this.e.getBlockes().size() - 1).getColumnes().add(columnes);
		}
		return analyse();
	}

	public static void main(String[] args) throws IOException {
		String url = "G:\\develop\\codeSpace\\openSource\\entity_util\\entity_util\\src\\main\\java\\org\\hhp\\opensource\\entityutil\\courseSystem.txt";
		EntutyFileAnalyzer a = new EntutyFileAnalyzer(url);
		a.analyse().generateJpaSource();
		System.out.println(a.print());
	}
}
