package com.app.service.customer.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.service.customer.entities.Customer;
import com.app.service.customer.entities.CustomerNameDto;
import com.app.service.customer.entities.GSTINAndPanDto;

/**
 * Repository for Customer
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
	@Query(value = "select customername from Customer where upper(customername)= upper(:customerName)")
	public String findCustomerName(@Param("customerName") String customerName);

	@Query(value = "select customeralias from Customer where upper(customeralias)= upper(:customerAlias)")
	public String findCustomerAlias(@Param("customerAlias") String customerAlias);

	@Query(value = "select customercode from Customer where upper(customercode)= upper(:customerCode)")
	public String findCustomerCode(@Param("customerCode") String customerCode);

	@Query(value = "select panno from Customer where upper(panno)= upper(:panNo)")
	public List<String> findPanNo(@Param("panNo") String panNo);

	@Query(value = "select customergstin from Customer where upper(customergstin)= upper(:customerGSTIN)")
	public List<String> findCustomerGSTIN(@Param("customerGSTIN") String customerGSTIN);

	@Query(value = "select supplygstin from Customer where upper(supplygstin)= upper(:supplyGSTIN)")
	public List<String> findSupplyGSTIN(@Param("supplyGSTIN") String supplyGSTIN);

	/*
	 * methods to retrieve customerGSTIN,supplierGSTIN,PanNo based on customer name
	 * 
	 * @Query(value =
	 * "select panno from Customer where panno= :panNo and customername= :customerName"
	 * ) public String fetchPanNoByCustomerName(@Param("panNo") String
	 * panNo, @Param("customerName") String customerName);
	 * 
	 * @Query(value =
	 * "select customergstin from Customer where customergstin= :customerGSTIN and customername= :customerName"
	 * ) public String fetchCustomerGSTINByCustomerName(@Param("customerGSTIN")
	 * String customerGSTIN,
	 * 
	 * @Param("customerName") String customerName);
	 * 
	 * @Query(value =
	 * "select supplygstin from Customer where supplygstin= :supplyGSTIN and customername= :customerName"
	 * ) public String fetchSupplyGSTINByCustomerName(@Param("supplyGSTIN") String
	 * supplyGSTIN,
	 * 
	 * @Param("customerName") String customerName);
	 */

	@Query(nativeQuery = true, value = "select (select customername from customer where lower(customername)= lower(:customerName)) as customerName,"
			+ "(select customercode from customer where lower(customercode)= lower(:customerCode)) as customerCode,"
			+ "(select customeralias from customer where lower(customeralias) = lower(:customerAlias)) as customerAlias")
	public CustomerNameDto getDiffCustomerNames(@Param("customerName") String customerName,
			@Param("customerAlias") String customerAlias, @Param("customerCode") String customerCode);

	@Query(nativeQuery = true, value = "select (select customergstin from customer where upper(customergstin)= upper(:customerGstIn)) as customerGstIn,"
			+ "(select supplygstin from customer where upper(supplygstin)= upper(:supplyGstIn)) as supplyGstIn,"
			+ "(select panno from customer where upper(panno)= upper(:panNo))")
	public GSTINAndPanDto getGSTINAndPANDetails(@Param("customerGstIn") String customerGstIn,
			@Param("supplyGstIn") String supplyGstIn, @Param("panNo") String panNo);

	/*
	 * fetches customerGSTIN,supplierGSTIN,PANNo based on customer Name
	 * 
	 * @Query(nativeQuery = true, value =
	 * "select (select customergstin from customer where upper(customergstin)= upper(:customerGstIn) and upper(customername)= upper(:customerName)) as customerGstIn,"
	 * +
	 * "(select supplygstin from customer where upper(supplygstin)= upper(:supplyGstIn) and upper(customername)= upper(:customerName)) as supplyGstIn,"
	 * +
	 * "(select panno from customer where upper(panno)= upper(:panNo) and upper(customername)= upper(:customerName))"
	 * ) public GSTINAndPanDto
	 * getGSTINAndPANDetailsByCustomerName(@Param("customerGstIn") String
	 * customerGstIn,
	 * 
	 * @Param("supplyGstIn") String supplyGstIn, @Param("panNo") String panNo,
	 * 
	 * @Param("customerName") String customerName);
	 */

	@Query(value = "select customerpk from Customer where upper(customername)= upper(:customerName)")
	public UUID getParentCustomerId(@Param("customerName") String customerName);
}
