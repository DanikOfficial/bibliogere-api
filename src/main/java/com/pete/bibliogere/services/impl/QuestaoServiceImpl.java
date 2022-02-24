package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.modelo.Questao;
import com.pete.bibliogere.repositorios.QuestaoRepositorio;
import com.pete.bibliogere.services.QuestaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestaoServiceImpl implements QuestaoService {

    @Autowired
    private QuestaoRepositorio questaoRepositorio;

    @Override
    public void registaQuestoes(List<Questao> questoes) {
        questaoRepositorio.saveAll(questoes);
    }

    @Override
    public List<Questao> listarQuestoes() {
        return questaoRepositorio.findAll();
    }

}
