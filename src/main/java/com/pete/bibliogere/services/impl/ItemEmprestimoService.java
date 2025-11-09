package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.ItemEmprestimoDTO;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.ItemEmprestimo;

import java.util.List;

public interface ItemEmprestimoService {

    /**
     * Registar items de empréstimo
     * @param codigosObras Array of obra codes
     * @param emprestimo The Emprestimo object (changed from Long codigoEmprestimo)
     * @return List of created ItemEmprestimo
     */
    List<ItemEmprestimo> registarItens(Long[] codigosObras, Emprestimo emprestimo);

    /**
     * Get all items for a specific emprestimo
     * @param codigoEmprestimo Emprestimo code
     * @return List of ItemEmprestimoDTO
     */
    List<ItemEmprestimoDTO> getEmprestimoItems(Long codigoEmprestimo);

    /**
     * Devolver (return) a single item
     * @param codigo Item code
     * @return Item code that was returned
     */
    Long devolverItem(Long codigo);

    /**
     * Devolver (return) all items of an emprestimo
     * @param emprestimo The Emprestimo object
     * @return List of item codes that were returned
     */
    List<Long> devolverItens(Emprestimo emprestimo);
}
