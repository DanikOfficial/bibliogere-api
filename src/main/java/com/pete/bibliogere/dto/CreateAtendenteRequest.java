package com.pete.bibliogere.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CreateAtendenteRequest {
    @NotBlank(message = "O utilizador é obrigatório!")
    private String username;
    @NotBlank(message = "O nome do utilizador é obrigatório")
    private String nome;
}
