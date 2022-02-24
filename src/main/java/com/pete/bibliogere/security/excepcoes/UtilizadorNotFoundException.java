package com.pete.bibliogere.security.excepcoes;

public class UtilizadorNotFoundException extends RuntimeException {
    public UtilizadorNotFoundException(String message) {
        super(message);
    }
}
