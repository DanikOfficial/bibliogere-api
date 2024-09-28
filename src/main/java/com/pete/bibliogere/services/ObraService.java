package com.pete.bibliogere.services;


import com.pete.bibliogere.modelo.Obra;
import com.pete.bibliogere.modelo.enumeracao.Quantidade;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public interface ObraService {

    Obra registarObra(Obra obra, Long codigoEstante, Long codigoLocalizacao);

    Obra atualizarObra(Map<Object, Object> fields, Long codigoEstante, Long codigoObra,
                       Long codigoLocalizacao) throws MethodArgumentNotValidException;

    Obra apagarObra(Long codigo);

    Obra pesquisarPorCodigo(Long codigoObra);

    List<Obra> pesquisarObras(String titulo);

    Map<String, Object> listarMonografiasPaging(int page);

    Map<String, Object> listarLivrosPaging(int page);

    Map<String, Object> pesquisarMonografias(String titulo, String tutor, int ano, String autor, int page);

    Map<String, Object> pesquisarLivros(String titulo, String editora, String autor, int ano, int page);

    List<Obra> listarObras();

    void alteraQuantidade(Quantidade operacao, Obra obra);

    List<Obra> listarObrasPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

}
