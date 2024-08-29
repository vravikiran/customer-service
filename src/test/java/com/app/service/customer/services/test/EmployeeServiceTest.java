package com.app.service.customer.services.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.apache.poi.EmptyFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.service.customer.entities.Customer;
import com.app.service.customer.entities.Employee;
import com.app.service.customer.entities.EmployeeDto;
import com.app.service.customer.repositories.CustomerRepository;
import com.app.service.customer.repositories.EmployeeRepository;
import com.app.service.customer.services.EmployeeService;
import com.app.service.customer.services.ErrorLogService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
	@Mock
	EmployeeRepository employeeRepository;
	@Mock
	CustomerRepository customerRepository;
	@Mock
	Validator validator;
	@Mock
	ErrorLogService errorLogService;
	@InjectMocks
	EmployeeService employeeService;
	@Mock
	ConstraintViolation<Employee> constraint;

	@Test
	public void testUploadEmployeesInfo_WithValidData() throws IOException {
		Set<ConstraintViolation<Employee>> empErrorRows = new HashSet<>();
		File file = new File("./src/test/resources/Employee_Valid.csv");
		InputStream inputStream = new FileInputStream(file);
		doReturn(empErrorRows).when(validator).validate(any());
		assertTrue(employeeService.uploadEmployeesInfo(inputStream));
	}

	@Test
	public void testUploadEmployeesInfo_WithInValidData() throws IOException {
		Set<ConstraintViolation<Employee>> empErrorRows = new HashSet<>();
		empErrorRows.add(constraint);
		File file = new File("./src/test/resources/Employee_Valid.csv");
		InputStream inputStream = new FileInputStream(file);
		doReturn(empErrorRows).when(validator).validate(any());
		assertFalse(employeeService.uploadEmployeesInfo(inputStream));
	}

	@Test
	public void testUploadEmployeesInfoWithEmptyFile() throws IOException {
		File file = new File("./src/test/resources/emptydata.csv");
		InputStream inputStream = new FileInputStream(file);
		assertThrows(EmptyFileException.class, () -> employeeService.uploadEmployeesInfo(inputStream));
	}

	@Test
	public void testSaveEmployees() {
		employeeService.saveEmployees(getEmpList());
		verify(employeeRepository, times(1)).saveAll(anyList());
	}

	@Test
	public void testDeactivateEmp() {
		when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
		employeeService.deactivateEmployee(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d"));
		verify(employeeRepository, times(1)).save(any());
	}

	@Test
	public void testDeactivateEmp_WithInvalidData() {
		when(employeeRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class,
				() -> employeeService.deactivateEmployee(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d")));
	}

	@Test
	public void testGetEmployee() {
		when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		assertNotNull(employeeService.getEmployeeInfo(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d")));
	}

	@Test
	public void testGetEmployee_WithInvalidData() {
		when(employeeRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class,
				() -> employeeService.getEmployeeInfo(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d")));
	}

	@Test
	public void testCreateEmployee_WithValidData() {
		Set<ConstraintViolation<Employee>> empErrorRows = new HashSet<>();
		when(validator.validate(any(Employee.class))).thenReturn(empErrorRows);
		when(employeeRepository.save(any())).thenReturn(new Employee());
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		assertNotNull(employeeService.createEmployee(getEmployeeDto()));
		verify(employeeRepository,times(1)).save(any(Employee.class));
	}

	@Test
	public void testCreateEmployee_WithInValidCustomer() {
		Set<ConstraintViolation<Employee>> empErrorRows = new HashSet<>();
		when(validator.validate(any(Employee.class))).thenReturn(empErrorRows);
		when(employeeRepository.save(any())).thenReturn(new Employee());
		when(customerRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class, () -> employeeService.createEmployee(getEmployeeDto()));
	}

	@Test
	public void testCreateEmployee_WithInValidData() {
		Set<ConstraintViolation<Employee>> empErrorRows = new HashSet<>();
		empErrorRows.add(constraint);
		Path path = new Path() {

			@Override
			public Iterator<Node> iterator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		when(constraint.getPropertyPath()).thenReturn(path);
		when(validator.validate(any(Employee.class))).thenReturn(empErrorRows);
		assertThrows(ValidationException.class, () -> employeeService.createEmployee(getEmployeeDto()));
	}

	@Test
	public void testUpdateEmployee_WithInvalidEmp() {
		Map<String, String> updatedFields = new HashMap<>();
		updatedFields.put("designation", "Team Lead");
		when(employeeRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class, () -> employeeService
				.updateEmployee(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d"), updatedFields));
	}
	
	@Test
	public void testUpdateEmployee_WithValidationErrors() {
		Map<String, String> updatedFields = new HashMap<>();
		updatedFields.put("designation", "Team Lead");
		Set<ConstraintViolation<Employee>> empErrorRows = new HashSet<>();
		empErrorRows.add(constraint);
		Path path = new Path() {

			@Override
			public Iterator<Node> iterator() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		when(constraint.getPropertyPath()).thenReturn(path);
		when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
		when(validator.validate(any(Employee.class))).thenReturn(empErrorRows);
		assertThrows(ValidationException.class, () -> employeeService
				.updateEmployee(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d"), updatedFields));
	}

	@Test
	public void testUpdateEmployee_WithValidEmp() {
		Map<String, String> updatedFields = new HashMap<>();
		updatedFields.put("designation", "Team Lead");
		when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
		when(employeeRepository.save(any())).thenReturn(new Employee());
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		assertNotNull(
				employeeService.updateEmployee(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d"), updatedFields));
	}
	
	@Test
	public void testUpdateEmployee_WithInValidCustomer() {
		Map<String, String> updatedFields = new HashMap<>();
		updatedFields.put("designation", "Team Lead");
		when(employeeRepository.findById(any())).thenReturn(Optional.of(new Employee()));
		when(employeeRepository.save(any())).thenReturn(new Employee());
		when(customerRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class,()->
				employeeService.updateEmployee(UUID.fromString("0650e7f3-8437-4c4f-9797-d55a0701361d"), updatedFields));
	}

	public List<Employee> getEmpList() {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		return employees;
	}

	public EmployeeDto getEmployeeDto() {
		EmployeeDto employeeDto = new EmployeeDto();
		employeeDto.setAnniversarydate(LocalDate.now());
		employeeDto.setCustomerName("DUMMY");
		return employeeDto;
	}

}
