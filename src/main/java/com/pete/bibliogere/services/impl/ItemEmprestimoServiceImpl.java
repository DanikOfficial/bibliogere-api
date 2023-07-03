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
import com.pete.bibliogere.services.EmprestimoService;
import com.pete.bibliogere.services.ObraService;
import com.pete.bibliogere.services.impl.ItemEmprestimoService;
import com.pete.bibliogere.utils.ReusableEntityOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private ReusableEntityOperation reusable;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public List<ItemEmprestimoDTO> registarItens(Long[] codigosObras, Long codigoEmprestimo) {

        Emprestimo emprestimo = emprestimoService.pesquisarEmprestimoPorCodigo(codigoEmprestimo);

        List<Obra> obras = Arrays.asList(codigosObras)
                .stream()
                .map(codigoObra -> obraService.pesquisarPorCodigo(codigoObra))
                .collect(Collectors.toList());

        List<ItemEmprestimo> itensEmprestimo = obras.parallelStream()
                .map(obra -> new ItemEmprestimo(emprestimo, obra, "", SituacaoItemEmprestimo.Activo,
                        LocalDate.now().plusDays(2),
                        LocalDate.now()))
                .collect(Collectors.toList());


        handleItensExists(Arrays.asList(codigosObras), codigoEmprestimo);

        List<ItemEmprestimo> itensGravados = repositorio.saveAll(itensEmprestimo);

        obras.forEach(obra -> {
            handleObraQuantidade(Quantidade.DIMINUIR, obra);
        });

        handleEmprestimoQuantidade(Quantidade.AUMENTAR, emprestimo, obras.size());

        ItemEmprestimo[] itens1 = itensGravados.stream().toArray(ItemEmprestimo[]::new);

        ItemEmprestimo[] itens2 = itensEmprestimo.stream().toArray(ItemEmprestimo[]::new);

        for (int i = 0; i < itens2.length; i++) {
            itens2[i].setEmprestimo(emprestimo);
            itens2[i].setObra(itens1[i].getObra());
        }

        List<ItemEmprestimoDTO> itens = Arrays.asList(itens2).parallelStream().map(ItemEmprestimoDTO::new).collect(
                Collectors.toList());

        return itens;
    }
//
//    @Override
//    @Transactional
//    public List<ItemEmprestimo> registarItens(Long[] codigosObras, Long codigoEmprestimo) {
//
//        Emprestimo emprestimo = emprestimoService.pesquisarEmprestimoPorCodigo(codigoEmprestimo);
//
//
//        List<Obra> obras = Arrays.asList(codigosObras)
//                .stream()
//                .map(codigoObra -> obraService.pesquisarPorCodigo(codigoObra))
//                .collect(Collectors.toList());
//
//        List<ItemEmprestimo> itensEmprestimo = obras.parallelStream()
//                .map(obra -> new ItemEmprestimo(emprestimo, obra, "", SituacaoItemEmprestimo.Activo,
//                        LocalDate.now().plusDays(2),
//                        LocalDate.now()))
//                .collect(Collectors.toList());
//
//        handleItensExists(Arrays.asList(codigosObras), codigoEmprestimo);
//
//        List<ItemEmprestimo> itensGravados = repositorio.saveAll(itensEmprestimo);
//
//        obras.forEach(obra -> {
//            handleObraQuantidade(Quantidade.DIMINUIR, obra);
//        });
//
//        handleEmprestimoQuantidade(Quantidade.AUMENTAR, emprestimo, obras.size());
//
//        ItemEmprestimo[] itens1 = itensGravados.stream().toArray(ItemEmprestimo[]::new);
//
//        ItemEmprestimo[] itens2 = itensEmprestimo.stream().toArray(ItemEmprestimo[]::new);
//
//        for (int i = 0; i < itens2.length; i++) {
//            itens2[i].setEmprestimo(emprestimo);
//            itens2[i].setObra(itens1[i].getObra());
//        }
//
//
//        return Arrays.asList(itens2);
//    }

    @Override
    public List<ItemEmprestimoDTO> getEmprestimoItems(Long codigoEmprestimo) {
//        final String query = "SELECT i FROM itens_emprestimo i LEFT JOIN FETCH i.emprestimo LEFT JOIN FETCH i.obra WHERE i.emprestimo.codigo = :codigoEmprestimo";

        final String query = "SELECT i FROM itens_emprestimo i " +
                "LEFT JOIN FETCH i.emprestimo ie " +
                "LEFT JOIN FETCH i.obra io " +
                "LEFT JOIN FETCH io.localizacao " +
                "LEFT JOIN FETCH io.estante WHERE ie.codigo = :codigoEmprestimo AND i.situacao.situacao = 'Activo'";

        List<ItemEmprestimo> itens = em.createQuery(query, ItemEmprestimo.class)
                .setParameter("codigoEmprestimo", codigoEmprestimo)
                .getResultList();

        List<ItemEmprestimoDTO> itensDTO = itens.parallelStream().map(ItemEmprestimoDTO::new).collect(
                Collectors.toList());

        return itensDTO;
    }

