package com.pete.bibliogere.security.model.dto;

import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
public class AuthRequest {

    @NotBlank(message = "O utilizador é obrigatório!")
    private String username;

    @NotBlank(message = "A senha é obrigatória!")
    private String password;
}
