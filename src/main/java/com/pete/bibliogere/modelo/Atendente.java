package com.pete.bibliogere.modelo;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity(name = "atendentes")
@Table(name = "atendentes")
@PrimaryKeyJoinColumn(name = "codigo_atendente")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Atendente extends Utilizador {

    public Atendente(@NotBlank(message = "O utilizador é obrigatório!") @NonNull String username, String password, Boolean active, @NotBlank(message = "O nome do utilizador é obrigatório!") @NonNull String nome) {
        super(username, password, active, nome);
    }

    public Atendente(@NotBlank(message = "O utilizador é obrigatório!") @NonNull String username, Boolean active, @NotBlank(message = "O nome do utilizador é obrigatório!") @NonNull String nome) {
        super(username, active, nome);
    }

}
