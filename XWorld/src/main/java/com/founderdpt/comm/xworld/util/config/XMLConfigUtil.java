package com.founderdpt.comm.xworld.util.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.founderdpt.comm.xworld.orm.Configuration;
import com.founderdpt.comm.xworld.orm.exception.XMLConfigException;
import com.founderdpt.comm.xworld.util.POJOUtil;
import com.founderdpt.comm.xworld.util.config.builder.DynamicContext;
import com.founderdpt.comm.xworld.util.config.builder.DynamicXQLSource;
import com.founderdpt.comm.xworld.util.string.StringMatch;
import com.founderdpt.comm.xworld.util.xml.XmlAnalysis;
import com.founderdpt.comm.xworld.util.xml.XmlOperate;

/**
 * 读取CDAConfig 配置文件 依赖于XmlAnalysis文件
 * 
 * @author Administrator
 * 
 */
public class XMLConfigUtil {
	private static Logger log = Logger.getLogger(XMLConfigUtil.class);
	private static Configuration configuration = new Configuration();
  
	

	public static void main(String[] args) throws XMLConfigException {
		// XMLConfigUtil.getConfigByXpath("system");
	}

	public static DynamicContext getConfigByXpath(String nameSpace, String xpath,
			Object param) throws XMLConfigException {
		try {
			if (nameSpace == null || "".equals(nameSpace)) {
				throw new XMLConfigException("nameSpace为空");
			} else {
				Element element = (Element) configuration.getElement(nameSpace);
				if (element == null) {
					throw new XMLConfigException("无此配置文件");
				}
				if(log.isDebugEnabled()){
					log.debug("namsSpace:"+nameSpace+"  xpath:"+xpath);
				}
				//System.out.println("namsSpace:"+nameSpace+"  xpath:"+xpath);
				DynamicContext  context= DynamicXQLSource.parse(xpath, param, element);
				return context;
			}
		} catch (Exception e) {
			log.error("解析查询配置文件出错"+e.getMessage());
			throw new XMLConfigException(e);
		}
	}
	
	public static String getConfigStr(String nameSpace, String xpath,
			Object param) throws XMLConfigException {
			return getConfigByXpath(nameSpace,xpath,param).getXql_list().get(0);
	}

	
	public static List<String> getConfigStrs(String nameSpace, String xpath,
			Object param) throws XMLConfigException {
			return getConfigByXpath(nameSpace,xpath,param).getXql_list();
	}
	
	public static <T> T getConfigObject(String nameSpace, String xpath,
			Object param,Class<T> clazz) throws XMLConfigException {
		DynamicContext   dycontext = getConfigByXpath(nameSpace, xpath, param);
        List<String> xqls =	dycontext.getXql_list();
		return (T) XmlOperate.Xml2Object(xqls.get(0), clazz);
	}

	public static <T> List<T>  getConfigObjects(String nameSpace, String xpath,
			Object param,Class<T> clazz) throws XMLConfigException {
		DynamicContext   dycontext = getConfigByXpath(nameSpace, xpath, param);
        List<String> xqls =	dycontext.getXql_list();
        List<T> list = new ArrayList<T>();
        for (String xql : xqls) {
        	list.add((T) XmlOperate.Xml2Object(xql, clazz));
		}
		return list;
	}
	
	public static String getNamespaceXPath(String xpath){
			return "/mapper/"+xpath;
	}

	 
	
	
}
