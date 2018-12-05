package org.hhp.opensource.entityutil.code;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hhp.opensource.entityutil.structure.TableEntity;
import org.hhp.opensource.entityutil.util.Utils;

public class ServiceGenerator {
	
	public void generate(List<TableEntity> entityList, String packageName, String target) {
		entityList.forEach(entity -> {
			
			String entityClassName = Utils.createClassName(entity.getEntityName());
			
			Map<String, String> param = new HashMap<String, String>();
			param.put("package", packageName);
			param.put("EntityClass", entityClassName);
			
			//接口
			Utils.generatorJavaCodeFromClassPathTemplate(packageName, entityClassName + "Service", "$EntityClassService.tmp", target, param);
			
			//接口實現
			Utils.generatorJavaCodeFromClassPathTemplate(packageName + ".impl", entityClassName + "ServiceImpl", "$EntityClassServiceImpl.tmp", target, param);
		});
	}
}
