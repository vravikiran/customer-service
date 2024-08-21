package com.app.service.customer.controllers;

import java.io.IOException;

import org.slf4j.Logger;
import org.apache.poi.EmptyFileException;
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
import com.app.service.customer.services.CustomerService;

/**
 * API to handle customer csv file uploads
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
	Logger logger = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	CustomerService customerService;

	/**
	 * processes uploaded csv files and saves the customer data
	 * 
	 * @param file
	 * @return
	 * @throws InvalidFileTypeException
	 * @throws IOException
	 * @throws EmptyFileException
	 */
	@PostMapping("/upload")
	public ResponseEntity<Object> uploadCustomerInfo(@RequestParam("file") MultipartFile file)
			throws EmptyFileException, InvalidFileTypeException, IOException {
		logger.info("CustomerController::uploadCustomerInfo");
		boolean isAllValid = false;
		if (file.getContentType().equals("text/csv")) {
			isAllValid = customerService.uploadCustomerInfo(file.getInputStream());
		} else {
			logger.error("CustomerController:: invalid file Type - {}",file.getContentType());
			throw new InvalidFileTypeException("invalid file type");
		}
		if (isAllValid) {
			logger.info("CustomerController:: uploadCustomerInfo - {}","file uploaded successfully");
			return ResponseEntity.ok().build();
		}
		else {
			logger.info("CustomerController:: uploadCustomerInfo - {} ","file upload partially successful");
			return ResponseEntity.status(HttpStatusCode.valueOf(207)).body("file upload partially successful");
		}
	}
}
