package com.pete.bibliogere.controller;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.modelo.Localizacao;
import com.pete.bibliogere.services.LocalizacaoService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "/api/v1")
public class LocalizacaoRestController {

    @Autowired
    private LocalizacaoService service;

    @GetMapping(value = "/localizacoes", produces = "application/json")
    public ResponseEntity<List<Localizacao>> getLocalizacoes() {

        return ResponseEntity.ok(service.listarLocalizacoes());
    }

    @GetMapping(value = "/localizacao/{codigo}", produces = "application/json")
    public ResponseEntity<Localizacao> getLocalizacao(@PathParam("codigo") Long codigo) {
        Localizacao localizacao = service.pesquisaLocalizacaoPorCodigo(codigo);

        return ResponseEntity.ok(localizacao);
    }

}
