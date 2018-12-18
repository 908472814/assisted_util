package org.hhp.opensource.entityutil.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hhp.opensource.entityutil.structure.TableEntity;
import org.hhp.opensource.entityutil.util.Utils;

public class ControllerGenerator {
	public void generate(List<TableEntity> entityList, String basePackage, String packageName, String target) {
		entityList.forEach(entity -> {
			
			String entityClassName = Utils.createClassName(entity.getEntityName());
			
			//controller
			Map<String, String> param = new HashMap<String, String>();
			param.put("basePackage", basePackage);
			param.put("EntityClass", entityClassName);
			param.put("package", packageName);
			param.put("service", Utils.firstChar2LowerCase(entityClassName+"Service"));
			param.put("vo", packageName + ".vo");
			param.put("path",  Utils.firstChar2LowerCase(entityClassName));
			
			Utils.generatorJavaCodeFromClassPathTemplate(packageName, entityClassName + "Controller", "$EntityClassController.tmp", target, param);
			
		});
	}
}
