package org.hhp.opensource.entityutil.structure;

import java.util.List;

public class EntityDefinitionBlock {

	private String name;
	private List<EntityColumn> columnes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<EntityColumn> getColumnes() {
		return columnes;
	}

	public void setColumnes(List<EntityColumn> columnes) {
		this.columnes = columnes;
	}

}
