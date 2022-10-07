package com.dbc.company.sicredi.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaidaCsv extends EntradaCsv {

    @CsvBindByName
    private Boolean resultado;
}
