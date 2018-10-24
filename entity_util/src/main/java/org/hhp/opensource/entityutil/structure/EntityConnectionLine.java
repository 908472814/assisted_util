package org.hhp.opensource.entityutil.structure;

public class EntityConnectionLine {
	private String sourceEntity;
	private String sourceEntityColumn;
	private String destEntity;
	private String destEntityColumn;
	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSourceEntity() {
		return sourceEntity;
	}

	public void setSourceEntity(String sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	public String getSourceEntityColumn() {
		return sourceEntityColumn;
	}

	public void setSourceEntityColumn(String sourceEntityColumn) {
		this.sourceEntityColumn = sourceEntityColumn;
	}

	public String getDestEntity() {
		return destEntity;
	}

	public void setDestEntity(String destEntity) {
		this.destEntity = destEntity;
	}

	public String getDestEntityColumn() {
		return destEntityColumn;
	}

	public void setDestEntityColumn(String destEntityColumn) {
		this.destEntityColumn = destEntityColumn;
	}

	public EntityConnectionLine(String sourceEntity, String sourceEntityColumn, String destEntity,
			String destEntityColumn, String type) {
		this.sourceEntity = sourceEntity;
		this.sourceEntityColumn = sourceEntityColumn;
		this.destEntity = destEntity;
		this.destEntityColumn = destEntityColumn;
		this.type = type;
	}

	public EntityConnectionLine() {
		
	}
	
	public EntityConnectionLine addSource(String sourceEntity, String sourceEntityColumn) {
		this.sourceEntity = sourceEntity;
		this.sourceEntityColumn = sourceEntityColumn;
		return this;
	}
	
	public EntityConnectionLine addDest(String destEntity,String destEntityColumn) {
		this.destEntity = destEntity;
		this.destEntityColumn = destEntityColumn;
		return this;
	}
	
	public EntityConnectionLine addType(String type) {
		this.type = type;
		return this;
	}

}
