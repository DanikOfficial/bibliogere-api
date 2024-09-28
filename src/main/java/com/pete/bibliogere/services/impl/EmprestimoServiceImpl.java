package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.EmprestimoDTO;
import com.pete.bibliogere.dto.EmprestimoReadDTO;
import com.pete.bibliogere.dto.FindBetweenDatesRequest;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.ItemEmprestimo;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
    public EmprestimoDTO registar(Emprestimo emprestimo) {

        emprestimo.setIsCurrent(Boolean.TRUE);
        emprestimo.setCreatedAt(LocalDate.now());
        emprestimo.setDataDevolucao(LocalDate.now().plusDays(2));
        emprestimo.setMulta(0);

        handleExistsCreation(emprestimo);

        Emprestimo savedEmprestimo = repositorio.save(emprestimo);

        List<ItemEmprestimo> itens = itemEmprestimoService.registarItens(emprestimo.getObrasIds(), savedEmprestimo.getCodigo());

        savedEmprestimo.setItens(itens);

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

        emprestimo.setTotalObras(0);
        emprestimo.setIsCurrent(Boolean.FALSE);
        emprestimo.setDataDevolucao(LocalDate.now());

        return new EmprestimoDTO(emprestimo);
    }

    @Override
    public Map<String, Object> pesquisarEmprestimosPaging(String utente, int page) {
        Page<EmprestimoDTO> results = repositorio.pesquisarEmprestimosPaging(PageRequest.of(page, 30), utente.toUpperCase().trim());

        return reusable.buildDataWithPaging(results, Emprestimo.class);
    }

    @Override
    public Emprestimo pesquisarEmprestimoPorCodigo(Long codigo) {
        return repositorio.findWithItensByCodigoAndIsCurrent(codigo, true).orElseThrow(() -> new EmprestimoNotFoundException("O codigo do emprestimo digitado não existe "));
    }

    @Override
    public List<EmprestimoReadDTO> pesquisarEmprestimosLikeUtente(String utente) {
        return repositorio.pesquisarEmprestimos(utente);
    }

    @Override
    public EmprestimoDTO findEmprestimoByCodigo(Long codigo) {
        Emprestimo emprestimo = repositorio.findWithItensByCodigoAndIsCurrent(codigo, true).orElseThrow(() -> new EmprestimoNotFoundException("O codigo do emprestimo digitado não existe "));
        return new EmprestimoDTO((emprestimo));
    }

    @Override
    public EmprestimoDTO findEmprestimoByUtente(String codigoUtente) {
        Emprestimo emprestimo = repositorio.findByUtenteIgnoreCaseAndIsCurrent(codigoUtente, true).orElseThrow(() -> new EmprestimoNotFoundException("O utente digitado não existe!"));
        return new EmprestimoDTO(emprestimo);
    }

    @Override
    public List<EmprestimoDTO> findEmprestimosBetween(FindBetweenDatesRequest datesDTO) {
        return null;
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
        final String countQuery = "SELECT COUNT(e) FROM emprestimos e " + "WHERE UPPER(e.utente) = UPPER(:utente) AND e.isCurrent = TRUE";

        Long total = (Long) em.createQuery(countQuery).setParameter("utente", utente).getSingleResult();

        final String message = "Já existe um empréstimo registado com informação deste utente";

        if (total.intValue() > 0) throw new EmprestimoAlreadyExistsException(message);
    }

    private void handleExistsUpdate(Emprestimo emprestimo) {
        final String utente = emprestimo.getUtente().trim();
        final String countQuery = "SELECT COUNT(e) FROM emprestimos e WHERE (e.codigo <> :codigo) AND (UPPER(e.utente) = UPPER(:utente))";


        Long total = (Long) em.createQuery(countQuery).setParameter("utente", utente).setParameter("codigo", emprestimo.getCodigo()).getSingleResult();

        final String message = "Já existe um empréstimo registado com informação deste utente";
        if (total.intValue() > 0) throw new EmprestimoAlreadyExistsException(message);
    }
}
