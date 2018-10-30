package org.hhp.opensource.entityutil.structure;

/**
 * 被引用方
 */
public class Referenced {
	private String entityName;
	private String entityColumnName;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityColumnName() {
		return entityColumnName;
	}

	public void setEntityColumnName(String entityColumnName) {
		this.entityColumnName = entityColumnName;
	}

	public Referenced(String right) {
		String entityName = right.split("\\.")[0];
		String entityColumneName = right.split("\\.").length > 1 ? right.split("\\.")[1] : null;
		this.entityName = entityName;
		this.entityColumnName = entityColumneName;
	}
}
