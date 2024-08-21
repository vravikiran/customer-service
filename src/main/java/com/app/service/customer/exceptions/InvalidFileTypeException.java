package com.app.service.customer.exceptions;
/**
 * Exception to throw if an invalid file type is uploaded
 */
public class InvalidFileTypeException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidFileTypeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InvalidFileTypeException(String message) {
		super(message);
	}

}
