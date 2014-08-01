package com.founderdpt.comm.xworld.db.founder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.xquery.XQConnection;
import javax.xml.xquery.XQException;
import javax.xml.xquery.XQPreparedExpression;
import javax.xml.xquery.XQSequence;

import org.apache.log4j.Logger;

import com.founder.admin.XDBExecuteCmd;
import com.founder.impl.sdk.XDBConnection;
import com.founderdpt.comm.xworld.db.IXQSession;


public class XQSession implements IXQSession {
	private static Logger log = Logger.getLogger(XQSession.class);
	private XQConnection xqConnection;

	public XQSession(XQConnection connection) {
		this.xqConnection = connection;
	}

	public XQConnection getXqConnection() {
		return xqConnection;
	}

	public void setXqConnection(XQConnection xqConnection) {
		this.xqConnection = xqConnection;
	}

	public List<String> executeCmd(String command) throws XQException {
	 	XDBExecuteCmd cmd = new XDBExecuteCmd((XDBConnection) xqConnection);
		return cmd.executeCommand(command); 
	}

	public String executeQuery(String query) throws XQException {
		XQConnection connection = this.getXqConnection();
		String result = connection.createExpression().executeQuery(query)
				.getSequenceAsString(null);
		return result;
	}

	public XQSequence executeQuerySequence(String query, Map bind_map)
			throws XQException {
		XQSequence sequence = null;
		XQConnection connection = this.getXqConnection();
		if (bind_map != null && !bind_map.isEmpty()) {
			if(log.isDebugEnabled()){
				log.debug("查询参数:"+bind_map);
			}
			// bind
			if (bind_map != null && !bind_map.isEmpty()) {
				XQPreparedExpression expression = connection
						.prepareExpression(query);
				Set<Map.Entry> set = bind_map.entrySet();
				for (Iterator<Map.Entry> it = set.iterator(); it.hasNext();) {
					Map.Entry entry = (Map.Entry) it.next();
					String value = (String) entry.getValue();
					if(value!=null){
						expression.bindAtomicValue(new QName((String) entry.getKey()),
								value, null);
					}
				}
				sequence = expression.executeQuery();
			} else {
				sequence = connection.createExpression().executeQuery(query);
			}
		} else {
			sequence = connection.createExpression().executeQuery(query);
		}
		return sequence;
	}

	public XQSequence executeQuerySequence(String query) throws XQException {

		return executeQuerySequence(query, null);
	}

	/**
	 * 增加文档基于字符串
	 * 
	 * @param command
	 * @return
	 * @throws XQException
	 */
	public void addDOCByString(String collection, String doc_name, String str)
			throws XQException {
		/* 
		  XDBConnection con = (XDBConnection) xqConnection; 
		  CommandExecute ce = con.getExecute();
	     ce.cmdCommand("addDoc "+collection+" "+doc_name+" -s "+str);*/
	}

	/**
	 * 开始事务
	 * 
	 * @throws XQException
	 */
	public void beginTransaction() throws XQException {
		executeCmd("beginTransaction");
	}

	/**
	 *回滚事务
	 * 
	 * @throws XQException
	 */
	public void rollBack() throws XQException {
		executeCmd("rollBack");
	}

	/**
	 * 结束事务
	 * 
	 * @throws XQException
	 */
	public void commit() throws XQException {
		executeCmd("commit");
	}

	/**
	 * 查询是否关闭
	 * 
	 * @return
	 */
	public boolean isClosed() {

		return xqConnection.isClosed();
	}

	/**
	 * 关闭
	 * 
	 * @throws XQException
	 */
	public void close() throws XQException {
		xqConnection.close();
	}

}
