package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.modelo.Estante;
import com.pete.bibliogere.modelo.TipoEstante;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import com.pete.bibliogere.modelo.excepcoes.EstanteAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.EstanteException;
import com.pete.bibliogere.modelo.excepcoes.EstanteNotFoundException;
import com.pete.bibliogere.repositorios.EstanteRepositorio;
import com.pete.bibliogere.repositorios.ObraRepositorio;
import com.pete.bibliogere.services.EstanteService;
import com.pete.bibliogere.services.TipoEstanteService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EstanteServiceImpl implements EstanteService {

    @Autowired
    private EstanteRepositorio estanteRepositorio;

    @Autowired
    private TipoEstanteService tipoEstanteService;

    @Autowired
    private ReusableEntityOperation reusable;

    @Autowired
    private ObraRepositorio obraRepositorio;

    @PersistenceContext
    private EntityManager em;

    @Override
    public Estante registar(Estante estante) {

        // Verifica se o tipo de estante passado existe
        String tipoEstante = handleTipoEstanteExists(estante.getTipoEstante());

        // Atualiza tipo estante
        estante.setTipoEstante(tipoEstante);

        // Verifica se a estante já existe
        handleEstanteExists(estante);

        return estanteRepositorio.save(estante);
    }

    @Override
    public void registarEstantes(List<Estante> estantes) {
        estanteRepositorio.saveAll(estantes);
    }

    @Override
    @Transactional
    public Estante atualizar(Map<Object, Object> fields, Long codigo) {
        Estante estanteAntiga = pesquisarEstantePorCodigo(codigo);

        Estante estanteAtualizada = reusable.buildEntityToUpdate(estanteAntiga, fields);

        String tipoEstante = handleTipoEstanteExists(estanteAtualizada.getTipoEstante());

        estanteAtualizada.setTipoEstante(tipoEstante);

        reusable.handleConstraintViolation(estanteAtualizada);

        handleEstanteExists(estanteAtualizada, estanteAtualizada.getCodigo());

        Query query = em.createNativeQuery("UPDATE obras " +
                "SET nome_estante = '" + estanteAtualizada.getNome() + "' " +
                "WHERE codigo_estante =" + estanteAtualizada.getCodigo());
        query.executeUpdate();

//        return estanteRepositorio.save(estanteAtualizada);
        return estanteAtualizada;
    }

    @Override
    public Long apagar(Long id) {

        Estante estante = pesquisarEstantePorCodigo(id);

        BigInteger total = (BigInteger) em.createNativeQuery(
                "SELECT COUNT(titulo) FROM obras WHERE codigo_estante = :codigo AND is_deleted = FALSE")
                .setParameter("codigo", id).getSingleResult();

        if (total.intValue() > 0)
            throw new EstanteException("Impossível apagar esta estante porque ela contém obras!");

        estante.setIsDeleted(Boolean.TRUE);

        estanteRepositorio.save(estante);

        return estante.getCodigo();
    }

    @Override
    public Estante pesquisarPorNome(String nome, String tipoEstante) {
        //		Optional<Estante> estanteEncontrada = estanteRepositorio.findEstanteByNomeIgnoreCaseAndTipoEstanteIgnoreCase(
        //				nome.toUpperCase().trim(), tipoEstante.toUpperCase().trim());

        Optional<Estante> estanteEncontrada = estanteRepositorio
                .findEstanteByNomeIgnoreCaseAndTipoEstanteIgnoreCase(nome.trim(), tipoEstante.trim());

        return estanteEncontrada.orElseThrow(() -> new EstanteNotFoundException("A estante digitada não existe!"));
    }

    @Override
    public Estante pesquisarEstantePorCodigo(Long codigo) {
        return estanteRepositorio.findByCodigoAndIsDeletedFalse(codigo)
                .orElseThrow(() -> new EstanteNotFoundException("A estante digitada não existe!"));
    }

    @Override
    public List<Estante> listarEstantes() {
        return estanteRepositorio.findFirst20ByIsDeletedIsFalseOrderByCodigoDesc(PageRequest.of(0, 20)).getContent();
    }

    @Override
    public void alteraTotal(Quantidade operacao, Estante estante) {

        switch (operacao) {
            case AUMENTAR:
                estante.setTotalObras(estante.getTotalObras() + 1);
                break;
            case DIMINUIR:
                if (estante.getTotalObras() != 0) estante.setTotalObras(estante.getTotalObras() - 1);
                break;
            default:
                estante.setTotalObras(estante.getTotalObras());
        }

    }

    private void handleEstanteExists(Estante estante) {

        Optional<Estante> estanteEncontrada = estanteRepositorio.findEstanteByNomeIgnoreCaseAndTipoEstanteIgnoreCase(
                estante.getNome().trim(), estante.getTipoEstante().trim());

        String message = "Já existe uma estante com o nome de " + estante.getNome() + "!";

        reusable.handleEntityExists(estanteEncontrada, new EstanteAlreadyExistsException(message));
    }

    private void handleEstanteExists(Estante estante, Long codigoEstante) {

        Optional<Estante> estanteEncontrada = estanteRepositorio
                .findByNomeIgnoreCaseAndTipoEstanteIgnoreCaseAndCodigoNot(estante.getNome().trim(),
                        estante.getTipoEstante().trim(), estante.getCodigo());

        String message = "Já existe uma estante com o nome de " + estante.getNome() + "!";

        reusable.handleEntityExists(estanteEncontrada, new EstanteAlreadyExistsException(message));
    }

    private String handleTipoEstanteExists(String tipoEstante) {
        TipoEstante tipoEstanteEncontrada = tipoEstanteService.pesquisarPorDesignacao(tipoEstante);

        return tipoEstanteEncontrada.getDesignacao();
    }

}
