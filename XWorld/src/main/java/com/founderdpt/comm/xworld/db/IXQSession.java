package com.founderdpt.comm.xworld.db;

import java.util.List;
import java.util.Map;

import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQSequence;

public interface IXQSession {
	public XQConnection getXqConnection();

	public void setXqConnection(XQConnection xqConnection);

	public List<String> executeCmd(String command) throws XQException;

	public String executeQuery(String query) throws XQException;

	public XQSequence executeQuerySequence(String query, Map map)
			throws XQException;

	public XQSequence executeQuerySequence(String query) throws XQException;

	/**
	 * 增加文档基于字符串
	 * 
	 * @param command
	 * @return
	 * @throws XQException
	 */
	public void addDOCByString(String collection, String doc_name, String str)
			throws XQException;

	/**
	 * 开始事务
	 * 
	 * @throws XQException
	 */
	public void beginTransaction() throws XQException;

	/**
	 *回滚事务
	 * 
	 * @throws XQException
	 */
	public void rollBack() throws XQException;

	/**
	 * 结束事务
	 * 
	 * @throws XQException
	 */
	public void commit() throws XQException;

	/**
	 * 查询是否关闭
	 * 
	 * @return
	 */
	public boolean isClosed();

	/**
	 * 关闭
	 * 
	 * @throws XQException
	 */
	public void close() throws XQException;
	
	

}
