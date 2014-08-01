package com.founderdpt.comm.xworld.orm.exception;

public class GeneratorException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2605832197650141869L;

	public GeneratorException() {
		super();
	}

	public GeneratorException(String msg) {
		super(msg);
	}

	public GeneratorException(Throwable e) {
		super(e);
	}

	public GeneratorException(String msg, Throwable e) {
		super(msg, e);
	}
}
