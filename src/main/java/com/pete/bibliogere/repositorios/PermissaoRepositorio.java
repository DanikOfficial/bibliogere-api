package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissaoRepositorio extends JpaRepository<Permissao, Long> {

    Optional<Permissao> findByNomeIgnoreCase(String nome);

}
