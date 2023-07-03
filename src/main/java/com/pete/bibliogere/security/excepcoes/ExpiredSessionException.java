package com.pete.bibliogere.security.excepcoes;

public class ExpiredSessionException extends RuntimeException {

    public ExpiredSessionException(String message) {
        super(message);
    }
}
