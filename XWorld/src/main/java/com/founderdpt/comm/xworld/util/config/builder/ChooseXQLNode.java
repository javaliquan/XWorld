package com.founderdpt.comm.xworld.util.config.builder;

import java.util.List;

import com.founderdpt.comm.xworld.orm.parsing.XNode;

public class ChooseXQLNode implements XQLNode {

	@Override
	public boolean apply(XNode node, DynamicContext context) {
		// List<XNode> cNode = node.getChildren();
		List<XNode> when_Nodes = node.evalNodes("when");
		int a = 1;
		// System.out.println(when_Nodes.size());
		for (int j = 0; j < when_Nodes.size(); j++) {
			XNode aNode = when_Nodes.get(j);
			String test = aNode.getNode().getAttributes().getNamedItem("test")
					.getNodeValue();
			// System.out.println(test);
			// System.out.println(ExpressionEvaluator.evaluateBoolean(test,
			// context.getBindings()));
			if (ExpressionEvaluator
					.evaluateBoolean(test, context.getBindings())) {
				// context.getBindings().put("item", arg1)
				DynamicXQLSource.parseOne(aNode, context);
				a++;
			}
		}
		/*
		 * for(int j=0;j<cNode.size();j++){ XNode aNode = cNode.get(j); String
		 * test =
		 * aNode.getNode().getAttributes().getNamedItem("test").getNodeValue();
		 * if (ExpressionEvaluator.evaluateBoolean(test,
		 * context.getBindings())){ parseOne(aNode,context); } }
		 */
		/*
		 * for(int i=0;i<cNode.size();i++){ XNode aNode = cNode.get(i); String
		 * test =
		 * aNode.getNode().getAttributes().getNamedItem("test").getNodeValue();
		 * }
		 */
		if (a <= 1) {
			List<XNode> when_DefualtNodes = node.evalNodes("otherwise");
			XNode defaulNode = when_DefualtNodes.get(0);
			DynamicXQLSource.parseOne(defaulNode, context);
		}
		return false;
	}

}
