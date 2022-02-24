package com.pete.bibliogere.services;

import com.pete.bibliogere.modelo.TipoEstante;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TipoEstanteService {

    TipoEstante registar(TipoEstante tipoEstante);

    void registarEstantes(List<TipoEstante> tipoEstante);

    List<TipoEstante> listarTiposEstantes();

    TipoEstante pesquisarPorDesignacao(String designacao);
}
