package org.hhp.opensource.entityutil.structure.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * 被引用方
 */
public class Referenced {
	private String entityName;
	private List<String> entityColumnes = new ArrayList<>();

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void addEntityColumn(String column) {
		this.entityColumnes.add(column);
	}
	
	public List<String> getEntityColumnes() {
		return entityColumnes;
	}

	public void setEntityColumnes(List<String> entityColumnes) {
		this.entityColumnes = entityColumnes;
	}

	public Referenced(String right,String referenceType) {
		
		String [] tmp = right.split(",");
		for(String c:tmp) {
			if(referenceType.equals("n:n")) {
				this.entityName = c;
			}else {
				String entityName = c.split("\\.")[0];
				String entityColumneName = c.split("\\.")[1];
				this.addEntityColumn(entityColumneName);
				if(null==this.entityName) {
					this.entityName = entityName;
				}else {
					if(!this.entityName.equals(entityName)) {
						throw new RuntimeException("被引用的实体名称应该都一样");
					}
				}
			}
			
		}
	}
}
