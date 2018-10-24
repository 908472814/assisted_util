package org.hhp.opensource.entityutil.structure.v2;

/**
 * 实体字段定义
 */
public class EntityColumn {
	
	/**
	 * 字段名称
	 */
	private String columnName;
	
	/**
	 * 字段类型
	 */
	private String columnType;
	
	/**
	 * 字段注释
	 */
	private String columnComment;
	
	/**
	 * 字段取值范围
	 */
	private String columnRange;

	public String getColumnName() {
		return columnName;
	}

	public EntityColumn(String columnName, String columnType, String columnComment) {
		this.columnName = columnName;
		this.columnType = columnType;
		this.columnComment = columnComment;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}

	public String getColumnRange() {
		return columnRange;
	}

	public void setColumnRange(String columnRange) {
		this.columnRange = columnRange;
	}

}
