package com.pete.bibliogere.modelo.excepcoes;

public class DuplicateQuestionException extends RuntimeException {
    public DuplicateQuestionException(String message) {
        super(message);
    }

    public DuplicateQuestionException(String message, Throwable cause) {
        super(message, cause);
    }
}
