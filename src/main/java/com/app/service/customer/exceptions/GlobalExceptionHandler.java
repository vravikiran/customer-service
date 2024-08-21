package com.app.service.customer.exceptions;

import java.io.IOException;

import org.apache.poi.EmptyFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * handles exceptions across the service
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(InvalidFileTypeException.class)
	public ResponseEntity<String> handleInvalidFileType(InvalidFileTypeException exception) {
		return ResponseEntity.unprocessableEntity().body(exception.getMessage());
	}

	@ExceptionHandler(IOException.class)
	public ResponseEntity<String> handleIOException() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
	}
	
	@ExceptionHandler(EmptyFileException.class)
	public ResponseEntity<String> handleEmptyFileException() {
		return ResponseEntity.badRequest().body("Empty file");
	}
}
