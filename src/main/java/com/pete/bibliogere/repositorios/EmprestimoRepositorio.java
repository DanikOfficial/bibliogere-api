package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.modelo.Emprestimo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmprestimoRepositorio extends JpaRepository<Emprestimo, Long> {

    Optional<Emprestimo> findByUtenteIgnoreCase(String utente);

    Optional<Emprestimo> findByUtenteIgnoreCaseAndCodigoNot(String utente, Long codigo);

    @Query(value = "SELECT e FROM emprestimos e WHERE (UPPER(e.utente) LIKE %:utente%) AND (e.isCurrent = TRUE)")
    Page<EmprestimoDTO> pesquisarEmprestimosPaging(Pageable page, @Param("utente") String utente);
}
