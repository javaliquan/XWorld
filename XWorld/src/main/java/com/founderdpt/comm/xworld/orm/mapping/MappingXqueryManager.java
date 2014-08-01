package com.founderdpt.comm.xworld.orm.mapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.founderdpt.comm.xworld.orm.mapping.generator.GeneratorFactory;
import com.founderdpt.comm.xworld.orm.mapping.model.XworldMapping;

public class MappingXqueryManager {
	public static String getID(Class clazz) throws JAXBException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		XworldMapping mapping = MappingManager.get(clazz);
		String generator = mapping.getId().getGenerator().getClazz();
		String id = GeneratorFactory.getGenerator(generator,clazz);
		return id;
	}
	
 

	public static StringBuilder getDocXPath(Class clazz) throws JAXBException {
		XworldMapping mapping = MappingManager.get(clazz);
		StringBuilder xquery = new StringBuilder();
		String file = mapping.getFile();
		String xpath = mapping.getXpath();
		xquery.append("doc('").append(file).append("')");
		if (xpath != null) {
			xquery.append(xpath);
		}
		return xquery;
	}

	public static String getRootName(Class clazz) {
		Annotation rootElement = clazz.getAnnotation(XmlRootElement.class);

		if (rootElement instanceof XmlRootElement) {
			XmlRootElement xmlrootElement = (XmlRootElement) rootElement;
			return xmlrootElement.name();
		} else {
			String classname = clazz.getSimpleName();
			return classname.substring(0, 1).toLowerCase()
					+ classname.substring(1);
		}
	}
	public static String getIDValue(Object paramObj) throws JAXBException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		XworldMapping mapping = MappingManager.get(paramObj.getClass());
		String idc = mapping.getId().getName();
	   return BeanUtils.getProperty(paramObj, idc);
	}
	public static StringBuilder getSelectIDQuery(String id, Class clazz)
			throws JAXBException {
		// StringBuilder xquery = new StringBuilder();
		StringBuilder xquery = getDocXPath(clazz);
		XworldMapping mapping = MappingManager.get(clazz);
		String idc = mapping.getId().getName();
		String idtype = mapping.getId().getType();

		if (StringUtils.isBlank(idc)) {
			idc = "id";
		}
		String rootname = getRootName(clazz);
		;
		xquery.append("/" + rootname);
		if ("node".equals(idtype)) {
			xquery.append("[");
		} else {
			xquery.append("[@");
		}
		xquery.append(idc);
		xquery.append("='").append(id).append("']");
		return xquery;
	}
	public static StringBuilder getSelectQuery(Class clazz)
			throws JAXBException {
		return getSelectQuery(clazz,null);
	}
	public static StringBuilder getSelectQuery(Class clazz,String where)
			throws JAXBException {
		// StringBuilder xquery = new StringBuilder();
		StringBuilder xquery = new StringBuilder();
		if(StringUtils.isNotBlank(where)){
			 xquery.append(" for $x in ");
			 xquery.append(getDocXPath(clazz));
			 String rootname = getRootName(clazz); ;
			 xquery.append("/" + rootname);
			 xquery.append(" ");
			 xquery.append(where);//$x/xxx=''
			 xquery.append(" return $x ");
		}else{
		    xquery = getDocXPath(clazz);
			String rootname = getRootName(clazz); ;
			xquery.append("/" + rootname);
		
		}
		
		return xquery;
	}
 
}
