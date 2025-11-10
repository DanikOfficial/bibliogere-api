package com.pete.bibliogere.dto;

import jakarta.validation.constraints.NotBlank;

public record ValidateQuestoesRequest(@NotBlank(message = "A primeira questão é obrigatória e não pode estar em branco")
                                      String primeiraQuestao,
                                      @NotBlank(message = "A primeira resposta é obrigatória e não pode estar em branco")
                                      String primeiraResposta,
                                      @NotBlank(message = "A segunda questão é obrigatória e não pode estar em branco")
                                      String segundaQuestao,
                                      @NotBlank(message = "A segunda resposta é obrigatória e não pode estar em branco")
                                      String segundaResposta,
                                      @NotBlank(message = "O Utilizador não pode estar em branco")
                                      String username) {
}
