package com.app.service.customer.controllers.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

import org.apache.poi.EmptyFileException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.app.service.customer.entities.EmployeeDto;
import com.app.service.customer.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ValidationException;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@MockBean
	EmployeeService employeeService;

	@Test
	public void testEmployeeDataUpload_WithInvalidFileType() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());
		mockMvc.perform(multipart("/employee/upload").file(file)).andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void testEmployeeDataUpload_WithValidRecordsAndFileType() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "Id,Title,Description,Published\n" + "1,Spring Boot Tut#1,Tut#1 Description,FALSE";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		when(employeeService.uploadEmployeesInfo(any())).thenReturn(true);
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/employee/upload").file(filePart)).andExpect(status().isOk());
	}

	@Test
	public void testEmployeeDataUpload_WithValidFileTypeAndInvalidRecords() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "Id,Title,Description,Published\n" + "1,Spring Boot Tut#1,Tut#1 Description,FALSE";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		when(employeeService.uploadEmployeesInfo(any())).thenReturn(false);
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/employee/upload").file(filePart)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testEmployeeDataUpload_IOException() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "Id,Title,Description,Published\n" + "1,Spring Boot Tut#1,Tut#1 Description,FALSE";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		doThrow(new IOException()).when(employeeService).uploadEmployeesInfo(any());
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/employee/upload").file(filePart)).andExpect(status().is5xxServerError());

	}

	@Test
	public void testEmployeeDataUpload_WithEmptyFile() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		doThrow(new EmptyFileException()).when(employeeService).uploadEmployeesInfo(any());
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/employee/upload").file(filePart)).andExpect(status().is4xxClientError());
	}

	@Test
	public void testDeactivateEmployee_WithInValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		doThrow(NoSuchElementException.class).when(employeeService).deactivateEmployee(any());
		mockMvc.perform(delete("/employee").param("empId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDeactivateEmployee_WithValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		doNothing().when(employeeService).deactivateEmployee(any());
		mockMvc.perform(delete("/employee").param("empId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetEmployeeInfo_WithValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		when(employeeService.getEmployeeInfo(any())).thenReturn(new EmployeeDto());
		mockMvc.perform(get("/employee").param("empId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetEmployeeInfo_WithInValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		doThrow(NoSuchElementException.class).when(employeeService).getEmployeeInfo(any());
		mockMvc.perform(get("/employee").param("empId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testcreateEmployee_WithInvalidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String empJson = new ObjectMapper().writeValueAsString(new EmployeeDto());
		when(employeeService.createEmployee(any())).thenThrow(new ValidationException());
		mockMvc.perform(post("/employee").accept(MediaType.APPLICATION_JSON).content(empJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is5xxServerError());
	}

	@Test
	public void testcreateEmployee_WithValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String empJson = new ObjectMapper().writeValueAsString(new EmployeeDto());
		when(employeeService.createEmployee(any())).thenReturn(new EmployeeDto());
		mockMvc.perform(post("/employee").accept(MediaType.APPLICATION_JSON).content(empJson)
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
}
