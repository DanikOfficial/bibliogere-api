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

    //    @Value("${jwt.accessTokenDurationTest}")
//    private Long accessTokenExpirationMsec;
//
    @Value("${jwt.accessTokenDuration}")
    private Long accessTokenExpirationMsec;

    @Value("${jwt.refreshTokenDuration}")
    private Long refreshTokenExpirationMsec;

    @Override
    public String generateAccessToken(String subject) {
        Date now = new Date();
        Long duration = now.getTime() + accessTokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return token;

    }

    @Override
    public Token generateRefreshToken(String subject) {
        Date now = new Date();
        Long duration = now.getTime() + refreshTokenExpirationMsec;
        Date expiryDate = new Date(duration);
        String token = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecret)
                .compact();
        return new Token(Token.TokenType.REFRESH, token, duration,
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    @Override
    public Long getIdFromToken(String token) {
        Long id = extractAllClaims(token).get("codigo", Long.class);
        return id;
    }


    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(tokenSecret).parse(token);
            return true;
        } catch (SignatureException ex) {
            ex.printStackTrace();
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            throw new ExpiredSessionException("A sessão expirou!");
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(tokenSecret).parseClaimsJws(jwtToken)
                .getBody();
    }

}
