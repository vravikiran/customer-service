package com.app.service.customer.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
/**
 * Annotation to check GreetingType
 */
@Target({ ElementType.FIELD })
@Retention(value = RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GreetingTypeValidator.class)
@Documented
public @interface IsValidGreetingType {
	String message();

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
