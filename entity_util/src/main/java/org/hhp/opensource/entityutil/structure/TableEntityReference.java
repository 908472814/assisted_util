package org.hhp.opensource.entityutil.structure;

/**
 * 实体的引用对象
 *
 */
public class TableEntityReference {
	
	/**
	 * 引用方
	 */
	private Referer referer;
	
	/**
	 * 被引用方
	 */
	private Referenced referenced;
	
	/**
	 * 1:1,1:n,n:1,n:n
	 */
	private String referenceType;

	public Referer getReferer() {
		return referer;
	}

	public void setReferer(Referer referer) {
		this.referer = referer;
	}

	public Referenced getReferenced() {
		return referenced;
	}

	public void setReferenced(Referenced referenced) {
		this.referenced = referenced;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}
	
	
}
