package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.config.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks given GSTNType is valid
 */
public class GstnTypeValidator implements ConstraintValidator<IsValidGstnType, String> {
	@Autowired
	DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value != null && dbConfig.getGstnTypes().containsKey(value.toUpperCase())) ? true : false;
	}
}
