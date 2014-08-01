package com.founderdpt.comm.xworld.orm.mapping.generator.impl;

import java.util.List;
import java.util.UUID;

import com.founderdpt.comm.xworld.orm.mapping.generator.IGeneratorID;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column;

public class UUIDGeneratorImpl implements IGeneratorID {

	@Override
	public String generator(Class clazz) {

		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	@Override
	public String generatorColumnValue(Column column,Class clazz) {
		// TODO Auto-generated method stub
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

 

}
