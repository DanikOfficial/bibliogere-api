package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Utilizador;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilizadorRepositorio extends JpaRepository<Utilizador, Long> {

    @EntityGraph(attributePaths = "permissoes", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Utilizador> findByUsernameIgnoreCase(String name);

}
