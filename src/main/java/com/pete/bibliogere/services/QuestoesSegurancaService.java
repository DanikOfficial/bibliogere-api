package com.pete.bibliogere.services;

import com.pete.bibliogere.dto.QuestoesSegurancaOperationRequest;
import com.pete.bibliogere.dto.UserQuestoesResponse;
import com.pete.bibliogere.dto.ValidateQuestoesResponse;
import com.pete.bibliogere.modelo.QuestoesSeguranca;
import org.springframework.stereotype.Service;

@Service
public interface QuestoesSegurancaService {
    void createQuestoes(QuestoesSeguranca questoesSeguranca, String username);
    ValidateQuestoesResponse validate(QuestoesSegurancaOperationRequest questoesSegurancaRequest);
    UserQuestoesResponse create(QuestoesSegurancaOperationRequest createQuestionRequest);
    UserQuestoesResponse getUserQuestoes(Long codigoUtilizador);
}
