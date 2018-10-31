package org.hhp.opensource.entityutil.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * 实体定义对象
 */
public class Entity {

	private String entityName;
	
	private List<EntityColumn> entityColumnes = new ArrayList<>();
	
	private List<EntityReference> entityReferences = new ArrayList<>();
	
	public boolean checekColumnIsUsedInReferences(String columnName) {
		
		for(EntityReference r : entityReferences) {
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
	
	public List<EntityColumn> getEntityColumnes() {
		return entityColumnes;
	}

	public void setEntityColumnes(List<EntityColumn> entityColumnes) {
		this.entityColumnes = entityColumnes;
	}

	public List<EntityReference> getEntityReferences() {
		return entityReferences;
	}

	public void setEntityReferences(List<EntityReference> entityReferences) {
		this.entityReferences = entityReferences;
	}

	public void addColumn(EntityColumn entityColumn) {
		this.entityColumnes.add(entityColumn);
	}
	
	public void addReference(EntityReference entityReference) {
		this.entityReferences.add(entityReference);
	}
	
	public List<EntityReference> getEntityReference(String entityColumn){
		List<EntityReference> selected = new ArrayList<>();
		
		this.entityReferences.forEach(r->{
			if(null!=entityColumn && entityColumn.equals(r.getReferer().getName())) {
				selected.add(r);
			}
		});
		return selected;
	}
	
	public Entity(String entityName) {
		this.entityName = entityName;
	}

	public Entity() {
		
	}
}
