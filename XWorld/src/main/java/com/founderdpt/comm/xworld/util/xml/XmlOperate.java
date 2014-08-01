package com.founderdpt.comm.xworld.util.xml;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class XmlOperate {

	// 读取xml文件
	public static String readFile(String fileName) {
		// TODO Auto-generated method stub
		StringBuffer requestXML = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(fileName), "utf-8"));
			String s = null;
			while ((s = reader.readLine()) != null) {
				requestXML.append(s);
			}
		} catch (IOException ex) {

		}
		return requestXML.toString();
	}

	// 写入xml文件
	public static void writeFile(String fileName, String responseString) {
		// TODO Auto-generated method stub
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8");
			out.write(responseString);
			out.flush();
			out.close();
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}

	// 根据Java对象构建Xml
	public static String Object2Xml(Object object) {
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		xStream.processAnnotations(object.getClass());
		return xStream.toXML(object);

	}

	// 根据List对象构建Xml
	public static String List2Xml(List<String> list, String root, String str) {
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		xStream.processAnnotations(list.getClass());
		xStream.alias(root, List.class);
		xStream.alias(str, String.class);
		return xStream.toXML(list);
	}

	// 根据List对象构建Xml
	public static String List2Xml(List<String> list, String root) {
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		xStream.processAnnotations(list.getClass());
		xStream.alias(root, List.class);
		return xStream.toXML(list);
	}

	// 根据Xml生成Java对象
	public static Object Xml2Object(String xmlString, Class<?> clazz) {
		if (xmlString == null || "".equals(xmlString.trim())) {
			return null;
		}

		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		Object rs = null;
		xStream.processAnnotations(clazz);
		rs = xStream.fromXML(xmlString);
		return rs;
	}
	
	public static XStream getXStream() {

		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		return xStream;
	}
	

	// 根据Xml生成List对象
	public static <T> List<T> Xml2List(String xmlString, String root,
			Class<T> clazz) {
		if (xmlString == null || "".equals(xmlString.trim())) {
			return null;
		}
		XStream xStream = new XStream(new XppDriver(new XmlFriendlyNameCoder(
				"_-", "_")));
		xStream.alias(root, List.class);
		xStream.processAnnotations(clazz);
		List<T> rs = (List<T>) xStream.fromXML(xmlString);
		return rs;
	}

	// 根据Xml生成List对象
	public static <T> List<T> Xml2List(String xmlString, Class<T> clazz) {
		return Xml2List(xmlString, "root", clazz);
	}

	public static void main(String[] args) {
		String xmlString = "<rdb><id>标识号</id><username>SQLServer2000 </username><url>jdbc:sqlserver://192.168.8.250:1433;DatabaseName=mydb;</url><!-- jdbc加载类 --><driver>com.microsoft.jdbc.sqlserver.SQLServerDriver</driver><!-- 用户名 --><!-- 密码 --><password>sa</password></rdb>";
		//System.out.println(Xml2Object(xmlString, RDBConnectModel.class));
		
	//	List<RDBConnectModel> list = Xml2List(xmlString, RDBConnectModel.class);
		
	}
}