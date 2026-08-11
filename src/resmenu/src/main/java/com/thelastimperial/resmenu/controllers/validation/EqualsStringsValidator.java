package com.thelastimperial.resmenu.controllers.validation;

import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EqualsStringsValidator implements ConstraintValidator<EqualsStrings, Object> {
    private String field;
    private String fieldMatch;
    private String message;

    @Override
    public void initialize(EqualsStrings constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.fieldMatch = constraintAnnotation.fieldMatch();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object fieldValue = new BeanWrapperImpl(value).getPropertyValue(field);
        Object fieldMatchValue = new BeanWrapperImpl(value).getPropertyValue(fieldMatch);
        context.buildConstraintViolationWithTemplate(message)
            .addPropertyNode(fieldMatch)
            .addConstraintViolation();
        if(fieldValue == null || fieldMatchValue == null)
            return false;

        return fieldValue.equals(fieldMatchValue);
    }
}
