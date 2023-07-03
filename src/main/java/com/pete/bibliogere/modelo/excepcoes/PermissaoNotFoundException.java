package com.pete.bibliogere.modelo.excepcoes;

public class PermissaoNotFoundException extends RuntimeException {
    public PermissaoNotFoundException(String message) {
        super(message);
    }
}
