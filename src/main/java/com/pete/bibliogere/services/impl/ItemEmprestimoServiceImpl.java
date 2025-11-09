package com.pete.bibliogere.services.impl;

import com.pete.bibliogere.dto.ItemEmprestimoDTO;
import com.pete.bibliogere.modelo.Emprestimo;
import com.pete.bibliogere.modelo.ItemEmprestimo;
import com.pete.bibliogere.modelo.Obra;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import com.pete.bibliogere.modelo.enumeracao.SituacaoItemEmprestimo;
import com.pete.bibliogere.modelo.excepcoes.ItemEmprestimoAlreadyExistsException;
import com.pete.bibliogere.modelo.excepcoes.ItemEmprestimoNotFoundException;
import com.pete.bibliogere.repositorios.ItemEmprestimoRepositorio;
import com.pete.bibliogere.services.ObraService;
import com.pete.bibliogere.services.impl.ItemEmprestimoService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemEmprestimoServiceImpl implements ItemEmprestimoService {

    @Autowired
    private ItemEmprestimoRepositorio repositorio;

    @Autowired
    private ObraService obraService;

    // REMOVED: EmprestimoService dependency - this fixes the circular dependency

    @Autowired
    private ReusableEntityOperation reusable;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<ItemEmprestimo> registarItens(Long[] codigosObras, Emprestimo emprestimo) {
        // Changed: Accept Emprestimo object instead of Long codigoEmprestimo

        List<Obra> obras = Arrays.stream(codigosObras)
                .map(obraService::pesquisarPorCodigo)
                .collect(Collectors.toList());

        handleItensExists(Arrays.asList(codigosObras), emprestimo.getCodigo());

        obras.forEach(obra -> handleObraQuantidade(Quantidade.DIMINUIR, obra));

        // Update emprestimo total directly
        emprestimo.setTotalObras(emprestimo.getTotalObras() + obras.size());

        // Create ItemEmprestimo and save
        List<ItemEmprestimo> itensEmprestimo = obras.stream()
                .map(obra -> new ItemEmprestimo(emprestimo, obra, "", SituacaoItemEmprestimo.Activo,
                        LocalDate.now().plusDays(2), LocalDate.now()))
                .collect(Collectors.toList());

        List<ItemEmprestimo> itensGravados = repositorio.saveAll(itensEmprestimo);

        return itensGravados;
    }

    @Override
    public List<ItemEmprestimoDTO> getEmprestimoItems(Long codigoEmprestimo) {
        final String query = "SELECT i FROM itens_emprestimo i " +
                "LEFT JOIN FETCH i.emprestimo ie " +
                "LEFT JOIN FETCH i.obra io " +
                "LEFT JOIN FETCH io.localizacao " +
                "LEFT JOIN FETCH io.estante WHERE ie.codigo = :codigoEmprestimo AND i.situacao.situacao = 'Activo'";

        List<ItemEmprestimo> itens = em.createQuery(query, ItemEmprestimo.class)
                .setParameter("codigoEmprestimo", codigoEmprestimo)
                .getResultList();

        List<ItemEmprestimoDTO> itensDTO = itens.parallelStream()
                .map(ItemEmprestimoDTO::new)
                .collect(Collectors.toList());

        return itensDTO;
    }

    @Override
    @Transactional
    public Long devolverItem(Long codigo) {

        ItemEmprestimo itemEmprestimo = pesquisaItemPorCodigo(codigo);

        itemEmprestimo.setSituacao(SituacaoItemEmprestimo.Devolvido);
        itemEmprestimo.setDataDevolvido(LocalDate.now());

        handleObraQuantidade(Quantidade.AUMENTAR, itemEmprestimo.getObra());

        // Update emprestimo total directly
        Emprestimo emprestimo = itemEmprestimo.getEmprestimo();
        emprestimo.setTotalObras(emprestimo.getTotalObras() - 1);

        // Update isCurrent status if no more items
        if (emprestimo.getTotalObras() <= 0) {
            emprestimo.setIsCurrent(Boolean.FALSE);
        }

        return itemEmprestimo.getCodigo();
    }

    private ItemEmprestimo pesquisaItemPorCodigo(Long codigo) {
        ItemEmprestimo item;
        final String query = "SELECT i FROM itens_emprestimo i " +
                "LEFT JOIN FETCH i.emprestimo " +
                "LEFT JOIN FETCH i.obra io " +
                "LEFT JOIN FETCH io.localizacao " +
                "LEFT JOIN FETCH io.estante WHERE i.codigo = :codigo";

        try {
            item = em.createQuery(query, ItemEmprestimo.class)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return item;
        } catch (NoResultException ex) {
            throw new ItemEmprestimoNotFoundException("O item digitado não existe!");
        }
    }

    @Override
    public List<Long> devolverItens(Emprestimo emprestimo) {
        List<ItemEmprestimo> itens = emprestimo.getItens();

        List<Long> ids = itens.stream()
                .map(ItemEmprestimo::getCodigo)
                .collect(Collectors.toList());

        itens.forEach(item -> {
            handleObraQuantidade(Quantidade.AUMENTAR, item.getObra());
            item.setSituacao(SituacaoItemEmprestimo.Devolvido);
            item.setDataDevolvido(LocalDate.now());
        });

        // Update emprestimo total directly
        emprestimo.setTotalObras(emprestimo.getTotalObras() - itens.size());

        // Update isCurrent status if no more items
        if (emprestimo.getTotalObras() <= 0) {
            emprestimo.setIsCurrent(Boolean.FALSE);
        }

        return ids;
    }

    private void handleObraQuantidade(Quantidade operacao, Obra obra) {
        obraService.alteraQuantidade(operacao, obra);
    }

    private void handleItensExists(List<Long> ids, Long codigoEmprestimo) {
        List<ItemEmprestimo> itens = repositorio.findByObraCodigoInAndEmprestimoCodigoAndSituacao(
                ids,
                codigoEmprestimo,
                SituacaoItemEmprestimo.Activo
        );

        if (!itens.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            itens.forEach(item -> sb.append("<b>Titulo</b>: ")
                    .append(item.getObra().getTitulo())
                    .append("</ br>"));

            throw new ItemEmprestimoAlreadyExistsException(
                    "O Utente já tem um empréstimo activo com a(s) obra(s) escolhida(s):</ br>" + sb);
        }
    }
}