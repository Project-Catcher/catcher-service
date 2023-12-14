package com.catcher.common.utils.customValid.validator;

import com.catcher.common.utils.customValid.valid.ValidEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum> {
    private ValidEnum annotation;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {
        boolean result = false;

        boolean nullable = this.annotation.nullable();
        if (nullable) {
            return true;
        }

        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if (value == enumValue) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
