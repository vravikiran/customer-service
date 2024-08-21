package com.app.service.customer.entities;

/**
 * Entity for only name related fields,customerName,customerAlias,customerCode fields
 */
public interface CustomerNameDto {
	String getCustomerName();
	String getCustomerAlias();
	String getCustomerCode();
}
