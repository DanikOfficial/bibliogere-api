package com.pete.bibliogere.security.service.impl;

import com.pete.bibliogere.security.excepcoes.ExpiredSessionException;
import com.pete.bibliogere.security.jwt.Token;
import com.pete.bibliogere.security.service.TokenProviderService;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenProviderServiceImpl implements TokenProviderService {

    @Value("${jwt.secret}")
    private String tokenSecret;

    @Value("${jwt.accessTokenDuration}")
    private Long accessTokenExpirationMsec;

    @Value("${jwt.refreshTokenDuration}")
    private Long refreshTokenExpirationMsec;

    @Override
    public String generateAccessToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationMsec);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
    }

    @Override
    public Token generateRefreshToken(String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationMsec);

        String refreshToken = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();

        return new Token(
                Token.TokenType.REFRESH,
                refreshToken,
                expiryDate.getTime(),
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault())
        );
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    @Override
    public Long getIdFromToken(String token) {
        return extractAllClaims(token).get("codigo", Long.class);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw new ExpiredSessionException("A sessão expirou!");
        } catch (SignatureException | MalformedJwtException |
                 UnsupportedJwtException | IllegalArgumentException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody();
    }
}
