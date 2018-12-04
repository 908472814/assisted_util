package org.hhp.opensource.entityutil.code;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hhp.opensource.entityutil.structure.TableEntity;
import org.hhp.opensource.entityutil.structure.TableEntityColumn;
import org.hhp.opensource.entityutil.util.Utils;

import jodd.template.ContextTemplateParser;
import jodd.template.MapTemplateParser;

/**
 * 生成spring data jpa repository接口
 * 
 * @author admin
 *
 */
public class JpaRepositoryGenertator {

	public void generate(List<TableEntity> entityList, String packageName, String target) {

		entityList.forEach(entity -> {

			String interfaceName = Utils.createClassName(entity.getEntityName()) + "Repository";
			TableEntityColumn pKey = entity.getPrimaryKey().get(0);

			StringBuffer template = new StringBuffer();
			template.append("package ${packageName};\n");
			template.append("\n");
			template.append("import org.springframework.data.jpa.repository.JpaRepository;\n");
			template.append("\n");
			template.append("import ${pojoImport};\n");
			template.append("\n");
			template.append("interface ${Clazz}Repository extends JpaRepository<${pojoClazz},${IDtype}> {\n");
			template.append("\n");
			template.append("}\n");

			Map<String, String> param = new HashMap<String, String>();
			param.put("packageName", packageName);
			param.put("Clazz", interfaceName);
			param.put("pojoClazz", Utils.createClassName(entity.getEntityName()));
			param.put("IDtype", pKey.getColumnType());
			param.put("pojoImport", entity.getPackag() + "." + Utils.createClassName(entity.getEntityName()));
			
			ContextTemplateParser ctp = new MapTemplateParser().of(param);
			String result = ctp.parse(template.toString());

			try {
				Path fpath = Paths.get(target  + interfaceName + ".java");
				if(!Files.exists(fpath)) {
					Files.createFile(fpath);
				}
				
				BufferedWriter writer = Files.newBufferedWriter(fpath);
				writer.write(result);
				writer.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}
}
