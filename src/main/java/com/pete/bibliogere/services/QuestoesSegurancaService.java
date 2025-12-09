package com.pete.bibliogere.services;

import com.pete.bibliogere.dto.*;
import com.pete.bibliogere.modelo.QuestoesSeguranca;
import org.springframework.stereotype.Service;

@Service
public interface QuestoesSegurancaService {
    void createQuestoes(QuestoesSeguranca questoesSeguranca, String username);
    ValidateQuestoesResponse validate(QuestoesSegurancaOperationRequest questoesSegurancaRequest);
    ValidateQuestoesResponse validate(ValidateQuestoesRequest request);
    UserQuestoesResponse create(QuestoesSegurancaOperationRequest createQuestionRequest);
    UserQuestoesResponse getUserQuestoes(Long codigoUtilizador);
    FetchUserQuestoesByUsernameResponse getUserQuestoesFromRecover(FetchUserQuestoesByUsernameRequest request);
}
