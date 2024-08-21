package com.app.service.customer.services.test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.entities.CustomerNameDto;
import com.app.service.customer.entities.GSTINAndPanDto;
import com.app.service.customer.enums.GSTNTypeEnum;
import com.app.service.customer.services.CustomerFieldsByNameService;
import com.app.service.customer.services.CustomerFieldsValidator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class CustomerFieldsValidatorTest {
	@Mock
	CustomerFieldsByNameService customerFieldsByNameValidator;
	@InjectMocks
	CustomerFieldsValidator customerFieldsValidator;
	CustomerDto customerDto;
	@Mock
	GSTINAndPanDto gstinAndPanDto;
	@Mock
	private Validator validator;
	@Mock
	CustomerNameDto customerNameDto;
	Set<ConstraintViolation<Object>> constraintViolations;

	@Test
	public void testValidateCustomerDto_WithValidData() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setCustomerGstIn("dummy");
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		customerDto.setGstType(GSTNTypeEnum.Registered.name());
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertTrue(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithEmptyCustGSTINAndRegGstType() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		customerDto.setGstType(GSTNTypeEnum.Registered.name());
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithCustGSTINAndRegGstType() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setCustomerGstIn("dummy");
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		customerDto.setGstType(GSTNTypeEnum.Registered.name());
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertTrue(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithEmptyPanAndUnRegGstType() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithDupCustName() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn("Dummy");
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithDupCustAlias() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn("Dummy");
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithDisAllowDupGstnAndCustGSTNVal() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn("Dummy");
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithDisAllowDupGstnAndSuppGSTNVal() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn("Dummy");
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithDisAllowDupGstnAndPanNo() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn("Dummy");
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	
	@Test
	public void testValidateCustomerDto_WithDupCustCode() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn("Dummy");
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertFalse(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}
	
	@Test
	public void testValidateCustomerDto_WithValidPanAndUnRegGstType() throws InterruptedException, ExecutionException {
		customerDto = buildCustomerDto();
		customerDto.setAllowDuplicateGSTIN(false);
		customerDto.setGstType(GSTNTypeEnum.Unregistered.name());
		customerDto.setPanNo("DUMMY");
		constraintViolations = new HashSet<>();
		when(validator.validate(any())).thenReturn(constraintViolations);
		when(customerNameDto.getCustomerAlias()).thenReturn(null);
		when(customerNameDto.getCustomerCode()).thenReturn(null);
		when(customerNameDto.getCustomerName()).thenReturn(null);
		when(gstinAndPanDto.getCustomerGstIn()).thenReturn(null);
		when(gstinAndPanDto.getPanNo()).thenReturn(null);
		when(gstinAndPanDto.getSupplyGstIn()).thenReturn(null);
		when(customerFieldsByNameValidator.fetchDiffCustomerNames(any(), any(), any()))
		.thenReturn(CompletableFuture.completedFuture(customerNameDto));
		when(customerFieldsByNameValidator.getGSTINAndPANDetails(any(), any(), any(), any()))
				.thenReturn(CompletableFuture.completedFuture(gstinAndPanDto));
		assertTrue(customerFieldsValidator.validateCustomerDto(customerDto).get().isEmpty());
	}

	private CustomerDto buildCustomerDto() {
		return new CustomerDto();
	}
}
