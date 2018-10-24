package org.hhp.opensource.entityutil.structure.v2;

import java.util.ArrayList;
import java.util.List;

/**
 * 引用者
 */
public class ColumnReferer extends Referer {
	private List<String> columnes = new ArrayList<>();

	public List<String> getColumnes() {
		return columnes;
	}

	public void setColumnes(List<String> columnes) {
		this.columnes = columnes;
	}

	public void add(String column) {
		this.columnes.add(column);
	}

	public ColumnReferer() {
		
	}

	public ColumnReferer(String columnes) {
		String [] tmp = columnes.split(",");
		for(String c:tmp) {
			this.columnes.add(c);
		}
	}
	
}
