package com.pete.bibliogere.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "localizacoes")
@Table(name = "localizacoes")
@NoArgsConstructor
public class Localizacao implements GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    @NotBlank(message = "A designação da localização é obrigatória!")
    private String designacao;

    @JsonIgnore
    private Boolean isDeleted;

    public Localizacao(@NotBlank(message = "A designação da localização é obrigatória!") String designacao) {
        super();
        this.designacao = designacao;
    }

}
