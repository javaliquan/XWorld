package com.founderdpt.comm.xworld.util.config.builder;

import com.founderdpt.comm.xworld.orm.parsing.XNode;

public class IfXQLNode implements XQLNode {

	public boolean apply(XNode node, DynamicContext context) {

		String test = node.getNode().getAttributes().getNamedItem("test")
				.getNodeValue();
		// System.out.println(test);
		if (ExpressionEvaluator.evaluateBoolean(test, context.getBindings())) {
			// String xql = context.getXql();
			// contents.apply(context);
			return true;
		}
		return false;
	}

}
