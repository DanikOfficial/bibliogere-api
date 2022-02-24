package com.pete.bibliogere.repositorios;

import com.pete.bibliogere.modelo.Livro;
import com.pete.bibliogere.modelo.Monografia;
import com.pete.bibliogere.modelo.Obra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ObraRepositorio extends JpaRepository<Obra, Long> {

    Optional<Obra> findObraByAutorIgnoreCaseAndTituloIgnoreCaseAndTipoObraIgnoreCase(String autor,
                                                                                     String titulo,
                                                                                     String tipoObra);


    Optional<Obra> findObraByAutorIgnoreCaseAndTituloIgnoreCaseAndTipoObraIgnoreCaseAndCodigoNot(
            String autor, String titulo,
            String tipoObra, Long codigo
    );

    @EntityGraph(attributePaths = {"estante", "localizacao"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Obra> findByCodigo(Long codigo);

    @Query(value = "SELECT m FROM monografias m WHERE m.isDeleted = FALSE")
    @EntityGraph(attributePaths = {"estante", "localizacao"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Monografia> findAllMonografias(Pageable page);

    @Query(value = "SELECT l FROM livros l WHERE l.isDeleted = FALSE")
    @EntityGraph(attributePaths = {"estante", "localizacao"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Livro> findAllLivros(Pageable page);

    @Query(value = "SELECT m FROM monografias m " +
            "WHERE (UPPER(m.titulo) LIKE %:titulo%) OR " +
            "(UPPER(m.autor) LIKE %:autor%) OR " +
            "(UPPER(m.tutor) LIKE %:tutor%) OR " +
            "(m.ano = :ano) AND " +
            "(m.isDeleted = FALSE)")
    Page<Monografia> listaMonografiasPorCriterio(Pageable page, @Param("titulo") String titulo,
                                                 @Param("autor") String autor, @Param("tutor") String tutor,
                                                 @Param("ano") int ano);

    @Query(value = "SELECT l FROM livros l " +
            "WHERE (UPPER(l.titulo) LIKE %:titulo%) OR " +
            "(UPPER(l.autor) LIKE %:autor%) OR " +
            "(UPPER(l.editora) LIKE %:editora%) OR " +
            "(l.ano = :ano) AND " +
            "(l.isDeleted = FALSE)", nativeQuery = false)
    @EntityGraph(attributePaths = {"estante", "localizacao"}, type = EntityGraph.EntityGraphType.FETCH)
    Page<Livro> listaLivrosPorCriterio(Pageable page, @Param("titulo") String titulo, @Param("autor") String autor,
                                       @Param("editora") String editora, @Param("ano") int ano);


}
