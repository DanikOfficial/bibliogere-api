package com.pete.bibliogere.modelo.excepcoes;

public class UtilizadorAlreadyExistsException extends RuntimeException {
    public UtilizadorAlreadyExistsException(String message) {
        super(message);
    }
}
