package com.pete.bibliogere.controller;

import com.pete.bibliogere.modelo.TipoEstante;
import com.pete.bibliogere.services.TipoEstanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TipoEstanteRestController {

    @Autowired
    private TipoEstanteService service;

    @GetMapping(value = "/tipos", produces = "application/json")
    public ResponseEntity<List<TipoEstante>> getTiposEstantes() {

        return ResponseEntity.ok(service.listarTiposEstantes());
    }

    @GetMapping(value = "/tipo/{designacao}", produces = "application/json")
    public ResponseEntity<TipoEstante> getTiposEstantes(@RequestParam("nome") String designacao) {

        return ResponseEntity.ok(service.pesquisarPorDesignacao(designacao));
    }

    @PostMapping(value = "/tipos", consumes = "application/json", produces = "application/json")
    public ResponseEntity<TipoEstante> registaTipo(@Valid @RequestBody TipoEstante tipoEstante) {

        TipoEstante tipoEstanteGravada = service.registar(tipoEstante);

        return ResponseEntity.ok(tipoEstanteGravada);
    }

}
