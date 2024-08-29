package com.app.service.customer.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.EmptyFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.service.customer.entities.Employee;
import com.app.service.customer.entities.EmployeeDto;
import com.app.service.customer.enums.EmployeeCSVFileHeaders;
import com.app.service.customer.repositories.CustomerRepository;
import com.app.service.customer.repositories.EmployeeRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

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
	private static final String CUSTOMER_NAME = "customerName";

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

	/**
	 * updated employee based on provided employeeId
	 * 
	 * @param employeeId
	 * @param updatedFields
	 * @return
	 * @throws Exception
	 */
	public EmployeeDto updateEmployee(UUID employeeId, Map<String, String> updatedFields) throws NoSuchElementException {
		Employee employee = employeeRepository.findById(employeeId).get();
		Employee updatedEmployee = null;
		if (employee != null) {
			String customerName = updatedFields.get(CUSTOMER_NAME);
			if (customerName != null) {
				UUID customerfk = customerRepository.getParentCustomerId(customerName);
				if (customerfk !=null && !customerfk.equals(employee.getCustomerfk())) {
					employee.setCustomerfk(customerfk);
				} else {
					employee.setCustomerfk(null);
				}
			}
			updatedFields.remove(CUSTOMER_NAME);
			employee.updateValues(employee, updatedFields);
			if (validateEmployee(employee)) {
				updatedEmployee = employeeRepository.save(employee);
			}
		}
		return convertEmployeeToEmployeeDto(updatedEmployee);
	}

	/**
	 * deactivates employee if exists
	 * 
	 * @param employeeId
	 * @throws Exception if doesn't exists
	 */
	public void deactivateEmployee(UUID employeeId) throws NoSuchElementException {
		Employee employee = employeeRepository.findById(employeeId).get();
		if (employee != null) {
			employee.setIsactive(false);
			employeeRepository.save(employee);
		}
	}

	/**
	 * Creates a new employee if there are no validation errors
	 * 
	 * @param employee
	 * @return
	 */
	public EmployeeDto createEmployee(EmployeeDto employeeDto) throws ValidationException {
		Employee employee = convertEmployeeDtoToEmployeeObj(employeeDto);
		Employee createdEmp = null;
		if (validateEmployee(employee)) {
			employee.setCdate(LocalDate.now());
			createdEmp = employeeRepository.save(employee);
		}
		return convertEmployeeToEmployeeDto(createdEmp);
	}

	/**
	 * fetched employee info based on empId
	 * 
	 * @param empId
	 * @return
	 * @throws Exception if employee doesn't exists
	 */
	public EmployeeDto getEmployeeInfo(UUID empId) throws NoSuchElementException {
		Employee employee = employeeRepository.findById(empId).get();
		return convertEmployeeToEmployeeDto(employee);
	}

	/**
	 * converts employeeDto to Employee
	 * 
	 * @param employeeDto
	 * @return
	 */
	private Employee convertEmployeeDtoToEmployeeObj(EmployeeDto employeeDto) {
		Employee employee = new Employee();
		employee.setAnniversarydate(employeeDto.getAnniversarydate());
		employee.setCdate(employeeDto.getCdate());
		if (employeeDto.getCustomerName() != null) {
			employee.setCustomerfk(customerRepository.getParentCustomerId(employeeDto.getCustomerName()));
		}
		employee.setDepartment(employeeDto.getDepartment());
		employee.setDesignation(employeeDto.getDesignation());
		employee.setDob(employeeDto.getDob());
		employee.setEmail(employeeDto.getEmail());
		employee.setEmpname(employeeDto.getEmpname());
		employee.setIsactive(employeeDto.isIsactive());
		employee.setMobileno(employeeDto.getMobileno());
		employee.setPhoneno(employeeDto.getPhoneno());
		return employee;
	}
	
	private EmployeeDto convertEmployeeToEmployeeDto(Employee employee) {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setCustomeremppk(employee.getCustomeremppk());
		employeeDto.setAnniversarydate(employee.getAnniversarydate());
		employeeDto.setCdate(employee.getCdate());
		employeeDto.setCustomerName(customerRepository.findById(employee.getCustomerfk()).get().getCustomername());
		employeeDto.setDepartment(employee.getDepartment());
		employeeDto.setDesignation(employee.getDesignation());
		employeeDto.setDob(employee.getDob());
		employeeDto.setEmail(employee.getEmail());
		employeeDto.setEmpname(employee.getEmpname());
		employeeDto.setIsactive(employee.isIsactive());
		employeeDto.setMobileno(employee.getMobileno());
		employeeDto.setPhoneno(employee.getPhoneno());
		return employeeDto;
	}

	/**
	 * check for validation errors in the employee
	 * @param employee
	 * @return
	 */
	private boolean validateEmployee(Employee employee) {
		Set<ConstraintViolation<Employee>> errors = validator.validate(employee);
		boolean isValid = false;
		if (!errors.isEmpty()) {
			StringBuffer validationMsgs = new StringBuffer();
			for (ConstraintViolation<Employee> constraint : errors) {
				validationMsgs.append(constraint.getPropertyPath().toString() + "-" + constraint.getMessage());
				validationMsgs.append(";");
			}
			throw new ValidationException(validationMsgs.toString());
		} else {
			isValid = true;
		}
		return isValid;
	}
}
