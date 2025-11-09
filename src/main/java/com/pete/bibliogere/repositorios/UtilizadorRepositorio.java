package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Atendente;
import com.pete.bibliogere.modelo.Utilizador;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtilizadorRepositorio extends JpaRepository<Utilizador, Long> {

    @EntityGraph(attributePaths = {"permissoes", "questoesSeguranca"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Utilizador> findByUsernameIgnoreCase(String name);

    @EntityGraph(attributePaths = "questoesSeguranca", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Utilizador> findByCodigo(Long codigo);


    @Query("SELECT a FROM atendentes a ")
    List<Atendente> findAllAtendentes();
}
