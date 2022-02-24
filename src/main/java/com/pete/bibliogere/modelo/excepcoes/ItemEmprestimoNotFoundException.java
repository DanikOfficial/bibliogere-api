package com.pete.bibliogere.modelo.excepcoes;

public class ItemEmprestimoNotFoundException extends RuntimeException {
    public ItemEmprestimoNotFoundException(String message) {
        super(message);
    }
}
