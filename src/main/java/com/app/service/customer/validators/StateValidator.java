package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.config.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * checks given state is valid
 */
public class StateValidator implements ConstraintValidator<IsValidState, String> {
	@Autowired
	DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value != null && dbConfig.getStatesMap().containsKey(value.toUpperCase())) ? true : false;
	}
}
