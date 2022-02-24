package com.pete.bibliogere.security.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthRequest {

    @NotBlank(message = "O utilizador é obrigatório!")
    private String username;

    private String password;
}
