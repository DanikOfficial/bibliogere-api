package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestaoRepositorio extends JpaRepository<Questao, Long> {

}
