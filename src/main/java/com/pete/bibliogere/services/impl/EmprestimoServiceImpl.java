package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import com.pete.bibliogere.modelo.excepcoes.EmprestimoAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.EmprestimoNotFoundException;
import com.pete.bibliogere.repositorios.EmprestimoRepositorio;
import com.pete.bibliogere.services.EmprestimoService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    @Autowired
    private EmprestimoRepositorio repositorio;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReusableEntityOperation reusable;

    @Override
    public EmprestimoDTO registar(Emprestimo emprestimo) {

        emprestimo.setIsCurrent(Boolean.TRUE);

        handleExistsCreation(emprestimo);

        return Optional.of(repositorio.save(emprestimo)).map(EmprestimoDTO::new).get();
    }

    @Override
    @Transactional
    public EmprestimoDTO atualizar(Map<Object, Object> fields,
                                   Long codigoEmprestimo) {

        Emprestimo emprestimo = pesquisarEmprestimoPorCodigo(codigoEmprestimo);

        emprestimo = reusable.buildEntityToUpdate(emprestimo, fields);

        reusable.handleConstraintViolation(emprestimo);

        handleExistsUpdate(emprestimo);

        return Optional.of(emprestimo).map(EmprestimoDTO::new).get();
    }

    @Override
    @Transactional
    public EmprestimoDTO devolver(Long codigoEmprestimo) {
        Emprestimo emprestimo = pesquisarEmprestimoPorCodigo(codigoEmprestimo);

        emprestimo.setTotalObras(0);
        emprestimo.setIsCurrent(Boolean.FALSE);

        Query query = em.createNativeQuery("UPDATE itens_emprestimo " +
                "SET situacao = 'Devolvido' " +
                "WHERE codigo_emprestimo = :codigoEmprestimo AND situacao = 'Activo' ");
        query.setParameter("codigoEmprestimo", codigoEmprestimo);
        query.executeUpdate();

        return Optional.of(emprestimo).map(EmprestimoDTO::new).get();
    }

    @Override
    public Map<String, Object> pesquisarEmprestimosPaging(String utente, int page) {
        Page<EmprestimoDTO> results = repositorio.pesquisarEmprestimosPaging(PageRequest.of(page, 30),
                utente.toUpperCase().trim());

        return reusable.buildDataWithPaging(results, Emprestimo.class);
    }

    @Override
    public Emprestimo pesquisarEmprestimoPorCodigo(Long codigo) {
        Emprestimo emprestimo;

        try {
            TypedQuery<Emprestimo> typedQuery = em.createQuery("SELECT e FROM emprestimos e WHERE e.codigo = :codigo",
                    Emprestimo.class);
            typedQuery.setParameter("codigo", codigo);
            emprestimo = typedQuery.getSingleResult();

            return emprestimo;
        } catch (NoResultException ex) {
            throw new EmprestimoNotFoundException("O empréstimo digitado não existe!");
        }

    }

    @Override
    public EmprestimoDTO findEmprestimoByCodigo(Long codigo) {
        return Optional.of(pesquisarEmprestimoPorCodigo(codigo)).map(EmprestimoDTO::new).get();
    }

    @Override
    public void alteraTotal(Quantidade operacao, Emprestimo emprestimo) {

        switch (operacao) {
            case AUMENTAR:
                emprestimo.setTotalObras(emprestimo.getTotalObras() + 1);
                break;
            case DIMINUIR:
                emprestimo.setTotalObras(emprestimo.getTotalObras() - 1);
                handleSituacao(emprestimo);
                break;
            default:
                break;
        }

    }

    @Override
    public void alteraTotal(Quantidade operacao, Emprestimo emprestimo, int quantidade) {
        switch (operacao) {
            case AUMENTAR:
                emprestimo.setTotalObras(emprestimo.getTotalObras() + quantidade);
                break;
            case DIMINUIR:
                emprestimo.setTotalObras(emprestimo.getTotalObras() - quantidade);
                handleSituacao(emprestimo);
                break;
            default:
                break;
        }
    }

    private void handleSituacao(Emprestimo emprestimo) {
        if (emprestimo.getTotalObras() <= 0) emprestimo.setIsCurrent(Boolean.FALSE);
    }


    private void handleExistsCreation(Emprestimo emprestimo) {
        final String utente = emprestimo.getUtente().trim();
        final String countQuery = "SELECT COUNT(e) FROM emprestimos e " +
                "WHERE UPPER(e.utente) = UPPER(:utente) AND e.isCurrent = TRUE";

        Long total = (Long) em.createQuery(countQuery)
                .setParameter("utente", utente)
                .getSingleResult();

        final String message = "Já existe um empréstimo registado com informação deste utente";

        if (total.intValue() > 0) throw new EmprestimoAlreadyExistsException(message);
    }

    private void handleExistsUpdate(Emprestimo emprestimo) {
        final String utente = emprestimo.getUtente().trim();
        final String countQuery = "SELECT COUNT(e) FROM emprestimos e WHERE (e.codigo <> :codigo) AND (UPPER(e.utente) = UPPER(:utente))";


        Long total = (Long) em.createQuery(countQuery)
                .setParameter("utente", utente)
                .setParameter("codigo", emprestimo.getCodigo())
                .getSingleResult();

        final String message = "Já existe um empréstimo registado com informação deste utente";
        if (total.intValue() > 0) throw new EmprestimoAlreadyExistsException(message);


    }

}
