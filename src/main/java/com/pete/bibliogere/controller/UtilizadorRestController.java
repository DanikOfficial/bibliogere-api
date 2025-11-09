package com.pete.bibliogere.controller;

import com.pete.bibliogere.dto.*;
import com.pete.bibliogere.modelo.Questao;
import com.pete.bibliogere.security.model.dto.AuthRequest;
import com.pete.bibliogere.security.model.dto.AuthResponse;
import com.pete.bibliogere.services.QuestoesSegurancaService;
import com.pete.bibliogere.services.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class UtilizadorRestController {

    @Autowired
    private UtilizadorService utilizadorService;
    @Autowired
    private QuestoesSegurancaService questoesSegurancaService;

    @PostMapping(value = "/utilizador/entrar", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthResponse> entrar(@Valid @RequestBody AuthRequest authRequest) {

        return ResponseEntity.ok(utilizadorService.entrar(authRequest));
    }

    @PostMapping(value = "/utilizador/refresh", produces = "application/json")
    public ResponseEntity<Map<String, Object>> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return utilizadorService.refresh(refreshToken);
    }

    @PostMapping(value = "/admin/utilizadores", produces = "application/json")
    public ResponseEntity<AtendenteInfo> create(@Valid @RequestBody CreateAtendenteRequest request) {
        return ResponseEntity.ok(utilizadorService.createAtendente(request));
    }

    @GetMapping(value = "/admin/utilizadores", produces = "application/json")
    public ResponseEntity<List<AtendenteInfo>> getAtendentes() {
        return ResponseEntity.ok(utilizadorService.getAtendentes());
    }

    @PutMapping(value = "/utilizadores/password/reset/{codigo}", produces = "application/json")
    public ResponseEntity<SimpleResponse> updatePassword(@Valid @RequestBody UpdatePasswordRequest request, @PathVariable("codigo") Long codigo) {
            request.setCodigoUtilizador(codigo);
        return ResponseEntity.ok(utilizadorService.updatePassword(request));
    }

    @PostMapping(value = "/utilizadores/activate/{codigo}", produces = "application/json")
    public ResponseEntity<AuthResponse> activate(@Valid @RequestBody CreatePasswordRequest request, @PathVariable("codigo") Long codigo) {
        request.setCodigoUtilizador(codigo);
        return ResponseEntity.ok(utilizadorService.createPassword(request));
    }

    @PutMapping(value = "/utilizadores/{codigo}/questoes/validate", produces = "application/json")
    public ResponseEntity<ValidateQuestoesResponse> validateQuestoes(@Valid @RequestBody QuestoesSegurancaOperationRequest questoesSegurancaValidateRequest, @PathVariable("codigo") Long codigo) {
        questoesSegurancaValidateRequest.setCodigoUtilizador(codigo);
        return ResponseEntity.ok(questoesSegurancaService.validate(questoesSegurancaValidateRequest));
    }

    @GetMapping(value = "/utilizadores/{codigoUtilizador}/questoes", produces = "application/json")
    public ResponseEntity<UserQuestoesResponse> getUserQuestoes(@PathVariable("codigoUtilizador") Long codigoUtilizador) {
        return ResponseEntity.ok(questoesSegurancaService.getUserQuestoes(codigoUtilizador));
    }

    @PostMapping(value = "/utilizadores/{codigo}/questoes", produces = "application/json")
    public ResponseEntity<UserQuestoesResponse> create(@Valid @RequestBody QuestoesSegurancaOperationRequest questoesSegurancaValidateRequest, @PathVariable("codigo") Long codigo) {
        questoesSegurancaValidateRequest.setCodigoUtilizador(codigo);
        return ResponseEntity.ok(questoesSegurancaService.create(questoesSegurancaValidateRequest));
    }

    @DeleteMapping(value = "/admin/utilizadores/{codigo}", produces = "application/json")
    public ResponseEntity<AtendenteInfo> deleteAtendente(@PathVariable("codigo") Long codigo) {
        return ResponseEntity.ok(utilizadorService.deleteAtendente(codigo));
    }

    @PostMapping(value = "/admin/utilizadores/disable/{codigo}", produces = "application/json")
    public ResponseEntity<AtendenteInfo> disableAtendente(@PathVariable("codigo") Long codigo) {
        return ResponseEntity.ok(utilizadorService.desativarAtendente(codigo));
    }
}
