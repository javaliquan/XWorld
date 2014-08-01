package com.founderdpt.comm.xworld.util.config.builder;

import com.founderdpt.comm.xworld.orm.parsing.XNode;


public interface XQLNode {
	boolean apply(XNode node,DynamicContext context);
}
