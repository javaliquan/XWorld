package com.founderdpt.comm.xworld.db.basex;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;

import net.xqj.basex.BaseXXQDataSource;

import com.founderdpt.comm.xworld.db.IXQSessionFactory;
import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;

public class XQSessionFactory implements IXQSessionFactory {
	 
	public XQSession openXQSession() throws XQException {
		BaseXXQDataSource xqds = new BaseXXQDataSource();
		xqds.setProperty("serverName", ProperConfigUtil.getConfigByKey("SERVERIP"));
		xqds.setProperty("port",ProperConfigUtil.getConfigByKey("SERVERPORT"));
		XQConnection conn = xqds.getConnection(ProperConfigUtil.getConfigByKey("USER"),ProperConfigUtil.getConfigByKey("PASSWORD"));
		XQSession session = new XQSession(conn);
		return session;
	}
}
