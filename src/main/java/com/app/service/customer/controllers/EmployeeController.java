package com.app.service.customer.controllers;

import java.io.IOException;

import org.apache.poi.EmptyFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.service.customer.exceptions.InvalidFileTypeException;
import com.app.service.customer.services.EmployeeService;

/**
 * handles customer employee related functionalities
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	Logger logger = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeService employeeService;

	/**
	 * reads the contents of uploaded csv file and saves the valid employee details
	 * generates the error log file of invalid records in given csv file
	 * 
	 * @param file
	 * @return
	 * @throws EmptyFileException
	 * @throws InvalidFileTypeException
	 * @throws IOException
	 */
	@PostMapping("/upload")
	public ResponseEntity<Object> uploadEmployeeInfo(@RequestParam("file") MultipartFile file)
			throws EmptyFileException, InvalidFileTypeException, IOException {
		logger.info("EmployeeController :: uploadEmployeeInfo - {}", "Upload of employee details started");
		boolean isValid = false;
		if (file.getContentType().equals("text/csv")) {
			isValid = employeeService.uploadEmployeesInfo(file.getInputStream());
		} else {
			logger.error("EmployeeController :: uploadEmployeeInfo - {}", "invalid file Type " + file.getContentType());
			throw new InvalidFileTypeException("invalid file type");
		}
		logger.info("EmployeeController :: uploadEmployeeInfo - {}", "Upload of employee details completed");
		if (isValid)
			return ResponseEntity.ok().build();
		logger.info("EmployeeController :: uploadEmployeeInfo - {} ", "file upload partially successful");
		return ResponseEntity.status(HttpStatusCode.valueOf(207)).body("file upload partially successful");
	}
}
