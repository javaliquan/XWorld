package com.founderdpt.comm.xworld.orm.mapping.generator;

import java.util.HashMap;
import java.util.Map;

import com.founderdpt.comm.xworld.orm.mapping.generator.impl.FSequenceGeneratorImpl;
import com.founderdpt.comm.xworld.orm.mapping.generator.impl.UUIDGeneratorImpl;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column.Generator;

public class GeneratorFactory {

	private static Map<String, IGeneratorID> idGenerators = new HashMap<String, IGeneratorID>();
	
	public static String defaultGenerator="uuid";
	static {
		idGenerators.put("uuid", new UUIDGeneratorImpl());
		idGenerators.put("sequence", new FSequenceGeneratorImpl());
	}
	
	public static String getGenerator(String value,Class clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		IGeneratorID  generatorID=null;
		if(value!=null){
			generatorID=idGenerators.get(value.trim().toLowerCase());
			if(generatorID==null){
				generatorID = (IGeneratorID) Class.forName(value.trim()).newInstance();
			}
		}
		if(generatorID==null){
			generatorID=idGenerators.get(defaultGenerator);
		}
		return generatorID.generator(clazz);
	}
	public static String getGeneratorColumnValue(Column column , Class<? extends Object> clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		IGeneratorID  generatorID=null;
		Generator generatorObj = column.getGenerator();
		if(generatorObj!=null){
			String generator=generatorObj.getClazz();
			if(generator!=null){
				generatorID=idGenerators.get(generator.trim().toLowerCase());
				if(generatorID==null){
					generatorID = (IGeneratorID) Class.forName(generator.trim()).newInstance();
				}
			}
		}
		if(generatorID==null){
			generatorID=idGenerators.get(defaultGenerator);
		}
		return generatorID.generatorColumnValue(column,clazz);
	}
	 
	

}
