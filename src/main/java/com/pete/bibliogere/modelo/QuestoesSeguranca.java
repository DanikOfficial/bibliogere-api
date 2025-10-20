package com.pete.bibliogere.modelo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity(name = "questoes_seguranca")
@Table(name = "questoes_seguranca")
@Data
@NoArgsConstructor
public class QuestoesSeguranca {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    @NotBlank(message = "A primeira questão é obrigatória!")
    private String primeiraQuestao;

    @Column(nullable = false)
    @NotBlank(message = "A primeira resposta é obrigatória!")
    private String primeiraResposta;

    @Column(nullable = false)
    @NotBlank(message = "A segunda questão é obrigatória!")
    private String segundaQuestao;

    @Column(nullable = false)
    @NotBlank(message = "A segunda resposta é obrigatória!")
    private String segundaResposta;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "codigo_utilizador", nullable = false)
    private Utilizador utilizador;
}
