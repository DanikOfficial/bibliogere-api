package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.ItemEmprestimo;
import com.pete.bibliogere.modelo.enumeracao.SituacaoItemEmprestimo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemEmprestimoRepositorio extends JpaRepository<ItemEmprestimo, Long> {


    @EntityGraph(attributePaths = {"obra", "emprestimo"}, type = EntityGraph.EntityGraphType.FETCH)
    List<ItemEmprestimo> findByObraCodigoInAndEmprestimoCodigoAndSituacao(List<Long> codigos, Long codigoEmprestimo,
                                                                          SituacaoItemEmprestimo situacaoItem);

}
