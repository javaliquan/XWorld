package com.founderdpt.comm.xworld.plugin.data;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.founderdpt.comm.xworld.db.DptXMLDBSessionFactory;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;
import com.founderdpt.comm.xworld.util.xml.XmlAnalysis;

public class XDBDataUtil {
	protected  static Logger logger = Logger.getLogger(XDBDataUtil.class);
	public static String encoding="UTF-8";
	public static void exportData(String path) throws Exception {
		IXQSession session = null;
		try {
			session = DptXMLDBSessionFactory.getXQSession();
			session.beginTransaction();
			//session.executeCmd("useDatabase "+ProperConfigUtil.getConfigByKey("DBNAME"));
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			}
			List<String> collections = session.executeCmd("list collection / -m");
			for (String coll : collections) {
				String coll_name = coll.split(" ")[1];
				List<String> documents = session.executeCmd("list document "+coll_name.trim());
				for (String doc : documents) {
					String doc_name=doc.split(" ")[1];
					String xpath = coll_name+"/"+doc_name;
					String query="doc('"+xpath+"')";
					String result =session.executeQuery(query);
					
//				    result=xmlFormat(result).replaceAll("\r\n","");//此处格式化有问题，先注释掉，2013-12-06，lishusheng
				    System.out.println(result);
				    File output_file= new File(file.getAbsolutePath()+"/"+xpath);
				    if(logger.isDebugEnabled()){
				    	 logger.debug("导出文件路径："+output_file.getAbsolutePath());
				    }
				    FileUtils.write(output_file, result, XDBDataUtil.encoding);
				}
			}
			session.commit();
		} catch (Exception e) {
			if (session != null) {
				session.rollBack();
			}
			throw e;
		} finally {
			DptXMLDBSessionFactory.closeXQSession();
		}
	}
	public static void importData(String path,boolean del) throws Exception {
		IXQSession session = null;
		try {
			session = DptXMLDBSessionFactory.getXQSession();
			session.beginTransaction();
			File file = new File(path);
			if(!file.exists()){
				file.mkdirs();
			} 
			if(!file.isDirectory()){
				throw new Exception("只能是文件夹");
			}
			File[] listfile = file.listFiles();
			for (File coll_file : listfile) {
				if(coll_file.isDirectory()){
					try {
						String coll_name =coll_file.getName(); 
						try {
							session.executeCmd("createContainer "+coll_name);
						} catch (Exception e) {
							logger.warn(e.getMessage());
						}
						File[] doclist=coll_file.listFiles();
						for (File docfile : doclist) {
							if(docfile.isFile()){
								String doc_name =docfile.getName();
								String doc_str = FileUtils.readFileToString(docfile, XDBDataUtil.encoding);
								if(del){
									String cmd ="removeDoc "+coll_name+" "+doc_name;
									try {
										session.executeCmd(cmd);
									} catch (Exception e) {
										logger.warn(e.getMessage());
									}
								}
								String cmd ="addDoc "+coll_name+" "+doc_name+" -s '''"+doc_str+"'''";
								try {
									session.executeCmd(cmd);
								   if(logger.isDebugEnabled()){
									  logger.debug("导入文件成功  路径："+coll_name+"/"+doc_name);
								    }
								} catch (Exception e) {
									logger.warn(e.getMessage());
								}
								
							}
						}
					} catch (Exception e) {
					   logger.warn(e.getMessage());
					}
				}
			}
			session.commit();
		} catch (Exception e) {
			if (session != null) {
				session.rollBack();
			}
			throw e;
		} finally {
			DptXMLDBSessionFactory.closeXQSession();
		}
	}
	public static String xmlFormat(String str) {// 将node的XML字符串输出到控制台
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
			transformer.setOutputProperty("encoding",XDBDataUtil.encoding);
			transformer.setOutputProperty("indent", "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			//transformer.setOutputProperty(OutputKeys.METHOD, "text");
			//transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
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
}


