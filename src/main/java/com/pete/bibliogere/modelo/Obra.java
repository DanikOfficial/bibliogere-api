package com.pete.bibliogere.modelo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "obras")
@Table(name = "obras",
        indexes = {
                @Index(name = "idx_obras_created_at", columnList = "createdAt")
        }
    )
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = Monografia.class, name = "monografia"),
        @Type(value = Livro.class, name = "livro"),
})
@NamedEntityGraphs(value = {
        @NamedEntityGraph(attributeNodes = {
                @NamedAttributeNode(value = "localizacao"),
                @NamedAttributeNode(value = "estante")
        })
})
public abstract class Obra implements GenericModel, ValidableModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long codigo;

    @Column(nullable = false)
    @NotBlank(message = "O autor não pode ser vázio!")
    private String autor;

    @Column(nullable = false)
    @NotBlank(message = "O titulo não pode ser vázio!")
    private String titulo;

    @Min(value = 1000, message = "A ano digitado é invalido!")
    @NotNull(message = "O ano é obrigatório!")
    private Integer ano;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false, name = "quantidade_inicial")
    @Min(value = 0, message = "A quantidade não pode ser menor que {value}!")
    @NotNull(message = "A quantidade é obrigatória!")
    private Integer quantidadeInicial;

    @Column(nullable = false, name = "quantidade_atual")
    private Integer quantidadeAtual;

    //@JsonIgnore
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_estante")
    private Estante estante;

    @Column(name = "nome_estante")
    private String nomeEstante;

    @Column(name = "localizacao")
    private String localizacaoDesignacao;

    @Transient
    private Long codigoLocalizacao;

    @Transient
    private Long codigoEstante;

    private String tipoObra;

    private LocalDate createdAt;

    //@JsonIgnore
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_localizacao", nullable = true)
    private Localizacao localizacao;


    public Obra(String titulo, String autor, Integer ano, Integer quantidadeInicial, Integer quantidadeAtual) {
        super();
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.quantidadeInicial = quantidadeInicial;
        this.quantidadeAtual = quantidadeAtual;
    }

    public Obra(long codigo, String titulo, String autor, Integer ano) {
        super();
        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
    }

    public Obra(@NotBlank(message = "O autor não pode ser vázio!") String autor,
                @NotBlank(message = "O titulo não pode ser vázio!") String titulo,
                @Min(value = 1000, message = "A ano digitado é invalido!") Integer ano, Localizacao localizacao) {
        super();
        this.autor = autor;
        this.titulo = titulo;
        this.ano = ano;
        this.localizacao = localizacao;
    }


}
