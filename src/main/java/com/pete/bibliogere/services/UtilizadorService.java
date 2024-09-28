package com.pete.bibliogere.services;

import com.pete.bibliogere.dto.*;
import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.modelo.dto.UtilizadorDTO;
import com.pete.bibliogere.security.model.dto.AuthRequest;
import com.pete.bibliogere.security.model.dto.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UtilizadorService {

    Utilizador pesquisarPorUsername(String username);

    Utilizador pesquisaPorCodigo(Long codigo);

    UtilizadorDTO registar(Utilizador utilizador);

    UtilizadorDTO criaAdmin(Utilizador utilizador);

    AtendenteInfo createAtendente(CreateAtendenteRequest request);

    AuthResponse createPassword(CreatePasswordRequest request);

    SimpleResponse updatePassword(UpdatePasswordRequest request);

    UtilizadorDTO alterar(Map<String, Object> fields, Long codigoUtilizador);

    AuthResponse entrar(AuthRequest authRequest);

    ResponseEntity<Map<String, Object>> refresh(String refreshToken);
}
