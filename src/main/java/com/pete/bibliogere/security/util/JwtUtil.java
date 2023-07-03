package com.pete.bibliogere.security.util;

import org.springframework.stereotype.Service;

@Service
public class JwtUtil {
/*
    @Autowired
    private UtilizadorService service;

    @Autowired
    private TokenData token;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractId(String token) {
        final Long id = extractAllClaims(token).get("id", Long.class);
        return id;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(token.getTokenSecret().getBytes())).parseClaimsJws(jwtToken)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        Utilizador utilizador = service.pesquisaUtilizadorPorUsername(username);

        claims.put("id", utilizador.getCodigo());
        claims.put("role", utilizador.getPermissoes());

        return createToken(claims, username, token.getAccessTokenDuration(), token.getTokenSecret());
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, username, token.getRefreshTokenDuration(),
                token.getTokenSecret());
    }

    private String createToken(Map<String, Object> claims, String subject, Long duration, String secret) {

        Date issuedTime = new Date(System.currentTimeMillis());

        Date expirationDate = new Date(System.currentTimeMillis() + duration);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedTime)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder()
                        .encode(secret.getBytes()))
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {

        if (isTokenExpired(token))
            throw new JwtTokenExpiredException("O token expirou!");

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()));

    }

    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(token.getTokenSecret()).parse(jwtToken);
            return true;
        } catch (SignatureException ex) {
            ex.printStackTrace();
        } catch (MalformedJwtException ex) {
            ex.printStackTrace();
        } catch (ExpiredJwtException ex) {
            ex.printStackTrace();
        } catch (UnsupportedJwtException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }*/
}

