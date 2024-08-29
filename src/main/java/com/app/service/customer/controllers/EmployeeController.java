package com.app.service.customer.controllers;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

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

import com.app.service.customer.entities.EmployeeDto;
import com.app.service.customer.exceptions.InvalidFileTypeException;
import com.app.service.customer.services.EmployeeService;

import jakarta.validation.ValidationException;

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
	/**
	 * updates employee details based on empId
	 * @param employeeId
	 * @param updatedFields
	 * @return
	 * @throws Exception if employee with given Id doesn't exists
	 */
	@PatchMapping
	public ResponseEntity<EmployeeDto> updateEmployee(@RequestParam("empId") UUID employeeId,@RequestBody Map<String,String> updatedFields) throws Exception {
		EmployeeDto employeeDto = 
		employeeService.updateEmployee(employeeId, updatedFields);
		return ResponseEntity.ok(employeeDto);
	}
	
	/**
	 * creates new employee
	 * @param employee
	 * @return
	 * @throws Exception if there are any validation errors
	 */
	@PostMapping
	public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employeeDto) throws ValidationException {
		EmployeeDto createdEmployeeDto = employeeService.createEmployee(employeeDto);
		return ResponseEntity.ok(createdEmployeeDto);
	}
	/**
	 * deactivates employee based on empId
	 * @param empId
	 * @return
	 * @throws Exception if employee doesn't exists
	 */
	@DeleteMapping
	public ResponseEntity<Object> deactivateEmployee(@RequestParam("empId") UUID empId) throws NoSuchElementException {
		employeeService.deactivateEmployee(empId);
		return ResponseEntity.ok("Employee deactivated successfully");
	}
	
	/**
	 * gets details of an employee
	 * @param empId
	 * @return
	 * @throws Exception if employee doesn't exists
	 */
	@GetMapping
	public ResponseEntity<EmployeeDto> getEmployeeInfo(@RequestParam("empId") UUID empId) throws NoSuchElementException  {
		EmployeeDto employeeDto = employeeService.getEmployeeInfo(empId);
		return ResponseEntity.ok(employeeDto);
	}
}
