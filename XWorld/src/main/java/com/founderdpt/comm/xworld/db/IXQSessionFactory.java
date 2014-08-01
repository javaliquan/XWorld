package com.founderdpt.comm.xworld.db;

import javax.xml.xquery.XQException;

public interface IXQSessionFactory {
	public IXQSession openXQSession() throws XQException;

}
