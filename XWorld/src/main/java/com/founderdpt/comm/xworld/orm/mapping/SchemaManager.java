package com.founderdpt.comm.xworld.orm.mapping;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

public class SchemaManager {
	public static Map cache_Schema = new HashMap();

	public static URL get(Class clazz) throws JAXBException {
		String clazz_name = clazz.getName();
		URL schema = (URL) cache_Schema.get(clazz_name);
		if (schema == null) {
			schema =clazz.getResource(clazz.getSimpleName() + ".xsd");
			cache_Schema.put(clazz_name, schema);
		}
		return schema;
	}

}
