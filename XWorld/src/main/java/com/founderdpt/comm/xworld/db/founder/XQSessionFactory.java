package com.founderdpt.comm.xworld.db.founder;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQDataSource;
import javax.xml.xquery.XQException;

import org.apache.log4j.Logger;

import com.founder.impl.sdk.XDBDataSource;
import com.founderdpt.comm.xworld.db.IXQSession;
import com.founderdpt.comm.xworld.db.IXQSessionFactory;
import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;

public class XQSessionFactory implements IXQSessionFactory{
	private static Logger log = Logger.getLogger(XQSession.class);
	  static XQDataSource xqds ;
	  static {
			try {
				xqds = new XDBDataSource();
				//{POOL_CONNECTION_NUM=0, SERVERPORT=8090, DBNAME=, AUTOCOMMIT=, USERNAME=admin, SERVERIP=127.0.0.1, PASSWORD=123456}
				xqds.setProperty("SERVERIP",ProperConfigUtil.getConfigByKey("SERVERIP"));
				xqds.setProperty("SERVERPORT", ProperConfigUtil.getConfigByKey("SERVERPORT")); 
			   	xqds.setProperty("DBNAME", ProperConfigUtil.getConfigByKey("DBNAME"));
				xqds.setProperty("USERNAME", ProperConfigUtil.getConfigByKey("USER"));
				xqds.setProperty("PASSWORD", ProperConfigUtil.getConfigByKey("PASSWORD"));
			    xqds.setProperty("AUTOCOMMIT","false"); 
			} catch (XQException e) {
				e.printStackTrace();
				log.error("初始化XQDataSource出错",e);
			} 
			
	  }
	/**
	 * 
	 * @param fale   
	 * @return
	 * @throws XQException
	 */
	public IXQSession openXQSession() throws XQException {
		XQConnection conn = xqds.getConnection();
		XQSession session = new XQSession(conn);
		return session;
	}
}
