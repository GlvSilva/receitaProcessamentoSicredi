package com.dbc.company.sicredi.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EntradaCsv {

    @NotBlank
    @CsvBindByName
    private String agencia;

    @NotBlank
    @CsvBindByName
    private String conta;

    @NotBlank
    @CsvBindByName
    private String saldo;

    @NotBlank
    @CsvBindByName
    private String status;

}
