package org.hhp.opensource.entityutil.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.hhp.opensource.entityutil.structure.Entity;
import org.hhp.opensource.entityutil.structure.EntityColumn;
import org.hhp.opensource.entityutil.structure.EntityReference;
import org.hhp.opensource.entityutil.structure.Referenced;
import org.hhp.opensource.entityutil.structure.Referer;

import jodd.util.StringUtil;
public class NewSimpleFileReader{
	
	private List<String> fileLines;
	
	private String lastLineType;
	
	public NewSimpleFileReader(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		this.fileLines = Files.readAllLines(path);
	}
	
	public List<Entity> read() {
		List<Entity> entityList = new LinkedList<>();
		
		fileLines.forEach(line ->{
			if(StringUtil.isNotBlank(line)) {
				String lineType = checkLineType(line);
				
				if("table".equals(lineType)) {
					entityList.add(new Entity(line.split("#")[0].trim()));
					
				}else if("columnStart".equals(lineType)) {
					System.out.println("开始解析表字段定义");
					
				}else if("indexStart".equals(lineType)) {
					System.out.println("开始解析表索引定义");
					
				}else if("referencesStart".equals(lineType)) {
					System.out.println("开始解析表引用定义");
					
				}else if("column".equals(lineType)) {
					Entity entity = entityList.get(entityList.size()-1);
					
					String columnName = line.split(" ")[0].replaceAll("\t","");
					String columnType = line.split(" ")[1];
					String columnComment = line.split("#'").length>1?StringUtil.cutBetween(line.split(" ")[2], "#'", "'#"):"";
					
					EntityColumn column = new EntityColumn(columnName,columnType,columnComment);
					entity.addColumn(column);
					
				}else if("references".equals(lineType)) {
					Entity entity = entityList.get(entityList.size()-1);
					
					String left = line.split("->")[0].trim();
					String right = line.split(" ")[2].trim();
					String referenceType = line.split(" ")[3];
					
					EntityReference r = new EntityReference();
					r.setReferenceType(referenceType);
					r.setReferenced(createReferenced(right,referenceType));
					r.setReferer(createReferer(left,referenceType));
					entity.addReference(r);
					
				}else if("index".equals(lineType)) {
					
				}else {
					throw new RuntimeException("文件结构有问题");
				}
			}
		});
		
		return entityList;
	}
	
	private Referenced createReferenced(String right,String referenceType) {
		
		Referenced r = new Referenced(right);
		return r;
	}
	
	private Referer createReferer(String left,String referenceType) {
		return new Referer(left);
	}
	
	

	

	
	private String checkLineType(String line) {
		
		if(Pattern.matches("^([a-z]|[A-Z]|_|$).*", line)) {
			this.lastLineType="table";
			return "table";
		}
		
		if(Pattern.matches("(	|    )column", line)) {
			this.lastLineType="columnStart";
			return "columnStart";
		}
		
		if(Pattern.matches("(	|    )index", line)) {
			this.lastLineType="indexStart";
			return "indexStart";
		}
		
		if(Pattern.matches("(	|    )references", line)) {
			this.lastLineType="referencesStart";
			return "referencesStart";
		}
		
		if("indexStart".equals(this.lastLineType)) {
			return "index";
		}
		
		if("columnStart".equals(this.lastLineType)) {
			return "column";
		}
		
		if("referencesStart".equals(this.lastLineType)) {
			return "references";
		}
		
		return "other";
		
	}
}
