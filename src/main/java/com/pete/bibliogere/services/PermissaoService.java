package com.pete.bibliogere.services;

import com.pete.bibliogere.modelo.Permissao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissaoService {

    void inicializarPermissoes(List<Permissao> permissoes);

    Permissao pesquisarPermissaoPorNome(String nome);

    List<Permissao> listarTodasPermissoes();
}
