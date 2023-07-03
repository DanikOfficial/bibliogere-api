package com.pete.bibliogere.modelo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity(name = "atendentes")
@Table(name = "atendentes")
@PrimaryKeyJoinColumn(name = "codigo_atendente")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Atendente extends Utilizador {

    public Atendente(@NotBlank(message = "O utilizador é obrigatório!") @NonNull String username,
                     @NotBlank(message = "Password") String password, Boolean active,
                     @NotBlank(message = "O nome do utilizador é obrigatório!") @NonNull String nome) {
        super(username, password, active, nome);
    }

}
