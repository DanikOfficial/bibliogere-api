package com.pete.bibliogere.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleResponse {
    private String message;
}
