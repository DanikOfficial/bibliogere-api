package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.dto.EmprestimoReadDTO;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.enumeracao.SituacaoItemEmprestimo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmprestimoRepositorio extends JpaRepository<Emprestimo, Long> {

    @Query(value = "SELECT e FROM emprestimos e WHERE (UPPER(e.utente) LIKE %:utente%) AND (e.isCurrent = TRUE)")
    Page<EmprestimoDTO> pesquisarEmprestimosPaging(Pageable page, @Param("utente") String utente);

    @Query(value = "SELECT e FROM emprestimos e WHERE UPPER(e.utente) LIKE UPPER(CONCAT(:utente, '%')) AND e.isCurrent = TRUE")
    List<EmprestimoReadDTO> pesquisarEmprestimos(@Param("utente") String utente);


    @EntityGraph(attributePaths = {"itens", "itens.obra", "itens.obra.estante", "itens.obra.localizacao"})
    Optional<Emprestimo> findWithItensByCodigoAndIsCurrent(Long codigo, boolean isCurrent);

    @EntityGraph(attributePaths = {"itens", "itens.obra"})
    Optional<Emprestimo> findByUtenteIgnoreCaseAndIsCurrent(String codigoUtente, boolean isCurrent);

    @EntityGraph(value = "Emprestimo.completo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT DISTINCT e FROM emprestimos e " +
            "WHERE e.createdAt BETWEEN :dataInicio AND :dataFim " +
            "ORDER BY e.createdAt DESC")
    List<Emprestimo> findByCreatedAtBetween(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);

    @EntityGraph(value = "Emprestimo.completo", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT DISTINCT e FROM emprestimos e " +
            "JOIN e.itens i " +
            "WHERE e.createdAt BETWEEN :dataInicio AND :dataFim " +
            "AND i.situacao = :situacao " +
            "ORDER BY e.createdAt DESC")
    List<Emprestimo> findByCreatedAtBetweenAndItensSituacao(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("situacao") SituacaoItemEmprestimo situacao);
}
