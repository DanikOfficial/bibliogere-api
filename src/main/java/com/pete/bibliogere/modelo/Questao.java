package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "questoes")
@Table(name = "questoes")
@NoArgsConstructor
public class Questao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    private String nome;

    public Questao(String nome) {
        super();
        this.nome = nome;
    }

}
