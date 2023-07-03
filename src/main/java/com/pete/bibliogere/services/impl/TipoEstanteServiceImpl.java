package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.modelo.TipoEstante;
import com.pete.bibliogere.modelo.excepcoes.TipoEstanteAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.TipoEstanteNotFoundException;
import com.pete.bibliogere.repositorios.TipoEstanteRepositorio;
import com.pete.bibliogere.services.TipoEstanteService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEstanteServiceImpl implements TipoEstanteService {

    @Autowired
    private TipoEstanteRepositorio tipoEstanteRepositorio;

    @Autowired
    private ReusableEntityOperation reusable;

    @Override
    public void registarEstantes(List<TipoEstante> tipoEstantes) {
        tipoEstanteRepositorio.saveAll(tipoEstantes);
    }

    @Override
    public List<TipoEstante> listarTiposEstantes() {
        return tipoEstanteRepositorio.findAll();
    }

    @Override
    public TipoEstante pesquisarPorDesignacao(String designacao) {

        Optional<TipoEstante> tipoEstante = tipoEstanteRepositorio.findByDesignacaoIgnoreCase(designacao.trim());

        return tipoEstante
                .orElseThrow(() -> new TipoEstanteNotFoundException("O tipo de estante especificado não existe!"));
    }

    @Override
    public TipoEstante registar(TipoEstante tipoEstante) {

        // Verifica se o tipo de estante já existe
        handleTipoEstanteExists(tipoEstante);

        return tipoEstanteRepositorio.save(tipoEstante);
    }

    public void handleTipoEstanteExists(TipoEstante tipoEstante) {
        Optional<TipoEstante> tipoEstanteEncontrada = tipoEstanteRepositorio
                .findByDesignacaoIgnoreCase(tipoEstante.getDesignacao().trim());

        reusable.handleEntityExists(tipoEstanteEncontrada, new TipoEstanteAlreadyExistsException(
                "Já existe um tipo de estante com o nome de " + tipoEstante.getDesignacao() + "!"));

    }
}
