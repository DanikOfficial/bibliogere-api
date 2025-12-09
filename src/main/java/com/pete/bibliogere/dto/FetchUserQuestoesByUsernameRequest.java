package com.pete.bibliogere.dto;

import jakarta.validation.constraints.NotBlank;

public record FetchUserQuestoesByUsernameRequest(@NotBlank(message = "O utilizador é obrigatorio!") String username) {
}
