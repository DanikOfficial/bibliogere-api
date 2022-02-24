package com.pete.bibliogere.controller;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.modelo.Questao;
import com.pete.bibliogere.services.QuestaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")
public class QuestaoRestController {

    @Autowired
    private QuestaoService service;

    @GetMapping(value = "/questoes", produces = "application/json")
    public ResponseEntity<List<Questao>> getQuestoes() {
        return ResponseEntity.ok( service.listarQuestoes());
    }

}
