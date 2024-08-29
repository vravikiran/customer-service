package com.app.service.customer.services.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.service.customer.entities.CustomerNameDto;
import com.app.service.customer.entities.GSTINAndPanDto;
import com.app.service.customer.repositories.CustomerRepository;
import com.app.service.customer.services.CustomerFieldsByNameService;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CustomerFieldsByNameServiceTest {
	@Mock
	private CustomerRepository customerRepository;
	@InjectMocks
	CustomerFieldsByNameService customerFieldsByNameService;
	@Mock
	CustomerNameDto customerNameDto;
	@Mock
	GSTINAndPanDto gstinAndPanDto;

	@Test
	public void testDiffCustNames_WithValidData() throws InterruptedException, ExecutionException {
		when(customerRepository.getDiffCustomerNames(anyString(), anyString(), anyString()))
				.thenReturn(customerNameDto);
		assertNotNull(customerFieldsByNameService.fetchDiffCustomerNames(anyString(), anyString(), anyString()).get());
	}

	@Test
	public void testDiffCustNames_WithInValidData() throws InterruptedException, ExecutionException {
		when(customerRepository.getDiffCustomerNames(anyString(), anyString(), anyString())).thenReturn(null);
		assertNull(customerFieldsByNameService.fetchDiffCustomerNames(anyString(), anyString(), anyString()).get());
	}

	@Test
	public void testGetGSTINAndPANDetails_WithInValidData() throws InterruptedException, ExecutionException {
		when(customerRepository.getGSTINAndPANDetails(anyString(), anyString(), anyString())).thenReturn(null);
		assertNull(customerFieldsByNameService.getGSTINAndPANDetails(anyString(), anyString(), anyString()).get());
	}

	@Test
	public void testGetGSTINAndPANDetails_WithValidData() throws InterruptedException, ExecutionException {
		when(customerRepository.getGSTINAndPANDetails(anyString(), anyString(), anyString()))
				.thenReturn(gstinAndPanDto);
		assertNotNull(customerFieldsByNameService.getGSTINAndPANDetails(anyString(), anyString(), anyString()).get());
	}

	@Test
	public void testFetchParentCustomerId_WithValidVal() throws InterruptedException, ExecutionException {
		UUID uuid = new UUID(22, 23);
		when(customerRepository.getParentCustomerId(anyString())).thenReturn(uuid);
		assertNotNull(customerFieldsByNameService.fetchParentCustomerId(anyString()).get());
	}

	@Test
	public void testFetchParentCustomerId_WithInValidVal() throws InterruptedException, ExecutionException {
		when(customerRepository.getParentCustomerId(anyString())).thenReturn(null);
		assertNull(customerFieldsByNameService.fetchParentCustomerId(anyString()).get());
	}
}
