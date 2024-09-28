package com.pete.bibliogere.modelo.dto;

import com.pete.bibliogere.modelo.Utilizador;
import lombok.Data;

@Data
public class UtilizadorDTO {

    private Long codigo;

    private String username;

    private String nome;

    private Boolean isActive;

    private Boolean isFirstLogin;

    public UtilizadorDTO(Utilizador utilizador) {
        this.codigo = utilizador.getCodigo();
        this.username = utilizador.getUsername();
        this.nome = utilizador.getNome();
        this.isActive = utilizador.getEnabled();
        this.isFirstLogin = utilizador.getIsFirstLogin();
    }
}
