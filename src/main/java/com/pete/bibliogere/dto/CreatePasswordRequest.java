package com.pete.bibliogere.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreatePasswordRequest {
    private Long codigoUtilizador;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "A senha deve ter no mínimo 8 caracteres, incluindo uma letra maiúscula, uma minúscula, um número e um caractere especial")
    private String newPassword;

    @NotBlank(message = "A confirmação da nova senha é obrigatória")
    private String confirmPassword;
}
