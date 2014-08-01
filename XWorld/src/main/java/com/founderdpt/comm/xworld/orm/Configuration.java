package com.founderdpt.comm.xworld.orm;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.founderdpt.comm.xworld.orm.exception.XMLConfigException;
import com.founderdpt.comm.xworld.orm.property.IPropertyHandle;
import com.founderdpt.comm.xworld.orm.property.impl.AutoDDLHandle;
import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;
import com.founderdpt.comm.xworld.util.config.XMLConfigUtil;
import com.founderdpt.comm.xworld.util.xml.XmlAnalysis;

public class Configuration {
	private static Logger log = Logger.getLogger(XMLConfigUtil.class);
	private Element root;
	private Map<String, Element> element_map = new HashMap<String, Element>();
	
	private Map<String, String> element_str_map = new HashMap<String, String>();
	
	
	
	private Map<String, String> propertys = new HashMap<String, String>();
	
	private static Map<String,IPropertyHandle> propertyHandles=new HashMap<String, IPropertyHandle>();
	
	

	public static final String HANDLE_XW2DDL_COMMAND="xw2ddl.auto";
	public static final String HANDLE_XWPACKAGE_COMMAND="XWorldConfig";
	
	
	static{
		propertyHandles.put(HANDLE_XW2DDL_COMMAND.toUpperCase(),new AutoDDLHandle());
	}
	
	
	
	private void init() {
		InputStream in = ProperConfigUtil.class.getClassLoader()
				.getResourceAsStream("CommXConfig.xml");
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = factory.newDocumentBuilder();
			Document xmldoc = db.parse(in);
			root = xmldoc.getDocumentElement();
			List<String> list = XmlAnalysis.selectNodeToString(
					"/system/include/@file", root);
			for (String file_path : list) {
				InputStream temp_in = ProperConfigUtil.class.getClassLoader()
						.getResourceAsStream(file_path);
				Element temp_element = db.parse(temp_in).getDocumentElement();
				// map.put(,db.parse(temp_in).getDocumentElement());
				String namespace = XmlAnalysis.selectNodeToString(
						"/mapper/@namespace", temp_element).get(0);
				element_map.put(namespace, temp_element);
				element_str_map.put(namespace, file_path);
			}
			
			/*NodeList nodeList = XmlAnalysis.selectNode("/system/include/@file", root);
			
			int nodeList_length = nodeList.getLength();
			for (int i = 0; i < nodeList_length; i++) {
				Node node = nodeList.item(i);
			    String  property_name=node.getAttributes().getNamedItem("name").getTextContent();
			    String  property_value=node.getTextContent();
			    propertys.put(property_name.toUpperCase(), property_value);
			}*/
		} catch (Exception e) {
			log.error("初始化CommXConfig失败", e);
		}
	}

	public Configuration() {
		init();
	}

	public Element getElement(String nameSpace){
		try {
			String file_path =  element_str_map.get(nameSpace);
			InputStream temp_in = ProperConfigUtil.class.getClassLoader()
					.getResourceAsStream(file_path);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = factory.newDocumentBuilder();
			return db.parse(temp_in).getDocumentElement();
		} catch (Exception e) {
		     throw new XMLConfigException(e);
		}
	}
	
	public Map<String, Element> getElement_map() {
		return element_map;
	}

	public Map<String, String> getPropertys() {
		return propertys;
	}

	public void setPropertys(Map<String, String> propertys) {
		this.propertys = propertys;
	}

	public static Map<String, IPropertyHandle> getPropertyHandles() {
		return propertyHandles;
	}

	public static void setPropertyHandles(
			Map<String, IPropertyHandle> propertyHandles) {
		Configuration.propertyHandles = propertyHandles;
	}
	
	

}
