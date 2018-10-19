package org.hhp.opensource.entityutil.structure;

import java.util.List;

public class EntityDefinitionBlock {

	private String name;
	private String comment;
	private List<EntityColumn> columnes;
	private List<EntityIndex> indexs;

	public List<EntityIndex> getIndexs() {
		return indexs;
	}

	public void setIndexs(List<EntityIndex> indexs) {
		this.indexs = indexs;
	}

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

	public List<EntityColumn> getColumnes() {
		return columnes;
	}

	public void setColumnes(List<EntityColumn> columnes) {
		this.columnes = columnes;
	}

}
