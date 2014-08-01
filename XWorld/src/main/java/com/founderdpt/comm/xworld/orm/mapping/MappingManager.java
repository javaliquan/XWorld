package com.founderdpt.comm.xworld.orm.mapping;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column.Generator;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id;
import com.founderdpt.comm.xworld.util.xml.JAXBUtil;

public class MappingManager {
	public static Map cache_Mapping = new HashMap();

	public static XworldMapping get(Class clazz) throws JAXBException {
		String clazz_name = clazz.getName();
		XworldMapping mapping = (XworldMapping) cache_Mapping.get(clazz_name);
		if (mapping == null) {
			URL xworld_config = clazz.getResource(clazz.getSimpleName() + ".xworld.xml");
			mapping = (XworldMapping) JAXBUtil.Xml2Object(xworld_config, XworldMapping.class);
			Id id = mapping.getId();
			if(id==null){
				id=new Id();
				com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator generator = new com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator();
				generator.setClazz("uuid");
				id.setGenerator( generator);
				id.setName("id");
				id.setType("attr");
			}else{
				if(id.getGenerator()==null){
					com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator generator = new com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator();
					generator.setClazz("uuid");
					id.setGenerator(generator);
				}
				if(id.getName()==null){
					id.setName("id");
				}
				if(id.getType()==null){
					id.setType("attr");
				}
			}
			mapping.setId(id);
			cache_Mapping.put(clazz_name, mapping);
		}
		return mapping;
	}
	 
 
}
