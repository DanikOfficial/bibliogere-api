package com.pete.bibliogere.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Token {

    private TokenType tokenType;

    public String tokenValue;

    public Long duration;

    private LocalDateTime expiryDate;

    public enum TokenType {
        ACCESS, REFRESH
    }
}
