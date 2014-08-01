package com.founderdpt.comm.xworld.util.xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

public class JAXBUtil {
	public static Marshaller createMarshallerByClazz(Class<?> clazz)
			throws JAXBException {
		JAXBContext jax = JAXBContext.newInstance(clazz);
		Marshaller mar = jax.createMarshaller();
		mar.setProperty(Marshaller.JAXB_FRAGMENT, true);
		return mar;
	}

	public static Unmarshaller createUnMarshallerByClazz(Class<?> clazz)
			throws JAXBException {
		JAXBContext jax = JAXBContext.newInstance(clazz);
		Unmarshaller unMar = jax.createUnmarshaller();

		return unMar;
	}

	// 根据Java对象构建Xml
	public static String Object2Xml(Object object) throws JAXBException {
		Marshaller mar = createMarshallerByClazz(object.getClass());
		StringWriter strWriter = new StringWriter();
		mar.marshal(object, strWriter);
		return strWriter.toString();
	}

	// 根据Xml生成Java对象
	public static Object Xml2Object(String xmlString, Class<?> clazz)
			throws JAXBException {
		if (xmlString == null || "".equals(xmlString.trim())) {
			return null;
		}
		Unmarshaller unmarshaller = createUnMarshallerByClazz(clazz);
		return unmarshaller.unmarshal(new StringReader(xmlString));
	}

	public static Object Xml2Object(URL url, Class<?> clazz)
			throws JAXBException {
		if (url == null) {
			return null;
		}
		Unmarshaller unmarshaller = createUnMarshallerByClazz(clazz);
		return unmarshaller.unmarshal(url);
	}

/*	private static SchemaFactory schemaFactory = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);*/

	// 根据Java对象构建Xml
	public static String Object2Xml(Object object, URL schema_url)
			throws JAXBException, SAXException {
		Marshaller mar = createMarshallerByClazz(object.getClass());
		StringWriter strWriter = new StringWriter();
		if (schema_url != null) {
			SchemaFactory schemaFactory=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schema_url);
			mar.setSchema(schema);
		}
		mar.marshal(object, strWriter);
		return strWriter.toString();
	}

	public static String getXmlByObjDefSchema(Object object, URL schema_url)
			throws JAXBException, SAXException {
		if (schema_url == null) {
			String obj_name = object.getClass().getName();
			schema_url = object.getClass().getResource(obj_name + ".xsd");
		}
		return Object2Xml(object, schema_url);
	}
}
