package com.dbc.company.sicredi.function;

import com.dbc.company.sicredi.dto.EntradaCsv;
import com.dbc.company.sicredi.dto.SaidaCsv;
import com.dbc.company.sicredi.exception.EntradaCsvNotFound;
import com.dbc.company.sicredi.service.ReceitaService;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SincronizacaoReceitafunction {

    public static List<EntradaCsv> entradaProcessamento(String nomeArquivo) {

        try {
            Reader reader = Files.newBufferedReader(Paths.get(nomeArquivo));
            List<EntradaCsv> entradaCsv = new CsvToBeanBuilder(reader).withSeparator(';')
                    .withType(EntradaCsv.class).build().parse();

            return entradaCsv;
        } catch (IOException e) {
            throw new EntradaCsvNotFound();
        }
    }

    public static List<SaidaCsv> saidaProcessamento(List<EntradaCsv> entradaCsvList, ReceitaService receitaService) {

        List<SaidaCsv> saidaCsvList = new ArrayList<>();

        entradaCsvList.forEach(x -> {
            final String agencia = x.getAgencia();
            final String conta = x.getConta();
            final String saldo = x.getSaldo();
            final String status = x.getStatus();

            boolean resultado = false;
            try {
                resultado = receitaService.atualizarConta(agencia, conta.replace("-", ""),
                        Double.parseDouble(saldo.replace(",", ".")), status);
            } catch (RuntimeException | InterruptedException e) {
                e.printStackTrace();
            }

            SaidaCsv saidaCsv = new SaidaCsv();
            saidaCsv.setAgencia(agencia);
            saidaCsv.setConta(conta);
            saidaCsv.setSaldo(saldo);
            saidaCsv.setStatus(status);
            saidaCsv.setResultado(resultado);
            saidaCsvList.add(saidaCsv);
        });
        return saidaCsvList;
    }

    public static Boolean montaArquivoSaida(List<SaidaCsv> saidaArquivoList) {

        try {
            Writer writer = new FileWriter("saida.csv");
            ColumnPositionMappingStrategy<SaidaCsv> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(SaidaCsv.class);
            String[] fields = {"agencia", "conta", "saldo", "status", "resultado"};
            strategy.setColumnMapping(fields);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withMappingStrategy(strategy).build();
            beanToCsv.write(saidaArquivoList);
            writer.close();

            Path path = Paths.get("saida.csv");
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            String header = "agencia;conta;saldo;status;resultado";
            lines.add(0, header);
            Files.write(path, lines, StandardCharsets.UTF_8);

        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            e.printStackTrace();
        }
        return true;
    }

}