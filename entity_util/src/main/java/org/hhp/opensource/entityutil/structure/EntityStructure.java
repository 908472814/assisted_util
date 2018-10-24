package org.hhp.opensource.entityutil.structure;

import java.util.List;

public class EntityStructure {
	private List<EntityDefinitionBlock> blockes;

	public List<EntityDefinitionBlock> getBlockes() {
		return blockes;
	}

	public void setBlockes(List<EntityDefinitionBlock> blockes) {
		this.blockes = blockes;
	}
	
	public EntityDefinitionBlock getBlock(String name) {
		EntityDefinitionBlock result = null;
		for(EntityDefinitionBlock block : this.blockes) {
			if(name.equals(block.getName())) {
				result = block;
			}
		}
		return result;
	}
}
