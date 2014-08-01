package com.founderdpt.comm.xworld.orm.mapping.generator.impl;

import java.util.List;

import javax.xml.xquery.XQException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.founderdpt.comm.xworld.db.DptXMLDBSessionFactory;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.orm.XMLConfigClientTemplate;
import com.founderdpt.comm.xworld.orm.exception.GeneratorException;
import com.founderdpt.comm.xworld.orm.mapping.MappingManager;
import com.founderdpt.comm.xworld.orm.mapping.generator.IGeneratorID;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Id.Generator.Param;

public class FSequenceGeneratorImpl implements IGeneratorID {
	private static Logger log = Logger.getLogger(FSequenceGeneratorImpl.class);

	@Override
	public String generator(Class clazz) {
		try {
			XworldMapping mapping = MappingManager.get(clazz);
			Id idObj = mapping.getId();
			Generator generator = idObj.getGenerator();
			List<XworldMapping.Id.Generator.Param> params = generator
					.getParam();
			String sequence_name = null;
			if (params != null) {
				for (Param param : params) {
					String name = param.getName();
					if ("sequence".equals(name)) {
						sequence_name = param.getValue();
					}
				}
			}
			if (StringUtils.isBlank(sequence_name)) {
				sequence_name = clazz.getSimpleName();
			}
			return  generatorValue(sequence_name);
		} catch (GeneratorException e) {
			throw  e;
		} catch (Exception e) {
			throw new GeneratorException("使用sequence生成ID错误", e);
		}
	}

	public String generatorValue(String sequence_name) {
		 
		try {
			IXQSession session = DptXMLDBSessionFactory.getXQSession(false);
			String seq_query="let $x := getsequencevalue(\""+ sequence_name + "\",1) return $x";
             
			if(log.isDebugEnabled()){
				System.out.println("get sequence xquery:"+seq_query);
			}
			return session.executeQuery(seq_query);
		} catch (Exception e) {
			throw new GeneratorException("使用sequence生成ID错误", e);
		}
	}
	
	@Override
	public String generatorColumnValue(Column column,Class clazz) {
		com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column.Generator generatorObj = column.getGenerator();
		List<XworldMapping.Column.Generator.Param> params =generatorObj.getParam();
		String sequence_name = null;
		if(params!=null){
			for (XworldMapping.Column.Generator.Param param : params) {
				String name = param.getName();
				if ("sequence".equals(name)) {
					sequence_name = param.getValue();
				}
			}
		}
		if (StringUtils.isBlank(sequence_name)) {
			String column_name = column.getName();
			sequence_name = clazz.getSimpleName() + "_"
					+ column_name ;
		}
		return generatorValue(sequence_name);
	}
	
}
