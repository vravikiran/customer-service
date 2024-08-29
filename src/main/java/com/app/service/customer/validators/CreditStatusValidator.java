package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.config.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
/**
 * checks if the creditstatus is valid or not
 */
public class CreditStatusValidator implements ConstraintValidator<IsValidCreditStatus, String> {
	
	@Autowired
	private DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value != null && dbConfig.getCreditStatuses().containsKey(value.toUpperCase())) ? true:false;
	}
}
