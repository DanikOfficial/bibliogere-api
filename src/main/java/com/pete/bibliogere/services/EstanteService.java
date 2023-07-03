package com.pete.bibliogere.services;

import com.pete.bibliogere.modelo.Estante;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public interface EstanteService {

    Estante registar(Estante estante);

    void registarEstantes(List<Estante> estantes);

    Estante atualizar(Map<Object, Object> fields, Long codigo);

    Long apagar(Long codigo);

    Estante pesquisarPorNome(String nome, String tipoEstante);

    Estante pesquisarEstantePorCodigo(Long codigo);

    List<Estante> listarEstantes();

    void alteraTotal(Quantidade operacao, Estante estante);

}
