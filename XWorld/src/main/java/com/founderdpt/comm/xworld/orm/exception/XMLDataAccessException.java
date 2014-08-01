package com.founderdpt.comm.xworld.orm.exception;

public class XMLDataAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7600991101291846827L;

	public XMLDataAccessException() {
		super();
	}

	public XMLDataAccessException(String msg) {
		super(msg);
	}

	public XMLDataAccessException(Throwable e) {
		super(e);
	}

	public XMLDataAccessException(String msg, Throwable e) {
		super(msg, e);
	}
}
