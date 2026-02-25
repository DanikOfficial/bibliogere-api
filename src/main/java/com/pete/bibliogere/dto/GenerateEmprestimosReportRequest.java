package com.pete.bibliogere.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class GenerateEmprestimosReportRequest {

    @NotNull(message = "A data de inicio é obrigatoria!")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatoria!")
    private LocalDate dataFim;

    // Optional - filter by situacao
    @Pattern(regexp = "^(Activo|Expirado|Devolvido)$",
            message = "Situação deve ser: Activo, Expirado ou Devolvido")
    private String situacaoEmprestimo;
}