package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.modelo.Localizacao;
import com.pete.bibliogere.modelo.excepcoes.LocalizacaoAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.LocalizacaoNotFoundException;
import com.pete.bibliogere.repositorios.LocalizacaoRepositorio;
import com.pete.bibliogere.services.LocalizacaoService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocalizacaoServiceImpl implements LocalizacaoService {

    @Autowired
    private LocalizacaoRepositorio repositorio;

    @Autowired
    private ReusableEntityOperation reusable;

    @Override
    public Localizacao registar(Localizacao localizacao) {

        // Verifica se a localização já existe
        handleLocalizacaoExists(localizacao);

        return repositorio.save(localizacao);
    }

    @Override
    public void registarLocalizacoes(List<Localizacao> localizacoes) {
        repositorio.saveAll(localizacoes);
    }

    @Override
    public List<Localizacao> listarLocalizacoes() {
        return repositorio.findAll();
    }

    @Override
    public Localizacao pesquisaLocalizacaoPorCodigo(Long codigo) {
        return repositorio.findById(codigo).orElseThrow(
                () -> new LocalizacaoNotFoundException("A localização digitada não existe!"));
    }

    private void handleLocalizacaoExists(Localizacao localizacao) {
        Optional<Localizacao> localizacaoEncontrada = repositorio
                .findByDesignacaoIgnoreCase(localizacao.getDesignacao().trim());

        reusable.handleEntityExists(localizacaoEncontrada, new LocalizacaoAlreadyExistsException(
                "Já existe a uma localização com nome " + localizacao.getDesignacao().trim() + "!")
        );

    }
}
