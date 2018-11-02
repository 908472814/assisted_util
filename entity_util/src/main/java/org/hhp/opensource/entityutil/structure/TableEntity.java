package org.hhp.opensource.entityutil.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体定义对象
 */
public class TableEntity {

	private String entityName;
	
	private List<TableEntityColumn> primaryKey = new ArrayList<>();
	
	private List<TableEntityColumn> entityColumnes = new ArrayList<>();
	
	private List<TableEntityReference> entityReferences = new ArrayList<>();
	
	public boolean checekColumnIsUsedInReferences(String columnName) {
		
		for(TableEntityReference r : entityReferences) {
			if(r.getReferer().getName().equals(columnName)) {
				return true;
			}
		}
		
		return false;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	public List<TableEntityColumn> getEntityColumnes() {
		return entityColumnes;
	}

	public void setEntityColumnes(List<TableEntityColumn> entityColumnes) {
		this.entityColumnes = entityColumnes;
	}

	public List<TableEntityReference> getEntityReferences() {
		return entityReferences;
	}

	public void setEntityReferences(List<TableEntityReference> entityReferences) {
		this.entityReferences = entityReferences;
	}
	
	public void addPrimaryKey(TableEntityColumn entityColumn) {
		this.primaryKey.add(entityColumn);
	}

	public void addColumn(TableEntityColumn entityColumn) {
		this.entityColumnes.add(entityColumn);
	}
	
	public void addReference(TableEntityReference entityReference) {
		this.entityReferences.add(entityReference);
	}
	
	public List<TableEntityReference> getEntityReference(String entityColumn){
		List<TableEntityReference> selected = new ArrayList<>();
		
		this.entityReferences.forEach(r->{
			if(null!=entityColumn && entityColumn.equals(r.getReferer().getName())) {
				selected.add(r);
			}
		});
		return selected;
	}
	
	public TableEntity(String entityName) {
		this.entityName = entityName;
	}

	public TableEntity() {
		
	}
}
