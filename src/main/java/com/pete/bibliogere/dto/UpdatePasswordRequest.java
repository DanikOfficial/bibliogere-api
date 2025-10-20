package com.pete.bibliogere.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdatePasswordRequest extends CreatePasswordRequest {
    @NotBlank(message = "A senha antiga é obrigatória")
    private String oldPassword;
}
