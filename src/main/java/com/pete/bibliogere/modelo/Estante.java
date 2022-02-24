package com.pete.bibliogere.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "estantes")
@Table(name = "estantes")
@NoArgsConstructor
public class Estante implements GenericModel, ValidableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    @NotBlank(message = "O nome da estante é obrigatório!")
    private String nome;

    @Column(nullable = false, name = "tipo_estante")
    @NotBlank(message = "O tipo de estante é obrigatório!")
    private String tipoEstante;

    @JsonIgnore
    private Boolean isDeleted;

    private int totalObras;

//	@OneToMany(mappedBy = "estante", cascade = { CascadeType.PERSIST,
//			CascadeType.REMOVE }, orphanRemoval = true, fetch = FetchType.LAZY)
//	private List<Obra> obras;

    public Estante(String nome, String tipo_estante) {
        this.nome = nome;
        this.tipoEstante = tipo_estante;
    }

    public Estante(Long codigo, String nome, String tipoEstante) {
        super();
        this.codigo = codigo;
        this.nome = nome;
        this.tipoEstante = tipoEstante;
    }


}
