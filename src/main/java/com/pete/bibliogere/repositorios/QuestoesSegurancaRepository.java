package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.QuestoesSeguranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestoesSegurancaRepository extends JpaRepository<QuestoesSeguranca, Long> {
    Optional<QuestoesSeguranca> findByUtilizadorCodigo(Long codigoUtilizador);

}
