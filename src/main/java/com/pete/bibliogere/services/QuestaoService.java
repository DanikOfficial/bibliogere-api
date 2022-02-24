package com.pete.bibliogere.services;

import com.pete.bibliogere.modelo.Questao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestaoService {

    void registaQuestoes(List<Questao> questoes);

    List<Questao> listarQuestoes();

}
