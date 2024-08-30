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

import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ValidationException;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext context;
	@MockBean
	CustomerService customerService;

	@Test
	public void testCustsomerDataUpload_WithInvalidFileType() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		MockMultipartFile file = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE,
				"Hello, World!".getBytes());
		mockMvc.perform(multipart("/customer/upload").file(file)).andExpect(status().isUnprocessableEntity());
	}

	@Test
	public void testCustsomerDataUpload_WithValidFileTypeAndRecords() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "Id,Title,Description,Published\n" + "1,Spring Boot Tut#1,Tut#1 Description,FALSE";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		when(customerService.uploadCustomerInfo(any())).thenReturn(true);
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/customer/upload").file(filePart)).andExpect(status().isOk());
	}

	@Test
	public void testCustsomerDataUpload_WithValidFileTypeAndInvalidRecords() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "Id,Title,Description,Published\n" + "1,Spring Boot Tut#1,Tut#1 Description,FALSE";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		when(customerService.uploadCustomerInfo(any())).thenReturn(false);
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/customer/upload").file(filePart)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testCustomerDataUpload_IOException() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "Id,Title,Description,Published\n" + "1,Spring Boot Tut#1,Tut#1 Description,FALSE";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		doThrow(new IOException()).when(customerService).uploadCustomerInfo(any());
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/customer/upload").file(filePart)).andExpect(status().is5xxServerError());
	}

	@Test
	public void testCustomerDataUpload_WithEmptyFile() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String content = "";
		byte[] fileContent = content.getBytes(StandardCharsets.UTF_8);
		doThrow(new EmptyFileException()).when(customerService).uploadCustomerInfo(any());
		MockMultipartFile filePart = new MockMultipartFile("file", "orig.csv", "text/csv", fileContent);
		mockMvc.perform(multipart("/customer/upload").file(filePart)).andExpect(status().is4xxClientError());
	}

	@Test
	public void testCreateCustomer_WithInvalidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String customerJson = new ObjectMapper().writeValueAsString(new CustomerDto());
		when(customerService.createCustomer(any())).thenThrow(new ValidationException());
		mockMvc.perform(post("/customer").accept(MediaType.APPLICATION_JSON)
				.content(customerJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void testCreateCustomer_WithValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		String customerJson = new ObjectMapper().writeValueAsString(new CustomerDto());
		 when(customerService.createCustomer(any())).thenReturn(new CustomerDto());
		 mockMvc.perform(post("/customer").accept(MediaType.APPLICATION_JSON)
				.content(customerJson).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testDeactivateCustomer_WithValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		doNothing().when(customerService).deactivateCustomer(any());
		mockMvc.perform(delete("/customer").param("customerId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeactivateCustomer_WithInValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		doThrow(NoSuchElementException.class).when(customerService).deactivateCustomer(any());
		mockMvc.perform(delete("/customer").param("customerId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testGetCustomerInfo_WithValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		when(customerService.getCustomerInfo(any())).thenReturn(new CustomerDto());
		mockMvc.perform(get("/customer").param("customerId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
				.andExpect(status().isOk());
	}

	@Test
	public void testGetCustomerInfo_WithInValidData() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		doThrow(NoSuchElementException.class).when(customerService).getCustomerInfo(any());
		 mockMvc.perform(get("/customer").param("customerId", "165bba29-e400-41f2-a9f7-a0a9fbcbbe92"))
						.andExpect(status().isNotFound());
	}
}
