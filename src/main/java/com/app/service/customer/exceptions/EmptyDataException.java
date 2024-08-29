package com.app.service.customer.exceptions;

public class EmptyDataException extends Exception{

	private static final long serialVersionUID = 1L;

	public EmptyDataException() {
		super();
	}

	public EmptyDataException(String message) {
		super(message);
	}


}
