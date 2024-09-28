package com.pete.bibliogere.dto;

import com.pete.bibliogere.modelo.Emprestimo;
import lombok.Data;

@Data
public class EmprestimoReadDTO {
    private Long codigo;

    private String utente;

    private String contacto;

    private String email;

    public EmprestimoReadDTO(Emprestimo emprestimo) {
        this.codigo = emprestimo.getCodigo();
        this.utente = emprestimo.getUtente();
        this.contacto = emprestimo.getContacto();
        this.email = emprestimo.getEmail();
    }
}
