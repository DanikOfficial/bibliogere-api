package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.dto.*;
import com.pete.bibliogere.modelo.Atendente;
import com.pete.bibliogere.modelo.Permissao;
import com.pete.bibliogere.modelo.RoleConstants;
import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.modelo.dto.UtilizadorDTO;
import com.pete.bibliogere.modelo.excepcoes.UtilizadorAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.UtilizadorDisabledExcception;
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
import com.pete.bibliogere.services.PermissaoService;
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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UtilizadorServiceImpl implements UtilizadorService {

    @Autowired
    PermissaoService permissaoService;

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
    public Utilizador pesquisaPorCodigo(Long codigo) {
        return repositorio.findByCodigo(codigo).orElseThrow(() -> new UtilizadorNotFoundException("O utilizador digitado não existe: " + codigo));
    }

    @Override
    public UtilizadorDTO registar(Utilizador utilizador) {
        return null;
    }

    @Override
    public UtilizadorDTO criaAdmin(Utilizador utilizador) {

        utilizador.setPassword(encoder.encode(utilizador.getPassword()));

        Utilizador user = repositorio.save(utilizador);

        return Optional.of(user).map(UtilizadorDTO::new).get();
    }

    @Override
    public AtendenteInfo createAtendente(CreateAtendenteRequest request) {
        boolean isPresent = repositorio.findByUsernameIgnoreCase(request.getUsername()).isPresent();

        if (isPresent) throw new UtilizadorAlreadyExistsException("Ja existe um Utilizador com este ID");

        Atendente atendente = new Atendente(request.getUsername(), encoder.encode("1234"), Boolean.TRUE, request.getNome());
        atendente.setIsFirstLogin(Boolean.TRUE);
        atendente.setEnabled(true);
        atendente.setIsDeleted(false);
        Permissao atendentePermission = permissaoService.pesquisarPermissaoPorNome(RoleConstants.ROLE_ATENDENTE);
        atendente.getPermissoes().add(atendentePermission);
        atendente = repositorio.save(atendente);

        return new AtendenteInfo(atendente);
    }

    @Override
    public AuthResponse createPassword(CreatePasswordRequest request) {
        Utilizador utilizador = pesquisarPorUsername(request.getUsername());

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CredenciaisInvalidasException("As senhas não coincidem");
        }
        utilizador.setPassword(encoder.encode(request.getNewPassword()));
        utilizador.setIsFirstLogin(Boolean.FALSE);
        utilizador = repositorio.save(utilizador);
        return entrar(AuthRequest.builder().username(utilizador.getUsername()).password(request.getNewPassword()).build());
    }

    @Override
    public SimpleResponse updatePassword(UpdatePasswordRequest request) {
        Utilizador utilizador = pesquisaPorCodigo(request.getCodigoUtilizador());

        if (!encoder.matches(request.getOldPassword(), utilizador.getPassword())) {
            throw new CredenciaisInvalidasException("Senha inválida!");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CredenciaisInvalidasException("As senhas não coincidem");
        }

        utilizador.setPassword(encoder.encode(request.getNewPassword()));
        repositorio.save(utilizador);

        return SimpleResponse.builder().message("Senha atualizada com sucesso").build();
    }

    @Override
    public UtilizadorDTO alterar(Map<String, Object> fields, Long codigoUtilizador) {
        return null;
    }

    @Override
    public AuthResponse entrar(AuthRequest authRequest) {
        Utilizador utilizador = pesquisarPorUsername(authRequest.getUsername());

        if (!utilizador.getEnabled()) throw new UtilizadorDisabledExcception("Este utilizador esta desativado. Contacte o seu Gerente!");

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

        return new AuthResponse(utilizador, newAccessToken);
    }

    @Override
    public ResponseEntity<Map<String, Object>> refresh(String refreshToken) {

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

    @Override
    public List<AtendenteInfo> getAtendentes() {
        List<Atendente> atendentes = repositorio.findAllAtendentes();
        if (atendentes == null) {
            return Collections.emptyList();
        }

        return atendentes.stream()
                .filter(Objects::nonNull) // Filter out null elements
                .filter(atendente -> Boolean.FALSE.equals(atendente.getIsDeleted())) // Safe null check
                .map(AtendenteInfo::new)
                .collect(Collectors.toList());    }

    @Override
    public AtendenteInfo deleteAtendente(Long codigo) {
        Utilizador foundAtendente = pesquisaPorCodigo(codigo);
        foundAtendente.setIsDeleted(Boolean.TRUE);
        return new AtendenteInfo(foundAtendente);
    }

    @Override
    public AtendenteInfo desativarAtendente(Long codigo) {
        Utilizador utilizador = pesquisaPorCodigo(codigo);
        utilizador.setEnabled(!utilizador.getEnabled());
        utilizador = repositorio.save(utilizador);
        return new AtendenteInfo(utilizador);
    }


    public void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE,
                cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration()).toString());
    }
}
