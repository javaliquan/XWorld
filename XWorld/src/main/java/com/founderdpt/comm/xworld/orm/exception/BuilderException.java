package com.founderdpt.comm.xworld.orm.exception;

public class BuilderException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2605832197650141869L;

	public BuilderException() {
		super();
	}

	public BuilderException(String msg) {
		super(msg);
	}

	public BuilderException(Throwable e) {
		super(e);
	}

	public BuilderException(String msg, Throwable e) {
		super(msg, e);
	}
}
