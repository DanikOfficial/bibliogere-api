package com.pete.bibliogere.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidateQuestoesResponse {
    private String message;
    private boolean valid;
    private boolean firstQuestionValid;
    private boolean secondQuestionValid;
    private String firstQuestionMessage;
    private String secondQuestionMessage;
}
