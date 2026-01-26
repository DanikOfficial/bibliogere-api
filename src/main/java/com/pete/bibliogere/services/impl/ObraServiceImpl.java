package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.modelo.*;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import com.pete.bibliogere.modelo.excepcoes.ObraAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.ObraException;
import com.pete.bibliogere.modelo.excepcoes.ObraNotFoundException;
import com.pete.bibliogere.repositorios.ObraRepositorio;
import com.pete.bibliogere.services.EstanteService;
import com.pete.bibliogere.services.LocalizacaoService;
import com.pete.bibliogere.services.ObraService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ObraServiceImpl implements ObraService {

    @Autowired
    private ObraRepositorio repositorio;

    @Autowired
    private EstanteService estanteService;

    @Autowired
    private ReusableEntityOperation reusable;

    @Autowired
    private LocalizacaoService localizacaoService;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Obra registarObra(Obra obra, Long codigoEstante, Long codigoLocalizacao) {
        Estante estanteEncontrada = estanteService.pesquisarEstantePorCodigo(codigoEstante);

        Localizacao localizacaoEncontrada = localizacaoService.pesquisaLocalizacaoPorCodigo(codigoLocalizacao);

        obra.setEstante(estanteEncontrada);

        obra.setIsDeleted(Boolean.FALSE);

        obra.setLocalizacao(localizacaoEncontrada);

        obra.setTipoObra(estanteEncontrada.getTipoEstante());

        obra.setNomeEstante(estanteEncontrada.getNome());

        obra.setLocalizacaoDesignacao(localizacaoEncontrada.getDesignacao());

        handleObraExists(obra);

        obra.setCreatedAt(LocalDate.now());

        obra = repositorio.save(obra);

        estanteService.alteraTotal(Quantidade.AUMENTAR, obra.getEstante());

        return obra;
    }

    @Override
    @Transactional
    public Obra atualizarObra(Map<Object, Object> fields, Long codigoEstante, Long codigoObra,
                              Long codigoLocalizacao) throws MethodArgumentNotValidException {

        Estante estanteEncontrada = estanteService.pesquisarEstantePorCodigo(codigoEstante);

        Localizacao localizacaoEncontrada = localizacaoService.pesquisaLocalizacaoPorCodigo(codigoLocalizacao);

        Obra obra = pesquisarPorCodigo(codigoObra);

        String type = removeType(fields);

        obra = reusable.buildEntityToUpdate(obra, fields);

        addType(fields, type);

        handleEstanteReplacement(obra.getEstante(), estanteEncontrada);

        obra.setEstante(estanteEncontrada);

        obra.setTipoObra(estanteEncontrada.getTipoEstante());

        obra.setLocalizacao(localizacaoEncontrada);

        obra.setNomeEstante(estanteEncontrada.getNome());

        obra.setLocalizacaoDesignacao(localizacaoEncontrada.getDesignacao());

        obra.setTipoObra(type);

        obra.setCreatedAt(LocalDate.now());

        reusable.handleConstraintViolation(obra);

        handleObraExists(obra, obra.getCodigo());

        return obra;
    }

    @Override
    @Transactional
    public Obra apagarObra(Long codigo) {

        Obra obra = pesquisarPorCodigo(codigo);

        BigInteger total = (BigInteger) em.createNativeQuery(
                "SELECT COUNT(codigo_obra) FROM itens_emprestimo WHERE codigo_obra = :codigo AND situacao = 'Activo'").setParameter(
                "codigo", codigo).getSingleResult();

        if (total.intValue() > 0)
            throw new ObraException("Impossível apagar esta obra porque está associada a um emprestimo!");

        obra.setIsDeleted(Boolean.TRUE);

//        Obra obraApagada = repositorio.save(obra);

        estanteService.alteraTotal(Quantidade.DIMINUIR, obra.getEstante());

        return obra;
    }

    @Override
    public Obra pesquisarPorCodigo(Long codigoObra) {
        return repositorio.findByCodigo(codigoObra).orElseThrow(
                () -> new ObraNotFoundException("A obra digitada não existe"));
    }

    @Override
    public List<Obra> pesquisarObras(String titulo) {
        return repositorio.findByTituloStartingWithIgnoreCase(titulo);
    }

    @Override
    public Map<String, Object> listarMonografiasPaging(int page) {
        Page<Monografia> results = repositorio.findAllMonografias(PageRequest.of(page, 30));
        return reusable.buildDataWithPaging(results, Monografia.class);
    }

    @Override
    public Map<String, Object> listarLivrosPaging(int page) {
        Page<Livro> results = repositorio.findAllLivros(PageRequest.of(page, 30));
        return reusable.buildDataWithPaging(results, Livro.class);
    }

    @Override
    public Map<String, Object> pesquisarMonografias(String titulo, String tutor, int ano, String autor, int page) {

        String tituloParam = titulo.toUpperCase().trim();
        String tutorParam = tutor.toUpperCase().trim();
        String autorParam = autor.toUpperCase().trim();

        Page<Monografia> results = repositorio.listaMonografiasPorCriterio(PageRequest.of(page, 30), tituloParam,
                autorParam, tutorParam, ano);
        return reusable.buildDataWithPaging(results, Monografia.class);
    }

    @Override
    public Map<String, Object> pesquisarLivros(String titulo, String editora, String autor, int ano, int page) {
        String tituloParam = titulo.toUpperCase().trim();
        String editoraParam = editora.toUpperCase().trim();
        String autorParam = autor.toUpperCase().trim();

        Page<Livro> results = repositorio.listaLivrosPorCriterio(PageRequest.of(page, 30), tituloParam, autorParam,
                editoraParam, ano);
        return reusable.buildDataWithPaging(results, Livro.class);
    }

    @Override
    public List<Obra> listarObras() {
        return repositorio.findTop5ByIsDeletedIsFalseOrderByCodigoDesc();
    }

    @Override
    public void alteraQuantidade(Quantidade operacao, Obra obra) {

        switch (operacao) {
            case AUMENTAR:
                obra.setQuantidadeAtual(obra.getQuantidadeAtual() + 1);
                break;
            case DIMINUIR:
                if (obra.getQuantidadeAtual() < 1)
                    throw new ObraException(("Impossível emprestar a obra com o titulo: " + obra.getTitulo() +" porque a quantidade é insuficiente"));

                obra.setQuantidadeAtual(obra.getQuantidadeAtual() - 1);
                break;
            default:
                break;
        }

    }

    private void handleObraExists(Obra obra) {

        Optional<Obra> obraOptional = repositorio.findObraByAutorIgnoreCaseAndTituloIgnoreCaseAndTipoObraIgnoreCase(
                obra.getAutor().trim(),
                obra.getTitulo().trim(), obra.getTipoObra().trim());

        String name = obra.getClass().getSimpleName();

//        name.endsWith("o") ? "O" : "A" + name + " digitad " + name.endsWith("o") ? "O" : "A" + " já existe!")
        String message = "Já existe " + name + " com os dados digitados!";

        reusable.handleEntityExists(obraOptional, new ObraAlreadyExistsException(message));
    }

    private void handleObraExists(Obra obra, Long codigo) {
        Optional<Obra> obraOptional = repositorio.findObraByAutorIgnoreCaseAndTituloIgnoreCaseAndTipoObraIgnoreCaseAndCodigoNot(
                obra.getAutor().trim(), obra.getTitulo().trim(), obra.getTipoObra().trim(), codigo);

        String name = obra.getClass().getSimpleName();

        reusable.handleEntityExists(obraOptional,
                new ObraAlreadyExistsException("A " + name + " digitada já existe!"));
    }

    private String removeType(Map<Object, Object> fields) {
        String type = (String) fields.get("type");
        fields.remove("type");
        return type;
    }

    private void addType(Map<Object, Object> fields, String type) {
        fields.put("type", type);
    }

    private void handleEstanteReplacement(Estante oldEstante, Estante newEstante) {

        if (!Objects.equals(oldEstante, newEstante)) {
            estanteService.alteraTotal(Quantidade.DIMINUIR, oldEstante);
            estanteService.alteraTotal(Quantidade.AUMENTAR, newEstante);
        }

    }

    @Override
    public List<Obra> listarObrasPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new ObraException("As datas de início e fim são obrigatórias para o relatório.");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new ObraException("A data de início não pode ser posterior à data final.");
        }

        List<Obra> obras = repositorio.findAllByCreatedAtBetween(dataInicio, dataFim);

        if (obras.isEmpty()) {
            throw new ObraNotFoundException("Nenhuma obra encontrada no período especificado.");
        }

        return obras;
    }


    }
