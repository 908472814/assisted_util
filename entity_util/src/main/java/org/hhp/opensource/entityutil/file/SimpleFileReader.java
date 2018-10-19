package org.hhp.opensource.entityutil.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hhp.opensource.entityutil.structure.EntityColumn;
import org.hhp.opensource.entityutil.structure.EntityDefinitionBlock;
import org.hhp.opensource.entityutil.structure.EntityIndex;
import org.hhp.opensource.entityutil.structure.EntityReferenceColnum;
import org.hhp.opensource.entityutil.structure.EntityStructure;

import com.alibaba.fastjson.JSON;

import jodd.util.StringUtil;
public class SimpleFileReader implements FileContentReader{
	
	private List<String> fileLines;
	
	private String lastLineType;
	
	public SimpleFileReader(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		this.fileLines = Files.readAllLines(path);
	}

	@Override
	public EntityStructure read() {
		
		EntityStructure structure = new EntityStructure();
		List<EntityDefinitionBlock> blockes = new LinkedList<>();
		structure.setBlockes(blockes);
		
		fileLines.forEach(line ->{
			
			if(StringUtil.isNotBlank(line)) {
				String lineType = checkLineType(line);
				if("table".equals(lineType)) {
					blockes.add(createTable(line));
					this.lastLineType = "table";
				}else if("columnStart".equals(lineType)) {
					System.out.println("开始解析表字段定义");
				}else if("indexStart".equals(lineType)) {
					System.out.println("开始解析表索引定义");
				}else if("column".equals(lineType)) {
					if(blockes.size()>0) {
						EntityDefinitionBlock block = blockes.get(blockes.size()-1);
						List<EntityColumn> columnes = block.getColumnes();
						if(null==columnes) {
							columnes = new LinkedList<>();
							block.setColumnes(columnes);
						}
						
						String columnName = line.split(" ")[0].replaceAll("\t","");
						String dataType = line.split(" ")[1];
						String comment = line.split("#'").length>1?StringUtil.cutBetween(line.split(" ")[3], "#'", "'#"):"";
						String refer = line.split("->").length>1 ? line.split("->")[1].trim():"";
						String referTable = refer.split("\\.")[0];
						
						String referTablecolumn = null;
						String referencedType = null;
						EntityReferenceColnum  er = null;
						if(line.split("->").length>1) {
							referTablecolumn = refer.split("\\.")[1].split("\\(")[0];
							String referencedTypePattern ="\\(.*\\)";
							Pattern r = Pattern.compile(referencedTypePattern);
							Matcher m = r.matcher(line);
							while(m.find()) {
								referencedType = m.group(0);
							}
							er = new EntityReferenceColnum();
							er.setReferencedEntity(referTable);
							er.setType(referencedType);
							er.setReferencedColumn(referTablecolumn);
						}
						
						EntityColumn ec = new EntityColumn();
						ec.setName(columnName);
						ec.setType(dataType);
						ec.setComment(comment);
						ec.setReferenceColnum(er);
						
						columnes.add(ec);
					}
				}else if("index".equals(lineType)) {
					if(blockes.size()>0) {
						EntityDefinitionBlock block = blockes.get(blockes.size()-1);
						List<EntityIndex> indexs = block.getIndexs();
						if(null==indexs) {
							indexs = new LinkedList<>();
							block.setIndexs(indexs);
						}
						
						EntityIndex index = new EntityIndex();
						String indexType = line.split("\\(")[0].trim().replace("	", "");
						
						String indexColumnPattern ="\\(.*\\)";
						Pattern r = Pattern.compile(indexColumnPattern);
						Matcher m = r.matcher(line);
						String indexColumn = "";
						while(m.find()) {
							indexColumn = m.group(0);
						}
						
						index.setType(indexType);
						index.setName(indexType + "_" + indexColumn.replace(",", "_").replace("(","").replace(")", ""));
						String [] indexColumns = indexColumn.split(",");
						index.setColumns(Arrays.asList(indexColumns));
						
						indexs.add(index);
					}
				}
			}
		});
		
		return structure;
	}
	
	private EntityDefinitionBlock createTable(String line) {
		EntityDefinitionBlock block = new EntityDefinitionBlock();
		block.setComment(StringUtil.cutBetween(line, "#'", "'#"));
		block.setName(line.split("#'")[0]);
		return block;
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
		
		if("indexStart".equals(this.lastLineType)) {
			return "index";
		}
		
		if("columnStart".equals(this.lastLineType)) {
			return "column";
		}
		
		return "other";
		
	}
}
