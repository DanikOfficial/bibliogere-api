package com.pete.bibliogere.dto;

import com.pete.bibliogere.modelo.Emprestimo;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EmprestimoDTO {

    private Long codigo;

    private String utente;

    private String contacto;

    private String email;

    private List<ItemEmprestimoDTO> itens;

    public EmprestimoDTO(Emprestimo emprestimo) {
        this.codigo = emprestimo.getCodigo();
        this.utente = emprestimo.getUtente();
        this.contacto = emprestimo.getContacto();
        this.email = emprestimo.getEmail();
        this.itens = emprestimo.getItens().stream().map(ItemEmprestimoDTO::new).collect(Collectors.toList());
    }
}
