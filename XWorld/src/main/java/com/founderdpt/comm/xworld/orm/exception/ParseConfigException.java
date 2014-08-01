package com.founderdpt.comm.xworld.orm.exception;

public class ParseConfigException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2605832197650141869L;

	public ParseConfigException() {
		super();
	}

	public ParseConfigException(String msg) {
		super(msg);
	}

	public ParseConfigException(Throwable e) {
		super(e);
	}

	public ParseConfigException(String msg, Throwable e) {
		super(msg, e);
	}
}
