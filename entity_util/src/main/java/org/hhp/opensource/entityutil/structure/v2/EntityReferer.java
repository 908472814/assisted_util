package org.hhp.opensource.entityutil.structure.v2;

public class EntityReferer extends Referer {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EntityReferer(String name) {
		this.name = name;
	}
}
