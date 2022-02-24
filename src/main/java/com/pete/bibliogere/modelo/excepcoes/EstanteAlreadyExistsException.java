package com.pete.bibliogere.modelo.excepcoes;

public class EstanteAlreadyExistsException extends RuntimeException {

    public EstanteAlreadyExistsException(String message) {
        super(message);
    }

}
