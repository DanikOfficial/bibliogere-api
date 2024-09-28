package com.pete.bibliogere.dto;

import com.pete.bibliogere.modelo.ItemEmprestimo;
import com.pete.bibliogere.modelo.Obra;
import com.pete.bibliogere.modelo.enumeracao.SituacaoItemEmprestimo;
import lombok.Data;
import org.hibernate.Hibernate;

import java.time.LocalDate;

@Data
public class ItemEmprestimoDTO {

    private Long codigo;

    private LocalDate data_devolucao;

    private LocalDate data_realizacao;

    private SituacaoItemEmprestimo situacao;

    private Obra obra;

    public ItemEmprestimoDTO(ItemEmprestimo item) {
        codigo = item.getCodigo();
        data_devolucao = item.getDataDevolucao();
        data_realizacao = item.getDataRealizacao();
        situacao = item.getSituacao();
        obra = Hibernate.unproxy(item.getObra(), Obra.class);
    }
}
