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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.poi.EmptyFileException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.service.customer.config.DBConfig;
import com.app.service.customer.entities.Customer;
import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.enums.CustomerCSVFileHeaders;
import com.app.service.customer.exceptions.EmptyDataException;
import com.app.service.customer.repositories.CustomerRepository;
import com.app.service.customer.services.CustomerFieldsValidator;
import com.app.service.customer.services.CustomerService;
import com.app.service.customer.services.ErrorLogService;

import jakarta.validation.ValidationException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
	@Mock
	CustomerRepository customerRepository;
	@Spy
	@InjectMocks
	CustomerService customerService;
	@Mock
	CustomerFieldsValidator customerFieldsValidator;
	@Mock
	Customer customer;
	@Mock
	CustomerDto customerDto;
	@Mock
	ErrorLogService errorLogService;
	@Mock
	DBConfig dbConfig;

	@Test
	public void testvalidateListOfCustomers_WithValidData() throws InterruptedException, ExecutionException {
		Map<String, String> errorsList = new HashMap<>();
		doReturn(customer).when(customerService).convertcustomerDtoToObj(any());
		when(customerFieldsValidator.validateCustomerDto(any()))
				.thenReturn(CompletableFuture.completedFuture(errorsList));
		assertTrue(customerService.validateListOfCustomers(getCustomerDtos()));
	}

	@Test
	public void testvalidateListOfCustomers_WithInValidData() throws InterruptedException, ExecutionException {
		Map<String, String> errors = new HashMap<>();
		errors.put(CustomerCSVFileHeaders.Duplicate.name(), "duplicate customer record");
		when(customerFieldsValidator.validateCustomerDto(any())).thenReturn(CompletableFuture.completedFuture(errors));
		assertFalse(customerService.validateListOfCustomers(getCustomerDtos()));
	}

	@Test
	public void testSaveCustomerDtos() {
		doReturn(customer).when(customerService).convertcustomerDtoToObj(any());
		customerService.saveCustomerDtos(getCustomerDtos().stream().collect(Collectors.toSet()));
		verify(customerRepository, times(2)).saveAll(anyList());
	}

	@Test
	public void testUploadCustomerInfo_WithEmptyFile() throws IOException {
		File file = new File("./src/test/resources/emptydata.csv");
		InputStream inputStream = new FileInputStream(file);
		assertThrows(EmptyFileException.class, () -> customerService.uploadCustomerInfo(inputStream));
	}

	@Test
	public void testUploadCustomerInfo_WithValidFile() throws IOException {
		File file = new File("./src/test/resources/data.csv");
		InputStream inputStream = new FileInputStream(file);
		Map<String, String> errors = new HashMap<>();
		doReturn(customer).when(customerService).convertcustomerDtoToObj(any());
		when(customerFieldsValidator.validateCustomerDto(any())).thenReturn(CompletableFuture.completedFuture(errors));
		assertTrue(customerService.uploadCustomerInfo(inputStream));
	}

	@Test
	public void testUploadCustomerInfo_WithErrorsInFile() throws IOException {
		File file = new File("./src/test/resources/data.csv");
		InputStream inputStream = new FileInputStream(file);
		Map<String, String> errors = new HashMap<>();
		errors.put(CustomerCSVFileHeaders.Duplicate.name(), "duplicate customer record");
		when(customerFieldsValidator.validateCustomerDto(any())).thenReturn(CompletableFuture.completedFuture(errors));
		assertFalse(customerService.uploadCustomerInfo(inputStream));
	}

	@Test
	public void testCreateCustomer_WithValidData() throws Exception {
		Map<String, String> errors = new HashMap<>();
		when(customerFieldsValidator.validateCustomerDto(any())).thenReturn(CompletableFuture.completedFuture(errors));
		doReturn(new Customer()).when(customerService).convertcustomerDtoToObj(any());
		when(customerRepository.save(any(Customer.class))).thenReturn(new Customer());
		assertNotNull(customerService.createCustomer(new CustomerDto()));
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	@Test
	public void testCreateCustomer_WithInValidData() throws Exception {
		Map<String, String> errors = new HashMap<>();
		errors.put("customerType", "Invalid Customer Type");
		when(customerFieldsValidator.validateCustomerDto(any())).thenReturn(CompletableFuture.completedFuture(errors));
		assertThrows(ValidationException.class, () -> customerService.createCustomer(new CustomerDto()));
	}

	@Test
	public void testGetCustomer_WithInvalidData() {
		when(customerRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class,
				() -> customerService.getCustomerInfo(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb")));
	}

	@Test
	public void testGetCustomer_WithValidData() {
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		assertNotNull(customerService.getCustomerInfo(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb")));
	}

	@Test
	public void testDeactivateCustomer_WithInValidData() {
		when(customerRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class,
				() -> customerService.deactivateCustomer(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb")));
	}

	@Test
	public void testDeactivateCustomer_WithValidData() {
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		customerService.deactivateCustomer(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb"));
		verify(customerRepository, times(1)).save(any());
	}

	@Test
	public void testUpdateCustomer_WithInValidCustomerId() {
		when(customerRepository.findById(any())).thenThrow(NoSuchElementException.class);
		assertThrows(NoSuchElementException.class, () -> customerService
				.updateCustomer(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb"), new HashMap<>()));
	}

	@Test
	public void testUpdateCustomer_WithValidCustomerIdAnd_NoData() {
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		assertThrows(EmptyDataException.class, () -> customerService
				.updateCustomer(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb"), new HashMap<>()));
	}

	@Test
	public void testUpdateCustomer_WithValidCustomerIdAndNullData() {
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customer()));
		assertThrows(EmptyDataException.class,
				() -> customerService.updateCustomer(UUID.fromString("d10a7cb3-372f-498c-923b-107b54b60bcb"), null));
	}

	private List<CustomerDto> getCustomerDtos() {
		List<CustomerDto> customerDtos = new ArrayList<>();
		CustomerDto customerDto = new CustomerDto();
		customerDto.setSlNo(1);
		customerDto.setCustomerName("dummyName");
		customerDto.setCustomerAlias("dummyAlias");
		customerDto.setCustomerCode("dummyCode");
		customerDto.setSupplyGstIn("dummyGSTN");
		customerDto.setCustomerGstIn("dummyCustGSTN");
		customerDto.setCustomerType("dummy");
		CustomerDto customerDto1 = new CustomerDto();
		customerDto1.setSlNo(1);
		customerDto1.setCustomerName("dummyName1");
		customerDto1.setCustomerAlias("dummyAlias1");
		customerDto1.setCustomerCode("dummyCode1");
		customerDto1.setSupplyGstIn("dummyGSTN1");
		customerDto1.setCustomerGstIn("dummyCustGSTN1");
		customerDto1.setCustomerType("dummy1");
		customerDtos.add(customerDto);
		customerDtos.add(customerDto1);
		return customerDtos;
	}

	private CustomerDto getCustomerDto() {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setSlNo(1);
		customerDto.setCustomerName("dummyName");
		customerDto.setCustomerAlias("dummyAlias");
		customerDto.setCustomerCode("dummyCode");
		customerDto.setSupplyGstIn("dummyGSTN");
		customerDto.setCustomerGstIn("dummyCustGSTN");
		customerDto.setCustomerType("dummy");
		return customerDto;
	}
}
