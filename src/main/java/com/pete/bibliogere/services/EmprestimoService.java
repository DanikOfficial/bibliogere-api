package com.pete.bibliogere.services;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.dto.EmprestimoReadDTO;
import com.pete.bibliogere.dto.FindBetweenDatesRequest;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EmprestimoService {

    EmprestimoDTO registar(Emprestimo emprestimo);

    EmprestimoDTO atualizar(Map<Object, Object> fields, Long codigoEmprestimo);

    EmprestimoDTO devolver(Long codigoEmprestimo);

    Map<String, Object> pesquisarEmprestimosPaging(String utente, int page);

    Emprestimo pesquisarEmprestimoPorCodigo(Long codigo);

    List<EmprestimoReadDTO> pesquisarEmprestimosLikeUtente(String utente);

    EmprestimoDTO findEmprestimoByCodigo(Long codigo);

    EmprestimoDTO findEmprestimoByUtente(String codigoUtente);

    List<EmprestimoDTO> findEmprestimosBetween(FindBetweenDatesRequest datesDTO);

    void alteraTotal(Quantidade operacao, Emprestimo emprestimo);

    void alteraTotal(Quantidade operacao, Emprestimo emprestimo, int quantidade);

}
