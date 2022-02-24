package com.pete.bibliogere.controller;

import com.pete.bibliogere.modelo.Estante;
import com.pete.bibliogere.services.EstanteService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EstanteRestController {

    @Autowired
    private EstanteService service;

    @GetMapping(value = "/estantes", produces = "application/json")
    public ResponseEntity<List<Estante>> getEstantes() {
        return ResponseEntity.ok(service.listarEstantes());
    }

    @PostMapping(value = "/admin/estantes", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Estante> registarEstante(@Valid @RequestBody Estante estante) {

        estante.setIsDeleted(Boolean.FALSE);

        Estante novaEstante = service.registar(estante);

        return new ResponseEntity<>(novaEstante, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/admin/estante/{codigo}", produces = "application/json")
    public ResponseEntity<Long> apagarEstante(@PathVariable("codigo") Long codigoEstante) {

        Long codigoRemovido = service.apagar(codigoEstante);

        return ResponseEntity.ok(codigoRemovido);
    }

    @PatchMapping("/admin/estante/{codigo}")
    public ResponseEntity<Estante> atualizarEstante(@PathVariable("codigo") Long codigo,
                                                    @RequestBody Map<Object, Object> fields) {

        Estante estanteGravada = service.atualizar(fields, codigo);

        return ResponseEntity.ok(estanteGravada);
    }

}
