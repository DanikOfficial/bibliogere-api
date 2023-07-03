package com.pete.bibliogere.security.service;

import com.pete.bibliogere.security.jwt.Token;
import org.springframework.stereotype.Service;

@Service
public interface TokenProviderService {

    String generateAccessToken(String subject);

    Token generateRefreshToken(String subject);

    String getUsernameFromToken(String token);

    Long getIdFromToken(String token);

    boolean validateToken(String token);

}
