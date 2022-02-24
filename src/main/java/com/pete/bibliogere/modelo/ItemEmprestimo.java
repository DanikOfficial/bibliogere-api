package com.pete.bibliogere.modelo;

import com.pete.bibliogere.modelo.enumeracao.SituacaoItemEmprestimo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "itens_emprestimo")
@Table(name = "itens_emprestimo")
@Data
@NoArgsConstructor
public class ItemEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long codigo;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Emprestimo.class, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "codigo_emprestimo")
    private Emprestimo emprestimo;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Obra.class, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "codigo_obra")
    private Obra obra;

    private String atendente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SituacaoItemEmprestimo situacao;

    @Column(name = "data_devolucao")
    private LocalDate dataDevolucao;

    @Column(name = "data_realizacao")
    private LocalDate dataRealizacao;

    @Column(name = "data_devolvido")
    private LocalDate dataDevolvido;

    public ItemEmprestimo(Emprestimo emprestimo, Obra obra, String atendente,
                          SituacaoItemEmprestimo situacao, LocalDate dataDevolucao, LocalDate dataRealizacao) {
        this.emprestimo = emprestimo;
        this.obra = obra;
        this.atendente = atendente;
        this.situacao = situacao;
        this.dataDevolucao = dataDevolucao;
        this.dataRealizacao = dataRealizacao;
    }


    public ItemEmprestimo(Obra obra, String atendente,
                          SituacaoItemEmprestimo situacao, LocalDate dataDevolucao, LocalDate dataRealizacao) {
        this.obra = obra;
        this.atendente = atendente;
        this.situacao = situacao;
        this.dataDevolucao = dataDevolucao;
        this.dataRealizacao = dataRealizacao;
    }

    @Override
    public String toString() {
        return "ItemEmprestimo{" +
                "codigo=" + codigo +
                ", atendente='" + atendente + '\'' +
                ", dataDevolucao=" + dataDevolucao +
                ", dataRealizacao=" + dataRealizacao +
                ", dataDevolvido=" + dataDevolvido +
                '}';
    }
}