package com.pete.bibliogere.modelo.excepcoes;

public class ItemEmprestimoAlreadyExistsException extends RuntimeException {
    public ItemEmprestimoAlreadyExistsException(String message) {
        super(message);
    }
}
