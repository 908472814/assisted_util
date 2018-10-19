package org.hhp.opensource.entityutil.structure;

public class EntityColumn {
	private String name;
	private String type;
	private String rangeOfValue;
	private EntityReferenceColnum referenceColnum;

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

	public EntityReferenceColnum getReferenceColnum() {
		return referenceColnum;
	}

	public void setReferenceColnum(EntityReferenceColnum referenceColnum) {
		this.referenceColnum = referenceColnum;
	}

}
