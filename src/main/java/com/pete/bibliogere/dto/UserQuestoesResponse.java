package com.pete.bibliogere.dto;

import com.pete.bibliogere.modelo.QuestoesSeguranca;
import lombok.Data;

@Data
public class UserQuestoesResponse {
    private String primeiraQuestao;
    private String segundaQuestao;

    public UserQuestoesResponse(QuestoesSeguranca questoesSeguranca) {
        primeiraQuestao = questoesSeguranca.getPrimeiraQuestao();
        segundaQuestao = questoesSeguranca.getSegundaQuestao();
    }
}
