package com.founderdpt.comm.xworld.util.config.builder;

import java.util.List;

import com.founderdpt.comm.xworld.orm.parsing.XNode;

public class WhereXQLNode implements XQLNode {

	@Override
	public boolean apply(XNode node, DynamicContext context) {
		// TODO Auto-generated method stub
		List<XNode> if_Nodes = node.evalNodes("if");
		int n=0;
		for (int j = 0; j < if_Nodes.size(); j++) {
			XNode aNode = if_Nodes.get(j);
			String test = aNode.getNode().getAttributes().getNamedItem("test")
					.getNodeValue();
			if (ExpressionEvaluator
					.evaluateBoolean(test, context.getBindings())) {
				if(n<1){
					String xql = context.getXql();
					xql+=" where";
					context.setXql(xql);
					DynamicXQLSource.parseOne(aNode, context);
					n++;
				}else{
					String xql = context.getXql();
					xql+=" and";
					context.setXql(xql);
					DynamicXQLSource.parseOne(aNode, context);
					n++;
				}
			}
		}
		return false;
	}

}
