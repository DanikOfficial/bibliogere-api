package com.pete.bibliogere.controller;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.dto.EmprestimoReadDTO;
import com.pete.bibliogere.dto.FindBetweenDatesRequest;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.services.EmprestimoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EmprestimoRestController {

    @Autowired
    private EmprestimoService service;

    @PostMapping(value = "/atendente/emprestimos", produces = "application/json", consumes = "application/json")
    public ResponseEntity<EmprestimoDTO> registar(@Valid @RequestBody Emprestimo emprestimo) {

        EmprestimoDTO emprestimoNovo = service.registar(emprestimo);

        return new ResponseEntity<>(emprestimoNovo, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/atendente/emprestimo/{codigoEmprestimo}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EmprestimoDTO> atualizar(@RequestBody Map<Object, Object> fields,
                                                   @PathVariable("codigoEmprestimo") Long codigoEmprestimo) {
        EmprestimoDTO emprestimoAtualizado = service.atualizar(fields, codigoEmprestimo);

        return ResponseEntity.ok(emprestimoAtualizado);
    }

    @PatchMapping(value = "/atendente/emprestimo/{codigoEmprestimo}/devolver", produces = "application/json")
    public ResponseEntity<EmprestimoDTO> devolver(@PathVariable("codigoEmprestimo") Long codigoEmprestimo) {

        EmprestimoDTO emprestimoAtualizado = service.devolver(codigoEmprestimo);

        return ResponseEntity.ok(emprestimoAtualizado);

    }

    @GetMapping(value = "/atendente/emprestimo/{codigoEmprestimo}", produces = "application/json")
    public ResponseEntity<EmprestimoDTO> getEmprestimo(@PathVariable("codigoEmprestimo") Long codigoEmprestimo) {

        EmprestimoDTO emprestimo = service.findEmprestimoByCodigo(codigoEmprestimo);

        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping(value = "/atendente/emprestimo/utente/{codigoUtente}", produces = "application/json")
    public ResponseEntity<EmprestimoDTO> getEmprestimoByUtente(@PathVariable("codigoUtente") String codigoEmprestimo) {
        EmprestimoDTO emprestimo = service.findEmprestimoByUtente(codigoEmprestimo);

        return ResponseEntity.ok(emprestimo);
    }


    @GetMapping(value = "/atendente/emprestimos/pagina/{pagina}/pesquisa", produces = "application/json")
    public ResponseEntity<Map<String, Object>> pesquisarEmprestimosPaging(@RequestParam("utente") String utente,
                                                                          @PathVariable("pagina") int pagina) {
        Map<String, Object> res = service.pesquisarEmprestimosPaging(utente, pagina);

        return ResponseEntity.ok(res);
    }

    @GetMapping(value = "/atendente/emprestimos/pesquisa", produces = "application/json")
    public ResponseEntity<List<EmprestimoReadDTO>> pesquisarEmprestimosPorCodigo(@RequestParam("utente") String utente) {
        List<EmprestimoReadDTO> emprestimos = service.pesquisarEmprestimosLikeUtente(utente);

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping(value = "/admin/emprestimos/relatorio")
    public ResponseEntity<List<EmprestimoDTO>> gerarRelatorioEmprestimos(@RequestBody @Valid FindBetweenDatesRequest findBetweenDatesDTO) {
        List<EmprestimoDTO> emprestimos = service.findEmprestimosBetween(findBetweenDatesDTO);

        return ResponseEntity.ok(emprestimos);
    }
}
