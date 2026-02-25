package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity(name = "emprestimos")
@Table(
        name = "emprestimos",
        indexes = {
                @Index(name = "idx_emprestimos_created_at", columnList = "createdAt")
        }
)
@NamedEntityGraph(
        name = "Emprestimo.completo",
        attributeNodes = {
                @NamedAttributeNode(value = "itens", subgraph = "itens-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "itens-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "obra", subgraph = "obra-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "obra-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("estante"),
                                @NamedAttributeNode("localizacao")
                        }
                )
        }
)

@NoArgsConstructor
public class Emprestimo implements ValidableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @Column(nullable = false)
    private String utente;

    private LocalDate createdAt;

    @Pattern(message = "O email digitado é inválido!", regexp = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@"
            + "((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$")
    private String email;

    @NotBlank(message = "O contacto é obrigatório!")
//    @Size(min = 9, max = 9, message = "O contacto digitado é inválido. Certifique-se de que o contacto tem {max} caracteres!")
    @Pattern(regexp = "^[0-9]{9}$", message = "O contacto digitado é inválido!")
    @Column(nullable = false)
    private String contacto;

    private Boolean isCurrent;

    @Column(nullable = false, name = "total_obras")
    private int totalObras;

    private LocalDate dataDevolucao;

    @Column(name = "multa")
    private int multa;

    @Transient
    private Long[] obrasIds;

    @OneToMany(mappedBy = "emprestimo", fetch = FetchType.LAZY)
    private List<ItemEmprestimo> itens;

    public Emprestimo(String utente, String contacto, String email) {
        this.utente = utente;
        this.contacto = contacto;
        this.email = email;
    }

    public Emprestimo(String utente, String contacto, String email, List<ItemEmprestimo> itensEmprestimos) {
        this(utente, contacto, email);
    }

    public Emprestimo(Long codigo, String utente, String contacto, String email) {
        this(utente, contacto, email);
        this.codigo = codigo;
    }

}
