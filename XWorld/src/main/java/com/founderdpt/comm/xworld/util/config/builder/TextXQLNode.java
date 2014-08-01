package com.founderdpt.comm.xworld.util.config.builder;

import com.founderdpt.comm.xworld.orm.parsing.BindingTokenParser;
import com.founderdpt.comm.xworld.orm.parsing.GenericTokenParser;
import com.founderdpt.comm.xworld.orm.parsing.XNode;

public class TextXQLNode implements XQLNode {
	private String text;

	public TextXQLNode(String text) {
		this.text = text;
	}

	public boolean apply(XNode node, DynamicContext context) {
		GenericTokenParser parser = new GenericTokenParser("${", "}",
				new BindingTokenParser(context));
		context.appendXQ(parser.parse(text));
		return true;
	}

}