package com.pete.bibliogere.controller;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.dto.ItemEmprestimoDTO;
import com.pete.bibliogere.modelo.ItemEmprestimo;
import com.pete.bibliogere.services.impl.ItemEmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ItemEmprestimoRestController {

    @Autowired
    private ItemEmprestimoService service;

    @PostMapping(value = "/atendente/emprestimo/{codigoEmprestimo}/itens",
            produces = "application/json",
            consumes = "application/json")
    public ResponseEntity<List<ItemEmprestimoDTO>> registarItens(@PathVariable("codigoEmprestimo") Long codigoEmprestimo,
                                                             @RequestBody Long[] codigosObras) {

        List<ItemEmprestimoDTO> itensEmprestimos = service.registarItens(codigosObras, codigoEmprestimo);

        return ResponseEntity.ok(itensEmprestimos);
    }

    @GetMapping(value = "/atendente/emprestimo/{codigoEmprestimo}/itens", produces = "application/json")
    public ResponseEntity<List<ItemEmprestimoDTO>> getEmprestimoItens(
            @PathVariable("codigoEmprestimo") Long codigoEmprestimo) {

        List<ItemEmprestimoDTO> itensEmprestimos = service.getEmprestimoItems(codigoEmprestimo);

        return ResponseEntity.ok(itensEmprestimos);
    }

    @PatchMapping(value = "/atendente/emprestimo/item/{codigoItem}", produces = "application/json")
    public ResponseEntity<Long> devolverItem(
            @PathVariable("codigoItem") Long codigoItem) {

        Long id = service.devolverItem(codigoItem);

        return ResponseEntity.ok(id);
    }

    @PatchMapping(value = "/atendente/emprestimo/{codigoEmprestimo}/itens", produces = "application/json")
    public ResponseEntity<List<Long>> devolverItens(
            @PathVariable("codigoEmprestimo") Long codigoEmprestimo) {

        List<Long> ids = service.devolverItens(codigoEmprestimo);

        return ResponseEntity.ok(ids);
    }
}