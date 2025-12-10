package com.pete.bibliogere.modelo.excepcoes;

public class UtilizadorDisabledExcception extends RuntimeException {
    public UtilizadorDisabledExcception(String message) {
        super(message);
    }
}
