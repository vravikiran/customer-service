package com.app.service.customer.services.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.EmptyFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.service.customer.entities.Employee;
import com.app.service.customer.repositories.CustomerRepository;
import com.app.service.customer.repositories.EmployeeRepository;
import com.app.service.customer.services.EmployeeService;
import com.app.service.customer.services.ErrorLogService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.util.List;
import java.util.ArrayList;

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
	@Spy
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

	public List<Employee> getEmpList() {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		return employees;
	}

}
