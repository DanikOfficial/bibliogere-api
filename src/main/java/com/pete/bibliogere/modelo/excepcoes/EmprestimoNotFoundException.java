package com.pete.bibliogere.modelo.excepcoes;

public class EmprestimoNotFoundException extends RuntimeException {

    public EmprestimoNotFoundException(String message) {
        super(message);
    }
}
