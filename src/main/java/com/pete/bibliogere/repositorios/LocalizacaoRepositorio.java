package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Localizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocalizacaoRepositorio extends JpaRepository<Localizacao, Long> {

    Optional<Localizacao> findByDesignacaoIgnoreCase(String designacao);

    Optional<Localizacao> findByDesignacaoIgnoreCaseAndCodigo(String designacao, Long codigo);

}
