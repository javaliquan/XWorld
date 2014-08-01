package com.founderdpt.comm.xworld.db;

import javax.xml.xquery.XQException;

import org.apache.log4j.Logger;

import com.founderdpt.comm.xworld.util.config.ProperConfigUtil;

public class DptXMLDBSessionFactory {
	private static Logger log = Logger.getLogger(DptXMLDBSessionFactory.class);
	private static final ThreadLocal<IXQSession> threadLocal = new ThreadLocal<IXQSession>();
	private static IXQSessionFactory xqSessionFactory;

	static {
		init();
	}

	/**
	 * 
	 * @return
	 */
	public static IXQSession getXQSession() {
		return getXQSession(true);
	}

	private static void init() {
		String sessionFactory_clazz = ProperConfigUtil
				.getConfigByKey("XQSessionFactory.className");
		try {
			xqSessionFactory = (IXQSessionFactory) Class.forName(
					sessionFactory_clazz).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			log.error("初始化sessionFactory失败", e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			log.error("初始化sessionFactory失败", e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			log.error("初始化sessionFactory失败", e);
		}
	}

	public static IXQSession getXQSession(boolean flag) {
		IXQSession xqSession = threadLocal.get();
		try {
			if ((xqSession == null || xqSession.isClosed()) && flag) {
				if (xqSessionFactory != null) {
					xqSession = xqSessionFactory.openXQSession();
				} else {
					init();
					xqSession = (xqSessionFactory != null) ? xqSessionFactory
							.openXQSession() : null;
				}
				threadLocal.set(xqSession);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xqSession;
	}

	/**
	 * 2011-11-12退出 释放资源
	 * 
	 * @throws XQException
	 */
	public static void closeXQSession() throws XQException {
		IXQSession session = threadLocal.get();
		threadLocal.set(null);
		if (session != null) {
			session.close();
		}
	}

}
