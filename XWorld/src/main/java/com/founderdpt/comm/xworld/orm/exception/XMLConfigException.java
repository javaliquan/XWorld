package com.founderdpt.comm.xworld.orm.exception;

public class XMLConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8974519969140938400L;

	public XMLConfigException() {
		super();
	}

	public XMLConfigException(String msg) {
		super(msg);
	}

	public XMLConfigException(Throwable e) {
		super(e);
	}

	public XMLConfigException(String msg, Throwable e) {
		super(msg, e);
	}
}
