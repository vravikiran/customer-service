package com.app.service.customer.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.EmptyFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.service.customer.entities.Employee;
import com.app.service.customer.enums.EmployeeCSVFileHeaders;
import com.app.service.customer.repositories.CustomerRepository;
import com.app.service.customer.repositories.EmployeeRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * service to handle the employee related functionalities
 */
@Service
public class EmployeeService {
	@Autowired
	EmployeeRepository employeeRepository;
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	Validator validator;
	@Autowired
	ErrorLogService errorLogService;

	/**
	 * persists the provided employees into the database
	 * 
	 * @param employees
	 * @return
	 */
	public List<Employee> saveEmployees(List<Employee> employees) {
		return employeeRepository.saveAll(employees);
	}

	/**
	 * Reads the records from csv file and converts them into Employee objects,
	 * validates them and returns valid employees
	 * 
	 * @param in
	 * @return
	 * @throws EmptyFileException if there are no records in given csv file
	 * @throws IOException
	 */
	public boolean uploadEmployeesInfo(InputStream in) throws EmptyFileException, IOException {
		List<Employee> employees = null;
		boolean isValid = true;
		Map<Integer, Set<ConstraintViolation<Employee>>> empErrorRows = new HashMap<>();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		CSVParser csvParser = CSVParser.parse(bufferedReader,
				CSVFormat.DEFAULT.withFirstRecordAsHeader().withDelimiter(','));
		List<CSVRecord> csvRecords = csvParser.getRecords();
		List<Employee> validEmployees = null;
		if (csvRecords.isEmpty()) {
			throw new EmptyFileException();
		} else {
			employees = csvRecords.parallelStream().map(employee -> convertCSVRecordToEmployee(employee))
					.collect(Collectors.toList());
			validEmployees = new ArrayList<>();
			for (Employee employee : employees) {
				Set<ConstraintViolation<Employee>> errors = validator.validate(employee);
				if (errors.isEmpty()) {
					validEmployees.add(employee);
				} else {
					empErrorRows.put(employee.getSlNo(), errors);
				}
			}
			if (!empErrorRows.isEmpty()) {
				isValid = false;
				errorLogService.generateEmployeeErrorLogFile(empErrorRows);
			}
			saveEmployees(validEmployees);
		}
		return isValid;
	}

	/**
	 * converts the Employee csvRecord to Employee object fetches the customer UUID
	 * and updates it
	 * 
	 * @param csvRecord
	 * @return
	 */
	private Employee convertCSVRecordToEmployee(CSVRecord csvRecord) {
		Employee employee = new Employee();
		employee.setSlNo(Integer.valueOf(csvRecord.get(EmployeeCSVFileHeaders.SlNo)));
		String customerName = csvRecord.get(EmployeeCSVFileHeaders.CustomerName);
		employee.setCustomerfk(customerRepository.getParentCustomerId(customerName));
		employee.setDesignation(csvRecord.get(EmployeeCSVFileHeaders.Designation));
		employee.setDepartment(csvRecord.get(EmployeeCSVFileHeaders.Department));
		employee.setEmpname(csvRecord.get(EmployeeCSVFileHeaders.EmpName));
		employee.setPhoneno(Long.parseLong(csvRecord.get(EmployeeCSVFileHeaders.Phone)));
		employee.setMobileno(Long.parseLong(csvRecord.get(EmployeeCSVFileHeaders.Mobile)));
		employee.setEmail(csvRecord.get(EmployeeCSVFileHeaders.Email));
		DateTimeFormatter da = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		employee.setDob(LocalDate.parse(csvRecord.get(EmployeeCSVFileHeaders.DoB), da));
		employee.setAnniversarydate(LocalDate.parse(csvRecord.get(EmployeeCSVFileHeaders.AnniversaryDate), da));
		employee.setCdate(LocalDate.now());
		boolean isActive = Integer.valueOf(csvRecord.get(EmployeeCSVFileHeaders.IsActive)) == 0 ? false : true;
		employee.setIsactive(isActive);
		return employee;
	}
}
