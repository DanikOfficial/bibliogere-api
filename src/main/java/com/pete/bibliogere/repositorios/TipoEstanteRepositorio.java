package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.TipoEstante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TipoEstanteRepositorio extends JpaRepository<TipoEstante, Long> {

    //@Query(name = "SELECT tp FROM tipo_estantes tp WHERE UPPER(tp.designacao) = :designacao")
    Optional<TipoEstante> findByDesignacaoIgnoreCase(@Param("designacao") String designacao);

}
