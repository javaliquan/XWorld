package com.founderdpt.comm.xworld.util.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;

public class XmlAnalysis {
	private static Logger logger = Logger.getLogger(XmlAnalysis.class);

	public static String transformerXSLT(File xslFile, String str)
			throws Exception {

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer(new StreamSource(
				xslFile));
	 
		// transformer.setParameter("nihao", "nihaoxxxxxxxxxxxx");
		StreamSource inputSourcse = new StreamSource(new StringReader(str),
				"utf-8");
		StringWriter output = new StringWriter();
		StreamResult outputResult = new StreamResult(output);
		transformer.transform(inputSourcse, outputResult);
		return output.toString();
	}

	public static String transformerXSLT(File xslFile, String str,Map<String,String> parammap)
			throws Exception {

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer(new StreamSource(
				xslFile));
		if(parammap!=null){
			Iterator<String> it=parammap.keySet().iterator();  
			while(it.hasNext()){  
			    String key;  
			    String value;  
			    key=it.next().toString();  
			    value=parammap.get(key);  
			    transformer.setParameter(key,value);
			}  
		}
		// transformer.setParameter("nihao", "nihaoxxxxxxxxxxxx");
		StreamSource inputSourcse = new StreamSource(new StringReader(str),
				"utf-8");
		StringWriter output = new StringWriter();
		StreamResult outputResult = new StreamResult(output);
		transformer.transform(inputSourcse, outputResult);
		return output.toString();
	}
	public static String transformerXSLT(String xslt, String str,Map<String,String> parammap)
			throws Exception {

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer(new StreamSource(
				new StringReader(xslt)));
		 
		if(parammap!=null){
			Iterator<String> it=parammap.keySet().iterator();  
			while(it.hasNext()){  
			    String key;  
			    String value;  
			    key=it.next().toString();  
			    value=parammap.get(key);  
			    transformer.setParameter(key,value);
			}  
		}
		StreamSource inputSourcse = new StreamSource(new StringReader(str),
				"utf-8");
		StringWriter output = new StringWriter();
		StreamResult outputResult = new StreamResult(output);
		transformer.transform(inputSourcse, outputResult);
		return output.toString();
	}
	
	public static String transformerXSLT(String xslt, String str)
			throws Exception {

		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer(new StreamSource(
				new StringReader(xslt)));
		 
		
		StreamSource inputSourcse = new StreamSource(new StringReader(str),
				"utf-8");
		StringWriter output = new StringWriter();
		StreamResult outputResult = new StreamResult(output);
		transformer.transform(inputSourcse, outputResult);
		return output.toString();
	}

	public static void output(Node node) {// 将node的XML字符串输出到控制台
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("encoding", "gb2312");
			transformer.setOutputProperty("indent", "yes");
			DOMSource source = new DOMSource();
			source.setNode(node);
			StreamResult result = new StreamResult();
			result.setOutputStream(System.out);
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static String output2String(String str) {// 将node的XML字符串输出到控制台
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			StringWriter writer = new StringWriter();
			StringReader sr = new StringReader(str);
			InputSource is = new InputSource(sr);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("encoding", "UTF-8");
			transformer.setOutputProperty("indent", "yes");
			DOMSource source = new DOMSource();
			source.setNode(doc);
			StreamResult result = new StreamResult();
			result.setWriter(writer);
			transformer.transform(source, result);
			return writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static NodeList selectNode(String express, Element source) {// 查找节点，并返回第一个符合条件节点
		NodeList result = null;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		try {
			XPathExpression expr = xpath.compile(express);
			result = (NodeList) expr.evaluate(source, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static List<String> selectNodeToString(String express, Element source) {// 查找节点，并返回第一个符合条件节点
		List<String> result = new ArrayList<String>();
		NodeList nodeList = selectNode(express, source);
		int nodeList_length = nodeList.getLength();
		for (int i = 0; i < nodeList_length; i++) {
			Node node = nodeList.item(i);
			result.add(node.getTextContent());
		}
		return result;
	}

	public static void saveXml(String fileName, Document doc) {// 将Document输出到文件
		TransformerFactory transFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes");

			DOMSource source = new DOMSource();
			source.setNode(doc);
			StreamResult result = new StreamResult();
			result.setOutputStream(new FileOutputStream(fileName));

			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static String addCDATA(String cdata_str) {
		return "<![CDATA[" + cdata_str + "]]>";

	}

}
