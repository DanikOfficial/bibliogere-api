package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity(name = "monografias")
@Table(name = "monografias")
@PrimaryKeyJoinColumn(name = "codigo_monografia")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Monografia extends Obra {

    @Column(nullable = false)
    @NotBlank(message = "O tutor não pode ser vázio!")
    private String tutor;


    public Monografia(@NotBlank(message = "O autor não pode ser vázio!") String autor,
                      @NotBlank(message = "O titulo não pode ser vázio!") String titulo,
                      @Min(value = 1000, message = "A ano digitado é invalido!") Integer ano, Localizacao localizacao,
                      @NotBlank(message = "O tutor não pode ser vázio!") String tutor) {
        super(autor, titulo, ano, localizacao);
        this.tutor = tutor;
    }

    public Monografia(String titulo, String autor, Integer ano,
                      @NotBlank(message = "O tutor não pode ser vázio!") String tutor, Integer quantiadeInicial,
                      Integer quantidadeAtual) {
        super(titulo, autor, ano, quantiadeInicial, quantidadeAtual);
        this.tutor = tutor;
    }

}
