package com.pete.bibliogere.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GenerateObraReportRequest {

    @NotNull(message = "A data de inicio é obrigatoria!")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatoria!")
    private LocalDate dataFim;

    private String tipoObra;

    private String nomeEstante;

}
