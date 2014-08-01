package com.founderdpt.comm.xworld.orm.exception;

public class TransactionException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7600991101291846827L;

	public TransactionException() {
		super();
	}

	public TransactionException(String msg) {
		super(msg);
	}

	public TransactionException(Throwable e) {
		super(e);
	}

	public TransactionException(String msg, Throwable e) {
		super(msg, e);
	}
}
