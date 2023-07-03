package com.pete.bibliogere.services;

import com.pete.bibliogere.modelo.Localizacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LocalizacaoService {

    Localizacao registar(Localizacao localizacao);

    void registarLocalizacoes(List<Localizacao> localizacoes);

    List<Localizacao> listarLocalizacoes();

    Localizacao pesquisaLocalizacaoPorCodigo(Long codigo);

}
