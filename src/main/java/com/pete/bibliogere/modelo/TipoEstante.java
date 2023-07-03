package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity(name = "tipo_estantes")
@Table(name = "tipo_estantes")
@NoArgsConstructor
public class TipoEstante implements GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    @NotBlank(message = "A designação do estante é obrigatória!")
    private String designacao;

    @Column(nullable = false)
    private Boolean isDefault;

    @Column(nullable = false)
    private Boolean isDeleted;

    public TipoEstante(@NotBlank(message = "A designação do estante é obrigatória!") String designacao,
                       Boolean isDefault, Boolean isDeleted) {
        super();
        this.designacao = designacao;
        this.isDefault = isDefault;
        this.isDeleted = isDeleted;
    }

}
	