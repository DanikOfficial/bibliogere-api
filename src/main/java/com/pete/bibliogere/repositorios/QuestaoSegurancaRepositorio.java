package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.QuestoesSeguranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoSegurancaRepositorio extends JpaRepository<QuestoesSeguranca, Long> {

}
