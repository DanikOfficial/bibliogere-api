package com.pete.bibliogere.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class QuestoesSegurancaOperationRequest {
    Long codigoUtilizador;

    @NotBlank(message = "A primeira questão é obrigatória e não pode estar em branco")
    String primeiraQuestao;

    @NotBlank(message = "A primeira resposta é obrigatória e não pode estar em branco")
    String primeiraResposta;

    @NotBlank(message = "A segunda questão é obrigatória e não pode estar em branco")
    String segundaQuestao;

    @NotBlank(message = "A segunda resposta é obrigatória e não pode estar em branco")
    String segundaResposta;

    boolean updating;
}
