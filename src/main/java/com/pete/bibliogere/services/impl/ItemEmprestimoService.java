package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.ItemEmprestimoDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemEmprestimoService {

    List<ItemEmprestimoDTO> registarItens(Long[] codigosObras, Long codigoEmprestimo);

    List<ItemEmprestimoDTO> getEmprestimoItems(Long codigoEmprestimo);

//    ItemEmprestimoDTO registarItem(Long codigoObra, Long codigoEmprestimo);

    Long devolverItem(Long codigo);

    List<Long> devolverItens(Long codigoEmprestimo);


}
