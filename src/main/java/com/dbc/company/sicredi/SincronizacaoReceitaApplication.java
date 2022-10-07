package com.dbc.company.sicredi;

import com.dbc.company.sicredi.dto.EntradaCsv;
import com.dbc.company.sicredi.dto.SaidaCsv;
import com.dbc.company.sicredi.function.SincronizacaoReceitafunction;
import com.dbc.company.sicredi.service.ReceitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class SincronizacaoReceitaApplication {

    @Autowired
    private ReceitaService receitaService;
    private static ReceitaService receitaServiceStatic;

    public static void main(String[] args) {

        SpringApplication.run(SincronizacaoReceitaApplication.class, args);
        List<EntradaCsv> entradaList = SincronizacaoReceitafunction.entradaProcessamento(args[0]);
        List<SaidaCsv> saidaList = SincronizacaoReceitafunction.saidaProcessamento(entradaList, receitaServiceStatic);
        SincronizacaoReceitafunction.montaArquivoSaida(saidaList);System.exit(0);
    }

    @PostConstruct
    public void init() {
        receitaServiceStatic = receitaService;
    }
}
