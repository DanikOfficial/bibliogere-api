package com.pete.bibliogere.modelo.excepcoes;

public class EmprestimoAlreadyExistsException extends RuntimeException {
    public EmprestimoAlreadyExistsException(String message) {
        super(message);
    }
}
