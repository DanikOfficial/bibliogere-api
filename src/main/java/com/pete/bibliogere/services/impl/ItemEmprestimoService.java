package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.ItemEmprestimoDTO;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.ItemEmprestimo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemEmprestimoService {

    List<ItemEmprestimo> registarItens(Long[] codigosObras, Long codigoEmprestimo);

    List<ItemEmprestimoDTO> getEmprestimoItems(Long codigoEmprestimo);

//    ItemEmprestimoDTO registarItem(Long codigoObra, Long codigoEmprestimo);

    Long devolverItem(Long codigo);

    List<Long> devolverItens(Emprestimo emprestimo);
}
