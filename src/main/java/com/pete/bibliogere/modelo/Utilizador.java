package com.pete.bibliogere.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "utilizadores")
@Table(name = "utilizadores")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties(value = {"username", "password", "active", "permissoes"})
@Data
@NoArgsConstructor
public abstract class Utilizador {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long codigo;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O utilizador é obrigatório!")
    @NonNull
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "A senha é obrigatória!")
    private String password;

    private Boolean enabled;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "O nome do utilizador é obrigatório!")
    @NonNull
    private String nome;

    @ManyToMany(targetEntity = Permissao.class, fetch = FetchType.LAZY)
    @JoinTable(name = "utilizadores_permissoes",
            joinColumns = @JoinColumn(name = "codigo_utilizador"), inverseJoinColumns = @JoinColumn(name = "codigo_permissao"))
    private Set<Permissao> permissoes = new HashSet<>();

    @OneToOne(mappedBy = "utilizador", fetch = FetchType.LAZY)
    private QuestoesSeguranca questoesSeguranca;

    public Utilizador(@NotBlank(message = "O utilizador é obrigatório!") @NonNull String username,
                      @NotBlank(message = "Password") String password, Boolean active,
                      @NotBlank(message = "O nome do utilizador é obrigatório!") @NonNull String nome) {
        super();
        this.username = username;
        this.password = password;
        this.enabled = active;
        this.nome = nome;
    }


}
