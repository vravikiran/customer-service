package com.app.service.customer.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.poi.EmptyFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.exceptions.EmptyDataException;
import com.app.service.customer.exceptions.InvalidFileTypeException;
import com.app.service.customer.services.CustomerService;

import jakarta.validation.ValidationException;

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
			logger.error("CustomerController:: invalid file Type - {}", file.getContentType());
			throw new InvalidFileTypeException("invalid file type");
		}
		if (isAllValid) {
			logger.info("CustomerController:: uploadCustomerInfo - {}", "file uploaded successfully");
			return ResponseEntity.ok().build();
		} else {
			logger.info("CustomerController:: uploadCustomerInfo - {} ", "file upload partially successful");
			return ResponseEntity.status(HttpStatusCode.valueOf(207)).body("file upload partially successful");
		}
	}

	/**
	 * creates customer
	 * 
	 * @param customerDto
	 * @return
	 * @throws ValidationException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@PostMapping
	public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto)
			throws ValidationException, InterruptedException, ExecutionException {
		CustomerDto createdcustomerDto = customerService.createCustomer(customerDto);
		return ResponseEntity.ok(createdcustomerDto);
	}

	/**
	 * deactivates customer
	 * 
	 * @param customerId
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping
	public ResponseEntity<String> deactivateCustomer(@RequestParam("customerId") UUID customerId)
			throws NoSuchElementException {
		customerService.deactivateCustomer(customerId);
		return ResponseEntity.ok("customer is deactivated successfully");
	}

	/**
	 * fetches customer info
	 * 
	 * @param customerId
	 * @return Customer
	 * @throws Exception
	 */
	@GetMapping
	public ResponseEntity<CustomerDto> getCustomerInfo(@RequestParam("customerId") UUID customerId)
			throws NoSuchElementException {
		CustomerDto customerDto = customerService.getCustomerInfo(customerId);
		return ResponseEntity.ok(customerDto);
	}

	/**
	 * updates customer details if provided customerId exists
	 * 
	 * @param customerId
	 * @param customerDto
	 * @return
	 * @throws Exception
	 */
	@PatchMapping
	public ResponseEntity<CustomerDto> updateCustomer(@RequestParam("customerId") UUID customerId,
			@RequestBody Map<String, String> valuesToUpdate) throws NoSuchElementException,ValidationException,EmptyDataException {
		CustomerDto customerDto = customerService.updateCustomer(customerId, valuesToUpdate);
		return ResponseEntity.ok(customerDto);
	}
}
