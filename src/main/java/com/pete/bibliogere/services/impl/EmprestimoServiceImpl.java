package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.dto.EmprestimoReadDTO;
import com.pete.bibliogere.dto.FindBetweenDatesRequest;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.ItemEmprestimo;
import com.pete.bibliogere.modelo.excepcoes.EmprestimoAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.EmprestimoNotFoundException;
import com.pete.bibliogere.repositorios.EmprestimoRepositorio;
import com.pete.bibliogere.services.EmprestimoService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class EmprestimoServiceImpl implements EmprestimoService {

    @Autowired
    private EmprestimoRepositorio repositorio;

    @Autowired
    private ItemEmprestimoService itemEmprestimoService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ReusableEntityOperation reusable;

    @Override
    @Transactional
    public EmprestimoDTO registar(Emprestimo emprestimo) {

        emprestimo.setIsCurrent(Boolean.TRUE);
        emprestimo.setCreatedAt(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(2));
        emprestimo.setMulta(0);
        emprestimo.setTotalObras(0); // Initialize total to 0

        handleExistsCreation(emprestimo);

        Emprestimo savedEmprestimo = repositorio.save(emprestimo);

        // Pass the entire Emprestimo object instead of just the ID
        List<ItemEmprestimo> itens = itemEmprestimoService.registarItens(
                emprestimo.getObrasIds(),
                savedEmprestimo
        );

        savedEmprestimo.setItens(itens);
        // Note: totalObras is already updated inside registarItens method

        return new EmprestimoDTO(savedEmprestimo);
    }

    @Override
    @Transactional
    public EmprestimoDTO atualizar(Map<Object, Object> fields, Long codigoEmprestimo) {

        Emprestimo emprestimo = pesquisarEmprestimoPorCodigo(codigoEmprestimo);

        emprestimo = reusable.buildEntityToUpdate(emprestimo, fields);

        reusable.handleConstraintViolation(emprestimo);

        handleExistsUpdate(emprestimo);

        return new EmprestimoDTO(emprestimo);
    }

    @Override
    @Transactional
    public EmprestimoDTO devolver(Long codigoEmprestimo) {
        Emprestimo emprestimo = pesquisarEmprestimoPorCodigo(codigoEmprestimo);

        itemEmprestimoService.devolverItens(emprestimo);
        // Note: totalObras and isCurrent are already updated inside devolverItens method

        emprestimo.setDataDevolucao(LocalDate.now());

        return new EmprestimoDTO(emprestimo);
    }

    @Override
    public Map<String, Object> pesquisarEmprestimosPaging(String utente, int page) {
        Page<EmprestimoDTO> results = repositorio.pesquisarEmprestimosPaging(
                PageRequest.of(page, 30),
                utente.toUpperCase().trim()
        );

        return reusable.buildDataWithPaging(results, Emprestimo.class);
    }

    @Override
    public Emprestimo pesquisarEmprestimoPorCodigo(Long codigo) {
        return repositorio.findWithItensByCodigoAndIsCurrent(codigo, true)
                .orElseThrow(() -> new EmprestimoNotFoundException(
                        "O codigo do emprestimo digitado não existe "));
    }

    @Override
    public List<EmprestimoReadDTO> pesquisarEmprestimosLikeUtente(String utente) {
        return repositorio.pesquisarEmprestimos(utente);
    }

    @Override
    public EmprestimoDTO findEmprestimoByCodigo(Long codigo) {
        Emprestimo emprestimo = repositorio.findWithItensByCodigoAndIsCurrent(codigo, true)
                .orElseThrow(() -> new EmprestimoNotFoundException(
                        "O codigo do emprestimo digitado não existe "));
        return new EmprestimoDTO(emprestimo);
    }

    @Override
    public EmprestimoDTO findEmprestimoByUtente(String codigoUtente) {
        Emprestimo emprestimo = repositorio.findByUtenteIgnoreCaseAndIsCurrent(codigoUtente, true)
                .orElseThrow(() -> new EmprestimoNotFoundException(
                        "O utente digitado não existe!"));
        return new EmprestimoDTO(emprestimo);
    }

    @Override
    public List<EmprestimoDTO> findEmprestimosBetween(FindBetweenDatesRequest datesDTO) {
        return null;
    }

    // REMOVED: alteraTotal methods - no longer needed
    // The ItemEmprestimoService now updates the Emprestimo directly

    private void handleExistsCreation(Emprestimo emprestimo) {
        final String utente = emprestimo.getUtente().trim();
        final String countQuery = "SELECT COUNT(e) FROM emprestimos e " +
                "WHERE UPPER(e.utente) = UPPER(:utente) AND e.isCurrent = TRUE";

        Long total = (Long) em.createQuery(countQuery)
                .setParameter("utente", utente)
                .getSingleResult();

        final String message = "Já existe um empréstimo registado com informação deste utente";

        if (total.intValue() > 0) {
            throw new EmprestimoAlreadyExistsException(message);
        }
    }

    private void handleExistsUpdate(Emprestimo emprestimo) {
        final String utente = emprestimo.getUtente().trim();
        final String countQuery = "SELECT COUNT(e) FROM emprestimos e " +
                "WHERE (e.codigo <> :codigo) AND (UPPER(e.utente) = UPPER(:utente))";

        Long total = (Long) em.createQuery(countQuery)
                .setParameter("utente", utente)
                .setParameter("codigo", emprestimo.getCodigo())
                .getSingleResult();

        final String message = "Já existe um empréstimo registado com informação deste utente";

        if (total.intValue() > 0) {
            throw new EmprestimoAlreadyExistsException(message);
        }
    }
}