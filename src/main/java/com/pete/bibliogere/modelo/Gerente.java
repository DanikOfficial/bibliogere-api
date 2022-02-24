package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity(name = "gerente")
@Table(name = "gerente")
@PrimaryKeyJoinColumn(name = "codigo_gerente")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Gerente extends Utilizador {

    public Gerente(@NotBlank(message = "O utilizador é obrigatório!") @NonNull String username,
                   @NotBlank(message = "Password") String password, Boolean active,
                   @NotBlank(message = "O nome do utilizador é obrigatório!") @NonNull String nome) {
        super(username, password, active, nome);
    }

}
