package com.app.service.customer.exceptions;

import java.io.IOException;
import java.util.NoSuchElementException;

import org.apache.poi.EmptyFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * handles exceptions across the service
 */

import jakarta.validation.ValidationException;

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

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(exception.getMessage());
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<String> handleValidationException(ValidationException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception.getMessage());
	}

	@ExceptionHandler(EmptyDataException.class)
	public ResponseEntity<String> handleEmptyDataException(EmptyDataException dataException) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(dataException.getMessage());
	}
}
