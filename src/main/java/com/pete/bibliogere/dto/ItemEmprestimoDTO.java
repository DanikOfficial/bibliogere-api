package com.pete.bibliogere.dto;

import com.pete.bibliogere.modelo.ItemEmprestimo;
import com.pete.bibliogere.modelo.Obra;
import lombok.Data;
import org.hibernate.Hibernate;

import java.time.LocalDate;

@Data
public class ItemEmprestimoDTO {

    private Long codigo;

    private String utente;

    private String utente_contacto;

    private String utente_email;

    private LocalDate data_devolucao;

    private LocalDate data_realizacao;

    private Obra obra;

    public ItemEmprestimoDTO(ItemEmprestimo item) {
        codigo = item.getCodigo();
        utente = item.getEmprestimo().getUtente();
        utente_contacto = item.getEmprestimo().getContacto();
        utente_email = item.getEmprestimo().getEmail();
        data_devolucao = item.getDataDevolucao();
        data_realizacao = item.getDataRealizacao();
        obra = Hibernate.unproxy(item.getObra(), Obra.class);
    }
}
