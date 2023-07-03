package com.pete.bibliogere.utils;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

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
