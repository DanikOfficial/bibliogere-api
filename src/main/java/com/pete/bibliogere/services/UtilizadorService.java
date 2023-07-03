package com.pete.bibliogere.services;

import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.modelo.dto.UtilizadorDTO;
import com.pete.bibliogere.security.model.dto.AuthRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UtilizadorService {

    Utilizador pesquisarPorUsername(String username);

    UtilizadorDTO registar(Utilizador utilizador);

    void criaAdmin(Utilizador utilizador);

    UtilizadorDTO alterar(Map<String, Object> fields, Long codigoUtilizador);

    ResponseEntity<Map<String, Object>> entrar(AuthRequest authRequest);

    ResponseEntity<Map<String, Object>> refresh(String refreshToken);

}
