/**
 * 
 */
package org.hhp.opensource.entityutil.code;

import java.util.HashMap;
import java.util.Map;

import org.hhp.opensource.entityutil.util.Utils;

/**
 * @author houhupign
 *
 */
public class CommonGenerator {

	public void generate(String pkg,String tmpltName,String target) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("package", pkg);
		
		Utils.generatorJavaCodeFromClassPathTemplate(pkg, tmpltName.split("\\.")[0], tmpltName, target, param);
	}
	
}
