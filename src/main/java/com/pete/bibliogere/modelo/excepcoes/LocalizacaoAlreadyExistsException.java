package com.pete.bibliogere.modelo.excepcoes;

public class LocalizacaoAlreadyExistsException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1902253183231972451L;

    public LocalizacaoAlreadyExistsException(String message) {
        super(message);
    }

}