//
//    @Override
//    @Transactional
//    public ItemEmprestimoDTO registarItem(Long codigoObra, Long codigoEmprestimo) {
//
//        Emprestimo emprestimo = emprestimoService.pesquisarEmprestimoPorCodigo(codigoEmprestimo);
//
//        Obra obra = obraService.pesquisarPorCodigo(codigoObra);
//
//        ItemEmprestimo itemEmprestimo = new ItemEmprestimo(emprestimo, obra, "", SituacaoItemEmprestimo.Activo,
//                LocalDate.now().plusDays(2),
//                LocalDate.now());
//
//        handleObraQuantidade(Quantidade.DIMINUIR, obra);
//
//        handleEmprestimoQuantidade(Quantidade.AUMENTAR, emprestimo, 1);
//
//        ItemEmprestimo itemGravado = repositorio.save(itemEmprestimo);
//
//        itemEmprestimo.setCodigo(itemGravado.getCodigo());
//
//        return Optional.of(itemEmprestimo).map(ItemEmprestimoDTO::new).get();
//    }

    @Override
    @Transactional
    public Long devolverItem(Long codigo) {

        ItemEmprestimo itemEmprestimo = pesquisaItemPorCodigo(codigo);

        itemEmprestimo.setSituacao(SituacaoItemEmprestimo.Devolvido);

        itemEmprestimo.setDataDevolvido(LocalDate.now());

        handleObraQuantidade(Quantidade.AUMENTAR, itemEmprestimo.getObra());

        handleEmprestimoQuantidade(Quantidade.DIMINUIR, itemEmprestimo.getEmprestimo(), 1);

        return itemEmprestimo.getCodigo();
    }

    private ItemEmprestimo pesquisaItemPorCodigo(Long codigo) {
        ItemEmprestimo item;
        //final String query = "SELECT i FROM itens_emprestimo i LEFT JOIN FETCH i.emprestimo LEFT JOIN FETCH i.obra WHERE i.codigo = :codigo";

        final String query = "SELECT i FROM itens_emprestimo i " +
                "LEFT JOIN FETCH i.emprestimo " +
                "LEFT JOIN FETCH i.obra io " +
                "LEFT JOIN FETCH io.localizacao " +
                "LEFT JOIN FETCH io.estante WHERE i.codigo = :codigo";

        try {
            item = em.createQuery(query, ItemEmprestimo.class).setParameter("codigo", codigo).getSingleResult();
            return item;
        } catch (NoResultException ex) {
            throw new ItemEmprestimoNotFoundException("O item digitado não existe!");
        }
    }

    @Override
    @Transactional
    public List<Long> devolverItens(Long codigoEmprestimo) {

        Emprestimo emprestimo = emprestimoService.pesquisarEmprestimoPorCodigo(codigoEmprestimo);

        final String query = "SELECT i FROM itens_emprestimo i " +
                "LEFT JOIN FETCH i.emprestimo ie " +
                "LEFT JOIN FETCH i.obra " +
                "WHERE ie.codigo = :codigo AND i.situacao.situacao = 'Activo'";

        List<ItemEmprestimo> itens = em.createQuery(query, ItemEmprestimo.class)
                .setParameter("codigo", codigoEmprestimo)
                .getResultList();

        List<Long> ids = itens.stream().map(ItemEmprestimo::getCodigo).collect(Collectors.toList());

        itens.forEach(item -> {
            handleObraQuantidade(Quantidade.AUMENTAR, item.getObra());
            item.setSituacao(SituacaoItemEmprestimo.Devolvido);
            item.setDataDevolvido(LocalDate.now());
        });

        handleEmprestimoQuantidade(Quantidade.DIMINUIR, emprestimo, itens.size());

        return ids;
    }

    private void handleEmprestimoQuantidade(Quantidade operacao, Emprestimo emprestimo, int quantidade) {
        emprestimoService.alteraTotal(operacao, emprestimo, quantidade);
    }

    private void handleObraQuantidade(Quantidade operacao, Obra obra) {
        obraService.alteraQuantidade(operacao, obra);
    }

    private void handleItensExists(List<Long> ids, Long codigoEmprestimo) {
        List<ItemEmprestimo> itens = repositorio.findByObraCodigoInAndEmprestimoCodigoAndSituacao(ids, codigoEmprestimo,
                SituacaoItemEmprestimo.Activo);

        if (!itens.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            itens.forEach(item -> sb.append("<b>Titulo</b>: " + item.getObra().getTitulo() + "</ br>"));

            throw new ItemEmprestimoAlreadyExistsException(
                    "O Utente já tem um empréstimo activo com a(s) obra(s) escolhida(s):</ br>" + sb);

        }
    }

}
