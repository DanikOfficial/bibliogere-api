package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity(name = "livros")
@Table(name = "livros")
@PrimaryKeyJoinColumn(name = "codigo_livro")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Livro extends Obra {

    @Column(nullable = false)
    @NotBlank(message = "A editora não pode ser vázio!")
    private String editora;

    public Livro(String titulo, String autor, String editora, Integer ano, int quantiadeInicial, int quantidadeAtual) {
        super(titulo, autor, ano, quantiadeInicial, quantidadeAtual);
        this.editora = editora;
    }

    public Livro(long codigo, String titulo, String autor, String editora, int ano, int quantiadeInicial) {
        super(codigo, titulo, autor, ano);
        this.editora = editora;
    }

}
