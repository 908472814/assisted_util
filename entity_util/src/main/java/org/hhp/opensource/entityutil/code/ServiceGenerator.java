package org.hhp.opensource.entityutil.code;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hhp.opensource.entityutil.structure.TableEntity;
import org.hhp.opensource.entityutil.util.Utils;

import jodd.template.ContextTemplateParser;
import jodd.template.MapTemplateParser;

public class ServiceGenerator {
	
	public void generate(List<TableEntity> entityList, String packageName, String target) {
		entityList.forEach(entity -> {
			String entityClassName = Utils.createClassName(entity.getEntityName());
			
			//接口
			String serviceTmplt = null;
			try {
				serviceTmplt = Utils.readFileFromClassPath("$EntityClassService.tmp");
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("package", packageName);
			param.put("EntityClass", entityClassName);
			
			ContextTemplateParser ctp = new MapTemplateParser().of(param);
			String result = ctp.parse(serviceTmplt.toString());
			
			Utils.writrSourceFile(result, target + "/" + Utils.package2path(packageName) + "/", entityClassName + "Service.java");
			
			//接口實現
			Utils.generatorFromClassPathTemplate(packageName + ".impl", entityClassName + "ServiceImpl", "$EntityClassServiceImpl.tmp", target, param);
		});
	}
}
