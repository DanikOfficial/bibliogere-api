package com.pete.bibliogere.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ValidatorUtil {

    private static final ValidatorFactory factory;

    static {
        factory = Validation.buildDefaultValidatorFactory();
    }

    public static Validator getValidator() {
        return factory.getValidator();
    }

    public static void close() {
        factory.close();
    }

}
