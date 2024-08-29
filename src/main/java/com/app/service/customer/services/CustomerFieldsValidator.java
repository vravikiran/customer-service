package com.app.service.customer.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.service.customer.entities.CustomerDto;
import com.app.service.customer.entities.CustomerNameDto;
import com.app.service.customer.entities.GSTINAndPanDto;
import com.app.service.customer.enums.CustomerCSVFileHeaders;
import com.app.service.customer.enums.GSTNTypeEnum;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates the customer record and returns errors associated with it
 */
@Service
public class CustomerFieldsValidator {
	Logger logger = LoggerFactory.getLogger(CustomerFieldsValidator.class);
	@Autowired
	private Validator validator;

	@Autowired
	CustomerFieldsByNameService customerFieldsByNameValidator;

	/**
	 * validates the customer fields
	 * 
	 * @param customerDto
	 * @return
	 */
	@Async("taskExecutor")
	public CompletableFuture<Map<String, String>> validateCustomerDto(CustomerDto customerDto) {
		Map<String, String> errors = new HashMap<String, String>();
		Set<ConstraintViolation<CustomerDto>> validationErrors = validator.validate(customerDto);
		for (ConstraintViolation<CustomerDto> c : validationErrors) {
			errors.put(c.getPropertyPath().toString(), c.getMessage());
		}
		CompletableFuture<CustomerNameDto> diffCustNames = customerFieldsByNameValidator.fetchDiffCustomerNames(
				customerDto.getCustomerName(), customerDto.getCustomerAlias(), customerDto.getCustomerCode());
		try {
			CustomerNameDto custNameDto = diffCustNames.get();
			if (custNameDto.getCustomerAlias() != null) {
				errors.put(CustomerCSVFileHeaders.customerAlias.name().toUpperCase(), "Customer Alias already exists");
			}
			if (custNameDto.getCustomerCode() != null) {
				errors.put(CustomerCSVFileHeaders.customerCode.name().toUpperCase(), "Customer Code already exists");
			}
			if (custNameDto.getCustomerName() != null) {
				errors.put(CustomerCSVFileHeaders.customerName.name().toUpperCase(), "Customer Name already exists");
			}
			if (customerDto.getGstType().equalsIgnoreCase(GSTNTypeEnum.Registered.name())
					&& customerDto.getCustomerGstIn() == null) {
				errors.put(CustomerCSVFileHeaders.customerGstIn.name().toUpperCase(),
						"CustomerGSTIN is not available for " + GSTNTypeEnum.Registered.name()
								+ customerDto.getCustomerName());
			}
			if (customerDto.getGstType().equalsIgnoreCase(GSTNTypeEnum.Unregistered.name())
					&& customerDto.getPanno() == null) {
				errors.put(CustomerCSVFileHeaders.panno.name().toUpperCase(), "PAN number is not available for "
						+ GSTNTypeEnum.Unregistered.name() + customerDto.getCustomerName());
			}

		} catch (InterruptedException | ExecutionException e) {
			logger.error(
					"CustomerFieldsValidator :: validateCustomerDto, exception occurred while validating  uniqueness of provided customer name fields in the row of uploaded csv file - {}",
					customerDto.getSlNo() + " " + e.getMessage());
		}

		if (!customerDto.isAllowDuplicateGSTIN()) {
			try {
				GSTINAndPanDto gstinAndPanDto = customerFieldsByNameValidator.getGSTINAndPANDetails(
						customerDto.getCustomerGstIn(), customerDto.getSupplyGstIn(), customerDto.getPanno()).get();
				if (gstinAndPanDto.getCustomerGstIn() != null) {
					errors.put(CustomerCSVFileHeaders.customerGstIn.name().toUpperCase(),
							"Customer GSTIN already exists");
				}
				if (gstinAndPanDto.getSupplyGstIn() != null) {
					errors.put(CustomerCSVFileHeaders.supplyGstIn.name().toLowerCase(), "SupplyGSTIN already exists");
				}
				if (gstinAndPanDto.getPanNo() != null) {
					errors.put(CustomerCSVFileHeaders.panno.name().toUpperCase(), "PanNo already exists ");
				}
			} catch (InterruptedException | ExecutionException e) {
				logger.error(
						"CustomerFieldsValidator :: validateCustomerDto, exception occurred while fetching values if duplicate GSTN not allowed in the row of uploaded csv file - {}",
						customerDto.getSlNo() + " " + e.getMessage());
			}
		}
		return CompletableFuture.completedFuture(errors);
	}
}
