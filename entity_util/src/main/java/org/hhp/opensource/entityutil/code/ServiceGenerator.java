package org.hhp.opensource.entityutil.code;

import java.util.List;

import org.hhp.opensource.entityutil.structure.TableEntity;
import org.hhp.opensource.entityutil.structure.TableEntityColumn;
import org.hhp.opensource.entityutil.util.Utils;

public class ServiceGenerator {
	
	public void generate(List<TableEntity> entityList, String packageName, String target) {
		entityList.forEach(entity -> {
			String interfaceName = Utils.createClassName(entity.getEntityName()) + "Service";
			TableEntityColumn pKey = entity.getPrimaryKey().get(0);
		});
	}
}
