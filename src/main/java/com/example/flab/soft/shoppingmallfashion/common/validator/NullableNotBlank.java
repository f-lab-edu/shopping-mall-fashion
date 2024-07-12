package com.example.flab.soft.shoppingmallfashion.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NullableNotBlankValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullableNotBlank {
    String message() default "Field must not be blank";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}