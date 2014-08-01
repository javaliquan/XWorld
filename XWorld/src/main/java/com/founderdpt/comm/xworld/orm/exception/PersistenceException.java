package com.founderdpt.comm.xworld.orm.exception;

public class PersistenceException extends RuntimeException {
 

	/**
	 * 
	 */
	private static final long serialVersionUID = -8589845891154738193L;

	public PersistenceException() {
		super();
	}

	public PersistenceException(String msg) {
		super(msg);
	}

	public PersistenceException(Throwable e) {
		super(e);
	}

	public PersistenceException(String msg, Throwable e) {
		super(msg, e);
	}
}
