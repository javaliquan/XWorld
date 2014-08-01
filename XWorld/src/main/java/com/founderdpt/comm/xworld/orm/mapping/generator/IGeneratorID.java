package com.founderdpt.comm.xworld.orm.mapping.generator;

import java.util.List;

import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping.Column;

public interface IGeneratorID {
	public String generator(Class clazz);
	public String  generatorColumnValue(Column column,Class clazz);
}
