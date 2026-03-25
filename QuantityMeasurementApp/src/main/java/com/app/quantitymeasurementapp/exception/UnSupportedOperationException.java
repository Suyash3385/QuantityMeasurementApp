package com.app.quantitymeasurementapp.exception;

public class UnSupportedOperationException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnSupportedOperationException(String msg) {
		super(msg);
	}

}
