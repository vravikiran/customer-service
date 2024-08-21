package com.app.service.customer.services;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.app.service.customer.entities.CustomerNameDto;
import com.app.service.customer.entities.GSTINAndPanDto;
import com.app.service.customer.repositories.CustomerRepository;

/**
 * This class contains methods to fetch columns based on customer name
 */
@Service
@Async("validateCustFieldsByNamesExecutor")
public class CustomerFieldsByNameService {
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * Fetches customer name, customer code and customer alias columns from customer table
	 * @param customerName
	 * @param customerAlias
	 * @param customerCode
	 * @return
	 */
	public CompletableFuture<CustomerNameDto> fetchDiffCustomerNames(String customerName,String customerAlias, String customerCode) {
		return CompletableFuture.completedFuture(customerRepository.getDiffCustomerNames(customerName, customerAlias, customerCode));
	}
	
	/**
	 * Fetches customerGstIn,supplyGstIn,panNo columns from customer table based on customer name
	 * @param customerName
	 * @param customerGstIn
	 * @param supplyGstIn
	 * @param panNo
	 * @return
	 */
	public CompletableFuture<GSTINAndPanDto> getGSTINAndPANDetails(String customerName,String customerGstIn,String supplyGstIn, String panNo) {
		return CompletableFuture.completedFuture(customerRepository.getGSTINAndPANDetails(customerGstIn, supplyGstIn, panNo, customerName));
	}
	
	/**
	 * Method to fetch parent customer id for given customer name
	 * @param customerName
	 * @return
	 */
	public CompletableFuture<UUID> fetchParentCustomerId(String customerName) {
		return CompletableFuture.completedFuture(customerRepository.getParentCustomerId(customerName));
	}

}
