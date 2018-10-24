package org.hhp.opensource.entityutil.structure;

public class EntityColumn {
	private String name;
	private String type;
	private String comment;
	private String rangeOfValue;
	private EntityConnectionLine connectionLine;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRangeOfValue() {
		return rangeOfValue;
	}

	public void setRangeOfValue(String rangeOfValue) {
		this.rangeOfValue = rangeOfValue;
	}

	public EntityConnectionLine getConnectionLine() {
		return connectionLine;
	}

	public void setConnectionLine(EntityConnectionLine connectionLine) {
		this.connectionLine = connectionLine;
	}

	public EntityColumn(String name, String type, EntityConnectionLine connectionLine) {
		this.name = name;
		this.type = type;
		this.connectionLine = connectionLine;
	}

	public EntityColumn(String name, String type, String comment, EntityConnectionLine connectionLine) {
		this.name = name;
		this.type = type;
		this.comment = comment;
		this.connectionLine = connectionLine;
	}

	public EntityColumn(String name, String type, String comment, String rangeOfValue,
			EntityConnectionLine referenceColnum) {
		this.name = name;
		this.type = type;
		this.comment = comment;
		this.rangeOfValue = rangeOfValue;
		this.connectionLine = connectionLine;
	}

	public EntityColumn() {
		
	}
}
