package com.pete.bibliogere.modelo.enumeracao;

public enum SituacaoItemEmprestimo {
    Activo("Activo"),
    Expirado("Expirado"),
    Devolvido("Devolvido");

    private final String situacao;

    SituacaoItemEmprestimo(String situacao) {
        this.situacao = situacao;
    }

    public String getSituacao() {
        return this.situacao;
    }
}
