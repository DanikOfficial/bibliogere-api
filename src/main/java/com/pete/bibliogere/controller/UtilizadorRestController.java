package com.pete.bibliogere.controller;

import com.pete.bibliogere.security.model.dto.AuthRequest;
import com.pete.bibliogere.services.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UtilizadorRestController {

    @Autowired
    private UtilizadorService utilizadorService;

    @PostMapping(value = "/utilizador/entrar", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, Object>> entrar(@Valid @RequestBody AuthRequest authRequest) {

        return utilizadorService.entrar(authRequest);
    }

    @PostMapping(value = "/utilizador/refresh", produces = "application/json")
    public ResponseEntity<Map<String, Object>> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {

        return utilizadorService.refresh(refreshToken);
    }

}
