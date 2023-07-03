package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Estante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstanteRepositorio extends JpaRepository<Estante, Long> {

    Optional<Estante> findEstanteByNomeIgnoreCaseAndTipoEstanteIgnoreCase(@Param("nome") String nome,
                                                                          String tipoEstante);

    Optional<Estante> findByNomeIgnoreCaseAndTipoEstanteIgnoreCaseAndCodigoNot(String nome,
                                                                               String tipoEstante, Long codigo);

    Optional<Estante> findByCodigoAndIsDeletedFalse(Long id);
}
