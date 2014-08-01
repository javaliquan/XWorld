package com.founderdpt.comm.xworld.util.config.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.founderdpt.comm.xworld.orm.exception.ParseConfigException;
import com.founderdpt.comm.xworld.orm.parsing.XNode;
import com.founderdpt.comm.xworld.orm.parsing.XPathParser;
import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;
import com.founderdpt.comm.xworld.util.string.StringMatch;
import com.founderdpt.comm.xworld.util.string.StringUtil;
import com.founderdpt.comm.xworld.util.xml.XmlAnalysis;

public class DynamicXQLSource {
	public static final Map<String, XQLNode> nodeMap = new HashMap<String, XQLNode>();

	static {
		nodeMap.put("if", new IfXQLNode());
		//nodeMap.put("text", new TextXQLNode(""));
		nodeMap.put("choose", new ChooseXQLNode());
		nodeMap.put("where", new WhereXQLNode());
		nodeMap.put("foreach", new ForeachXQLNode());
	}

	public static DynamicContext parse(String express, Object param,
			Element source) throws InstantiationException,
			IllegalAccessException {
		NodeList nodeList = XmlAnalysis.selectNode(express, source);
		int nodeList_length = nodeList.getLength();
		DynamicContext context = new DynamicContext();
		context.setParameter(param);
		for (int i = 0; i < nodeList_length; i++) {
			context.setXql("");
			Node node = nodeList.item(i);
			XPathParser xPathParser = new XPathParser(node.getOwnerDocument());
			XNode xNode = new XNode(xPathParser, node, new Properties());
			parseOne(xNode, context);
			String rxq = StringMatch.translateParamGlobal(context.getXql(),ProperConfigUtil.getGlobalMap());
		    //rxq=rxq.replaceAll("\n","");
			//System.out.println("查询语句:"+rxq);
			context.getNodes().add(xNode);
			context.getXql_list().add(rxq);
		}
		return context;
	}

	public static void parseOne(XNode node, DynamicContext context)
			throws ParseConfigException {
		List<XNode> nodeList = node.getChildren();
		int nodeList_length = nodeList.size();
		for (int i = 0; i < nodeList_length; i++) {
			XNode n_node = nodeList.get(i);
			String node_name = n_node.getName();
			short node_type = n_node.getNode().getNodeType();
			if (node_type == Node.TEXT_NODE
					|| node_type == Node.CDATA_SECTION_NODE) {
				String node_Text = n_node.getNode().getTextContent();
				if (node_Text != null && !node_Text.trim().equals("")) {
					XQLNode   xqlnode =new TextXQLNode(node_Text);
			        xqlnode.apply(n_node, context);
				}
			} else {
			
				
				XQLNode xqlnode = nodeMap.get(node_name);
		//		System.out.println(node_name);
				if (xqlnode != null){
					/*if(node_name=="choose"){
						System.out.println("choose节点");
						List<XNode> cNode = n_node.getChildren();
						System.out.println(cNode.size());
						for(int j=0;j<cNode.size();j++){
							XNode aNode = cNode.get(j);
							String test = aNode.getNode().getAttributes().getNamedItem("test").getNodeValue();
							if (ExpressionEvaluator.evaluateBoolean(test, context.getBindings())){
								parseOne(aNode,context);
							}
						}
					}*/
						boolean fale = xqlnode.apply(n_node, context);
						if (fale) {
							parseOne(n_node, context);
						}
				}
			}
		}
	}

}
