package com.pete.bibliogere.modelo.excepcoes;

public class ObraNotFoundException extends RuntimeException {
    public ObraNotFoundException(String message) {
        super(message);
    }
}
