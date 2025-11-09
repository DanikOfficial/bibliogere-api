package com.pete.bibliogere.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pete.bibliogere.modelo.Utilizador;
import lombok.Data;

@Data
public class AtendenteInfo {
    private String nome;
    private long codigo;
    private String username;

    @JsonProperty("isActive")  // This forces JSON to use "isActive"
    private boolean active;

    public AtendenteInfo(Utilizador utilizador) {
        this.codigo = utilizador.getCodigo();
        this.nome = utilizador.getNome();
        this.username = utilizador.getUsername();
        this.active = utilizador.getEnabled();
    }
}
