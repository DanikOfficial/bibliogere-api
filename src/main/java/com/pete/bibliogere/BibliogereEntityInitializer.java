package com.pete.bibliogere;

import com.pete.bibliogere.modelo.*;
import com.pete.bibliogere.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BibliogereEntityInitializer {

    @Autowired
    private QuestaoService questaoService;

    @Autowired
    private TipoEstanteService tipoEstanteService;

    @Autowired
    private LocalizacaoService localizacaoService;

    @Autowired
    private PermissaoService permissaoService;

    @Autowired
    private UtilizadorService utilizadorService;

    @Bean
    CommandLineRunner commandLineRunner() {
        return service -> {

            // Adiciona questões de sergurança
            this.inicializaQuestoes();

            // Adiciona tipos de estantes
            this.inicializaTipoEstantes();

            // Adiciona Localizacoes
            this.inicializaLocalizacoes();

            // Inicializa Permissoes
            this.inicializaPermissoesEAdmin();

        };
    }


    public void inicializaQuestoes() {

        // Verifica se já existe alguma questão
        if (questaoService.listarQuestoes().isEmpty()) {

            Questao questao1 = new Questao("Em que cidade você nasceu?");
            Questao questao2 = new Questao("Qual é o nome da sua primeira escola?");
            Questao questao3 = new Questao("Qual é o seu nome da infância?");
            Questao questao4 = new Questao("Qual é o nome do seu prato favorito?");
            Questao questao5 = new Questao("Qual é o seu animal favorito?");

            List<Questao> questoes = Arrays.asList(questao1, questao2, questao3, questao4, questao5);

            questaoService.registaQuestoes(questoes);
        }

    }

    public void inicializaTipoEstantes() {

        // Verifica se já existe algum tipo de estante inserido
        if (tipoEstanteService.listarTiposEstantes().isEmpty()) {

            TipoEstante tipoEstante1 = new TipoEstante("Monografia", Boolean.FALSE, Boolean.FALSE);
            TipoEstante tipoEstante2 = new TipoEstante("Livro", Boolean.TRUE, Boolean.FALSE);

            tipoEstanteService.registarEstantes(Arrays.asList(tipoEstante1, tipoEstante2));

        }
    }

    public void inicializaLocalizacoes() {

        // Verifica se já existe alguma localização
        if (localizacaoService.listarLocalizacoes().isEmpty()) {

            Localizacao localizacao1 = new Localizacao("Primeiro Andar - Primeira Coluna");
            Localizacao localizacao2 = new Localizacao("Primeiro Andar - Segunda Coluna");
            Localizacao localizacao3 = new Localizacao("Segundo Andar - Primeira Coluna");
            Localizacao localizacao4 = new Localizacao("Segundo Andar - Segunda Coluna");
            Localizacao localizacao5 = new Localizacao("Terceiro Andar - Primeira Coluna");
            Localizacao localizacao6 = new Localizacao("Terceiro Andar - Segunda Coluna");

            List<Localizacao> localizacoes = Arrays.asList(localizacao1, localizacao2, localizacao3, localizacao4,
                    localizacao5, localizacao6);

            localizacaoService.registarLocalizacoes(localizacoes);

        }
    }

    public void inicializaPermissoesEAdmin() {

        if (permissaoService.listarTodasPermissoes().isEmpty()) {

            Permissao admin = new Permissao("ROLE_ADMIN");

            Permissao atendente = new Permissao("ROLE_ATENDENTE");

            List<Permissao> permissoes = Arrays.asList(admin,atendente);

            permissaoService.inicializarPermissoes(permissoes);

            Gerente gerente = new Gerente("admin", "admin", Boolean.TRUE, "Administrador");

            Permissao permissaoGerente = permissaoService.pesquisarPermissaoPorNome("ROLE_ADMIN");

            gerente.getPermissoes().add(permissaoGerente);

            utilizadorService.criaAdmin(gerente);

        }

    }

}
