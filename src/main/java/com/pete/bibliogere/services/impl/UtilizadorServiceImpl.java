package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.modelo.dto.UtilizadorDTO;
import com.pete.bibliogere.repositorios.UtilizadorRepositorio;
import com.pete.bibliogere.security.excepcoes.CredenciaisInvalidasException;
import com.pete.bibliogere.security.excepcoes.InvalidTokenException;
import com.pete.bibliogere.security.excepcoes.UtilizadorNotFoundException;
import com.pete.bibliogere.security.jwt.Token;
import com.pete.bibliogere.security.model.dto.AuthRequest;
import com.pete.bibliogere.security.model.dto.AuthResponse;
import com.pete.bibliogere.security.model.dto.RefreshResponse;
import com.pete.bibliogere.security.service.TokenProviderService;
import com.pete.bibliogere.security.util.CookieUtil;
import com.pete.bibliogere.services.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class UtilizadorServiceImpl implements UtilizadorService {

    @Autowired
    private UtilizadorRepositorio repositorio;

    @Autowired
    private TokenProviderService tokenProvider;

    @Autowired
    private CookieUtil cookieUtil;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Utilizador pesquisarPorUsername(String username) {
        return repositorio.findByUsernameIgnoreCase(username.trim()).orElseThrow(
                () -> new UtilizadorNotFoundException("O utilizador digitado não existe: " + username));
    }

    @Override
    public UtilizadorDTO registar(Utilizador utilizador) {
        return null;
    }

    @Override
    public void criaAdmin(Utilizador utilizador) {

        utilizador.setPassword(encoder.encode(utilizador.getPassword()));

        repositorio.save(utilizador);
    }

    @Override
    public UtilizadorDTO alterar(Map<String, Object> fields, Long codigoUtilizador) {
        return null;
    }


    @Override
    public ResponseEntity<Map<String, Object>> entrar(AuthRequest authRequest) {
        Utilizador utilizador = pesquisarPorUsername(authRequest.getUsername());

        try {
            Authentication authentication = new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                    authRequest.getPassword());
            authenticationManager.authenticate(authentication);
        } catch (BadCredentialsException ex) {
            throw new CredenciaisInvalidasException("Utilizador/Senha inválida!");
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        String newAccessToken = tokenProvider.generateAccessToken(utilizador.getUsername());
        Token newRefreshToken = tokenProvider.generateRefreshToken(utilizador.getUsername());

        addRefreshTokenCookie(responseHeaders, newRefreshToken);


        AuthResponse authResponse = new AuthResponse(utilizador, newAccessToken);

        String message = "Autenticado com sucesso!";

        Map<String, Object> res = new ApiResponseObject().buildLoginResponse(Boolean.FALSE, message, authResponse);


        return ResponseEntity.ok().headers(responseHeaders).body(res);
    }

//
//    @Override
//    public ResponseEntity<Map<String, Object>> entrar(AuthRequest authRequest, String accessToken,
//                                                      String refreshToken) {
//        Utilizador utilizador = pesquisarPorUsername(authRequest.getUsername());
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
//        } catch (BadCredentialsException ex) {
//            throw new CredenciaisInvalidasException("Utilizador/Senha inválida!");
//        }
//
//        Boolean accessTokenValid = tokenProvider.validateToken(accessToken);
//        Boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//
//        Token newAccessToken;
//        Token newRefreshToken;
//
//        if (!accessTokenValid && !refreshTokenValid) {
//            newAccessToken = tokenProvider.generateAccessToken(utilizador.getUsername());
//            newRefreshToken = tokenProvider.generateRefreshToken(utilizador.getUsername());
//            addAccessTokenCookie(responseHeaders, newAccessToken);
//            addRefreshTokenCookie(responseHeaders, newRefreshToken);
//        }
//
//        if (!accessTokenValid && refreshTokenValid) {
//            newAccessToken = tokenProvider.generateAccessToken(utilizador.getUsername());
//            addAccessTokenCookie(responseHeaders, newAccessToken);
//        }
//
//        if (accessTokenValid && refreshTokenValid) {
//            newAccessToken = tokenProvider.generateAccessToken(utilizador.getUsername());
//            newRefreshToken = tokenProvider.generateRefreshToken(utilizador.getUsername());
//            addAccessTokenCookie(responseHeaders, newAccessToken);
//            addRefreshTokenCookie(responseHeaders, newRefreshToken);
//        }
//
//        AuthResponse authResponse = new AuthResponse(utilizador);
//
//        String message = "Autenticado com sucesso!";
//
//        Map<String, Object> res = new ApiResponseObject().response(Boolean.FALSE, message, authResponse, 200);
//
//
//        return ResponseEntity.ok().headers(responseHeaders).body(res);
//    }

    @Override
    public ResponseEntity<Map<String, Object>> refresh( String refreshToken) {

        boolean refreshTokenValid = tokenProvider.validateToken(refreshToken);

        if (!refreshTokenValid) throw new InvalidTokenException("Refresh token is Invalid!");

        String username = tokenProvider.getUsernameFromToken(refreshToken);

        String newAccessToken = tokenProvider.generateAccessToken(username);

        HttpHeaders responseHeaders = new HttpHeaders();

        String message = "Refresh Token Gerado com sucesso!";

        RefreshResponse refreshResponse = new RefreshResponse(newAccessToken);

        Map<String, Object> res = new ApiResponseObject().buildLoginResponse(Boolean.FALSE, message, refreshResponse);

        return ResponseEntity.ok().headers(responseHeaders).body(res);
    }


    public void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }
}
