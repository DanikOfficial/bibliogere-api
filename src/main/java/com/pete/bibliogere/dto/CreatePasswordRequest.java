package com.pete.bibliogere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreatePasswordRequest {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha deve ter no mínimo 8 caracteres, incluindo uma letra maiúscula, uma minúscula, um número e um caractere especial")
    private String newPassword;

    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    private String confirmPassword;

    @NotBlank(message = "O ID do utilizador é obrigatório")
    private String username;
}
