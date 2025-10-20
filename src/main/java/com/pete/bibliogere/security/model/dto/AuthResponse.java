package com.pete.bibliogere.security.model.dto;

import com.pete.bibliogere.dto.UserQuestoesResponse;
import com.pete.bibliogere.modelo.Permissao;
import com.pete.bibliogere.modelo.Utilizador;
import lombok.Data;

import java.util.Collection;

@Data
public class AuthResponse {

    private Long codigo;

    private String username;

    private String nome;

    private Collection<Permissao> permissoes;

    private UserQuestoesResponse questoes;

    private String token;

    private boolean firstLogin;

    public AuthResponse(Utilizador utilizador, String token) {
        this.codigo = utilizador.getCodigo();
        this.username = utilizador.getUsername();
        this.nome = utilizador.getNome();
        this.permissoes = utilizador.getPermissoes();
        this.token = token;
        this.firstLogin = utilizador.getIsFirstLogin();

        if (utilizador.getQuestoesSeguranca() != null) {
            questoes = new UserQuestoesResponse(utilizador.getQuestoesSeguranca());
        }
    }

}
