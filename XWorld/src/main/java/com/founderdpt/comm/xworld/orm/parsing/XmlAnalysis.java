package com.founderdpt.comm.xworld.orm.parsing;

import org.apache.log4j.Logger;
import org.w3c.dom.*;


import javax.xml.xpath.*;
 
public class XmlAnalysis {
	private static Logger logger = Logger.getLogger(XmlAnalysis.class);
	private XPathFactory xpathFactory = XPathFactory.newInstance();

	public String selectNode(String express, Document source) {// 查找节点，并返回第一个符合条件节�?
		NodeList result = null;
		XPath xpath = xpathFactory.newXPath();
		try {
			XPathExpression expr = xpath.compile(express);
			return (String) expr.evaluate(source, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			logger.debug(e);
			throw new ParsingException(e);
		}
	}
}
