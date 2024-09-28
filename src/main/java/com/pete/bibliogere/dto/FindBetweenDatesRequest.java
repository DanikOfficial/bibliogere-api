package com.pete.bibliogere.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class FindBetweenDatesRequest {

    @NotNull(message = "A data de inicio é obrigatoria!")
    private LocalDate startDate;

    @NotNull(message = "A data de fim é obrigatoria!")
    private LocalDate endDate;
}
