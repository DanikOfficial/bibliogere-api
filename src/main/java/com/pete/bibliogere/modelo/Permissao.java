package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "permissoes")
@Table(name = "permissoes")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Nome da Permissão é obrigatório!")
    @NonNull
    private String nome;

}
