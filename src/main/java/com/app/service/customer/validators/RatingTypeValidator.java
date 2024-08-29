package com.app.service.customer.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.service.customer.config.DBConfig;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Checks given rating type is valid
 */
public class RatingTypeValidator implements ConstraintValidator<IsValidRatingType, String> {
	@Autowired
	DBConfig dbConfig;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return (value != null && dbConfig.getRatings().containsKey(value.toUpperCase())) ? true : false;
	}

}
