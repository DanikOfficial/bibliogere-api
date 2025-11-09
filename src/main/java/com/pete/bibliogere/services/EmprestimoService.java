package com.pete.bibliogere.services;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.dto.EmprestimoReadDTO;
import com.pete.bibliogere.dto.FindBetweenDatesRequest;
import com.pete.bibliogere.modelo.Emprestimo;

import java.util.List;
import java.util.Map;

public interface EmprestimoService {

    /**
     * Register a new emprestimo
     * @param emprestimo Emprestimo to register
     * @return EmprestimoDTO
     */
    EmprestimoDTO registar(Emprestimo emprestimo);

    /**
     * Update an existing emprestimo
     * @param fields Fields to update
     * @param codigoEmprestimo Emprestimo code
     * @return Updated EmprestimoDTO
     */
    EmprestimoDTO atualizar(Map<Object, Object> fields, Long codigoEmprestimo);

    /**
     * Return all items of an emprestimo
     * @param codigoEmprestimo Emprestimo code
     * @return EmprestimoDTO
     */
    EmprestimoDTO devolver(Long codigoEmprestimo);

    /**
     * Search emprestimos with pagination
     * @param utente Utente name
     * @param page Page number
     * @return Map with paginated results
     */
    Map<String, Object> pesquisarEmprestimosPaging(String utente, int page);

    /**
     * Find emprestimo by code
     * @param codigo Emprestimo code
     * @return Emprestimo entity
     */
    Emprestimo pesquisarEmprestimoPorCodigo(Long codigo);

    /**
     * Search emprestimos by utente name (like search)
     * @param utente Utente name
     * @return List of EmprestimoReadDTO
     */
    List<EmprestimoReadDTO> pesquisarEmprestimosLikeUtente(String utente);

    /**
     * Find emprestimo by code (returns DTO)
     * @param codigo Emprestimo code
     * @return EmprestimoDTO
     */
    EmprestimoDTO findEmprestimoByCodigo(Long codigo);

    /**
     * Find emprestimo by utente code
     * @param codigoUtente Utente code
     * @return EmprestimoDTO
     */
    EmprestimoDTO findEmprestimoByUtente(String codigoUtente);

    /**
     * Find emprestimos between dates
     * @param datesDTO Date range
     * @return List of EmprestimoDTO
     */
    List<EmprestimoDTO> findEmprestimosBetween(FindBetweenDatesRequest datesDTO);
}
