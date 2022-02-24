package com.pete.bibliogere.controller;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.api.PesquisaUtil;
import com.pete.bibliogere.modelo.Obra;
import com.pete.bibliogere.services.ObraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ObraRestController {

    @Autowired
    private ObraService service;

    @Autowired
    private PesquisaUtil pesquisaUtil;

    @PostMapping(value = "/admin/obras/estante/{codigoEstante}/localizacao/{codigoLocalizacao}",
            consumes = "application/json", produces = "application/json")
    public ResponseEntity<Obra> storeData(@Valid @RequestBody Obra obra,
                                                         @PathVariable("codigoEstante") Long codigoEstante,
                                                         @PathVariable("codigoLocalizacao") Long codigoLocalizacao) {

        obra.setQuantidadeAtual(obra.getQuantidadeInicial());
        Obra novaObra = service.registarObra(obra, codigoEstante, codigoLocalizacao);

        return ResponseEntity.ok(novaObra);
    }

    @PatchMapping(value = "/admin/obra/{codigoObra}/estante/{codigoEstante}/localizacao/{codigoLocalizacao}",
            produces = "application/json", consumes = "application/json")
    public ResponseEntity<Obra> updateObra(@RequestBody Map<Object, Object> fields,
                                                          @PathVariable("codigoObra") Long codigoObra,
                                                          @PathVariable("codigoEstante") Long codigoEstante,
                                                          @PathVariable("codigoLocalizacao") Long codigoLocalizacao)
            throws MethodArgumentNotValidException {

        Obra novaObra = service.atualizarObra(fields, codigoEstante, codigoObra, codigoLocalizacao);

        return ResponseEntity.ok(novaObra);
    }

    @DeleteMapping(value = "/admin/obra/{codigoObra}", produces = "application/json")
    public ResponseEntity<Long> apagarObra(@PathVariable("codigoObra") Long codigoObra) {

        Obra obraApagada = service.apagarObra(codigoObra);

        return ResponseEntity.ok(obraApagada.getCodigo());
    }

    @GetMapping(value = "/admin/obras/monografias/pagina/{pagina}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> listaMonografiasPorPagina(@PathVariable("pagina") int pagina) {
        Map<String, Object> res = service.listarMonografiasPaging(pagina);

        return ResponseEntity.ok(res);
    }

    @GetMapping(value = "/admin/obras/livros/pagina/{pagina}", produces = "application/json")
    public ResponseEntity<Map<String, Object>> listaLivrosPorPagina(@PathVariable("pagina") int pagina) {

        Map<String, Object> res = service.listarLivrosPaging(pagina);

        return ResponseEntity.ok(res);
    }

    @GetMapping(value = "/obras/{codigoObra}", produces = "application/json")
    public ResponseEntity<Obra> getObra(@PathVariable("codigoObra") Long codigoObra) {

        Obra obraEncontrada = service.pesquisarPorCodigo(codigoObra);

        return ResponseEntity.ok(obraEncontrada);

    }
    @GetMapping(value = "/obras/monografias/pagina/{pagina}/pesquisa", produces = "application/json")
    public ResponseEntity<Map<String, Object>> pesquisaMonografiasPorCriterio(@PathVariable("pagina") int pagina,
                                                                              @RequestParam("titulo") String titulo,
                                                                              @RequestParam("ano") int ano,
                                                                              @RequestParam("autor") String autor,
                                                                              @RequestParam("tutor") String tutor) {

        Map<String, Object> pesquisa = service.pesquisarMonografias(titulo, tutor, ano, autor, pagina);

        return ResponseEntity.ok(pesquisa);
    }


    @GetMapping(value = "/obras/livros/pagina/{pagina}/pesquisa", produces = "application/json")
    public ResponseEntity<Map<String, Object>> pesquisaLivrosPorCriterio(@PathVariable("pagina") int pagina,
                                                                         @RequestParam("titulo") String titulo,
                                                                         @RequestParam("ano") int ano,
                                                                         @RequestParam("autor") String autor,
                                                                         @RequestParam("editora") String editora) {

        Map<String, Object> pesquisa = service.pesquisarLivros(titulo, editora, autor, ano, pagina);

        return ResponseEntity.ok(pesquisa);
    }
}
