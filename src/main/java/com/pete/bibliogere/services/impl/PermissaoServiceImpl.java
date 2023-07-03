package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.modelo.Permissao;
import com.pete.bibliogere.modelo.excepcoes.PermissaoNotFoundException;
import com.pete.bibliogere.repositorios.PermissaoRepositorio;
import com.pete.bibliogere.services.PermissaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissaoServiceImpl implements PermissaoService {

    @Autowired
    private PermissaoRepositorio repositorio;

    @Override
    public void inicializarPermissoes(List<Permissao> permissoes) {
        repositorio.saveAll(permissoes);
    }

    @Override
    public Permissao pesquisarPermissaoPorNome(String nome) {
        return repositorio.findByNomeIgnoreCase(nome.trim()).orElseThrow(
                () -> new PermissaoNotFoundException("A permissão digitada não existe"));
    }

    @Override
    public List<Permissao> listarTodasPermissoes() {
        return repositorio.findAll(Sort.by(Sort.Order.asc("nome")));
    }
}
