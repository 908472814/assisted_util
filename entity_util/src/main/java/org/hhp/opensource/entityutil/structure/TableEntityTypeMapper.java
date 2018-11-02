package org.hhp.opensource.entityutil.structure;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TableEntityTypeMapper {
	private static Map<String, Type> typeMap;
	static {
		typeMap = new HashMap<>();
		
		typeMap.put("Date", Date.class);
		typeMap.put("Time", Time.class);
		typeMap.put("Timestamp", Timestamp.class);
		typeMap.put("Clob", Clob.class);
		typeMap.put("Blob", Blob.class);
		typeMap.put("Array", Array.class);
		typeMap.put("Ref", Ref.class);
		typeMap.put("Struct", Struct.class);
		
		typeMap.put("Boolean", Boolean.class);
		typeMap.put("Byte", Byte.class);
		typeMap.put("Short", Short.class);
		typeMap.put("Integer", Integer.class);
		typeMap.put("Long", Long.class);
		typeMap.put("Double", Double.class);
		typeMap.put("Float", Float.class);
		typeMap.put("String", String.class);
		typeMap.put("BigDecimal", BigDecimal.class);
		
		typeMap.put("boolean", boolean.class);
		typeMap.put("byte", byte.class);
		typeMap.put("short", short.class);
		typeMap.put("int", int.class);
		typeMap.put("long", long.class);
		typeMap.put("double", double.class);
		typeMap.put("float", float.class);
	}
	
	public static Type getType(String typeString) {
		return typeMap.get(typeString);
	}
}
