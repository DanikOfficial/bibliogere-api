package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.QuestoesSegurancaOperationRequest;
import com.pete.bibliogere.dto.UserQuestoesResponse;
import com.pete.bibliogere.dto.ValidateQuestoesResponse;
import com.pete.bibliogere.modelo.QuestoesSeguranca;
import com.pete.bibliogere.modelo.Utilizador;
import com.pete.bibliogere.modelo.excepcoes.DuplicateQuestionException;
import com.pete.bibliogere.modelo.excepcoes.QuestoesSegurancaNotFoundException;
import com.pete.bibliogere.repositorios.QuestaoRepositorio;
import com.pete.bibliogere.repositorios.QuestoesSegurancaRepository;
import com.pete.bibliogere.services.QuestoesSegurancaService;
import com.pete.bibliogere.services.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestoesSegurancaServiceImpl implements QuestoesSegurancaService {

    @Autowired
    QuestoesSegurancaRepository repository;

    @Autowired
    QuestaoRepositorio questoesRepository;

    @Autowired
    UtilizadorService utilizadorService;

    @Override
    public void createQuestoes(QuestoesSeguranca questoesSeguranca, String username) {
        Utilizador utilizador = utilizadorService.pesquisarPorUsername(username);
        questoesSeguranca.setUtilizador(utilizador);
        repository.save(questoesSeguranca);
    }

    private QuestoesSeguranca createQuestoes(QuestoesSegurancaOperationRequest request) {

        validateDuplicateQuestions(request);

        QuestoesSeguranca questoesSeguranca = new QuestoesSeguranca();
        questoesSeguranca.setPrimeiraQuestao(request.getPrimeiraQuestao().trim());
        questoesSeguranca.setPrimeiraResposta(request.getPrimeiraResposta().trim());
        questoesSeguranca.setSegundaQuestao(request.getSegundaQuestao().trim());
        questoesSeguranca.setSegundaResposta(request.getSegundaResposta().trim());

        Utilizador utilizador = utilizadorService.pesquisaPorCodigo(request.getCodigoUtilizador());
        questoesSeguranca.setUtilizador(utilizador);

        return repository.save(questoesSeguranca);
    }

    private void validateDuplicateQuestions(QuestoesSegurancaOperationRequest request) {
        // Trim and compare to handle whitespace
        if (request.getPrimeiraQuestao().trim().equalsIgnoreCase(request.getSegundaQuestao().trim())) {
            throw new DuplicateQuestionException("A primeira questão e a segunda questão são iguais!");
        }

        // Also check if answers are the same (optional but recommended)
        if (request.getPrimeiraResposta().trim().equalsIgnoreCase(request.getSegundaResposta().trim())) {
            throw new DuplicateQuestionException("A primeira resposta e a segunda resposta são iguais!");
        }
    }

    // For UPDATING existing security questions
    private QuestoesSeguranca updateQuestoes(QuestoesSegurancaOperationRequest request) {

        validateDuplicateQuestions(request);

        // Find existing questions for this user
        QuestoesSeguranca questoesSeguranca = repository.findByUtilizadorCodigo(request.getCodigoUtilizador())
                .orElseThrow(() -> new QuestoesSegurancaNotFoundException("Questões de segurança não encontradas para este utilizador"));

        // Update the fields
        questoesSeguranca.setPrimeiraQuestao(request.getPrimeiraQuestao().trim());
        questoesSeguranca.setPrimeiraResposta(request.getPrimeiraResposta().trim());
        questoesSeguranca.setSegundaQuestao(request.getSegundaQuestao().trim());
        questoesSeguranca.setSegundaResposta(request.getSegundaResposta().trim());

        return repository.save(questoesSeguranca);
    }

    @Override
    public ValidateQuestoesResponse validate(QuestoesSegurancaOperationRequest request) {
        Utilizador utilizador = utilizadorService.pesquisaPorCodigo(request.getCodigoUtilizador());
        QuestoesSeguranca questaoUtilizador = utilizador.getQuestoesSeguranca();

        boolean firstQuestionValid = questaoUtilizador.getPrimeiraQuestao().trim().equalsIgnoreCase(request.getPrimeiraQuestao().trim()) &&
                questaoUtilizador.getPrimeiraResposta().trim().equalsIgnoreCase(request.getPrimeiraResposta().trim());

        boolean secondQuestionValid = questaoUtilizador.getSegundaQuestao().trim().equalsIgnoreCase(request.getSegundaQuestao().trim()) &&
                questaoUtilizador.getSegundaResposta().trim().equalsIgnoreCase(request.getSegundaResposta().trim());

        boolean isValid = firstQuestionValid && secondQuestionValid;

        return ValidateQuestoesResponse.builder()
                .valid(isValid)
                .message(isValid ? "VÁLIDO" : "Erro ao validar questoes")
                .firstQuestionValid(firstQuestionValid)
                .secondQuestionValid(secondQuestionValid)
                .firstQuestionMessage(firstQuestionValid ? "A primeira questão está correcta" : "A primeira questão está errada")
                .secondQuestionMessage(secondQuestionValid ? "A segunda questão está correcta" : "A segunda questão está errada")
                .build();
    }

    @Override
    public UserQuestoesResponse create(QuestoesSegurancaOperationRequest request) {
        if (request.isUpdating()) {
            return new UserQuestoesResponse(updateQuestoes(request));
        }

        return new UserQuestoesResponse(createQuestoes(request));
    }

    @Override
    public UserQuestoesResponse getUserQuestoes(Long codigoUtilizador) {
        Utilizador utilizador = utilizadorService.pesquisaPorCodigo(codigoUtilizador);
        QuestoesSeguranca questoesSeguranca = utilizador.getQuestoesSeguranca();
        return new UserQuestoesResponse(questoesSeguranca);
    }
}
