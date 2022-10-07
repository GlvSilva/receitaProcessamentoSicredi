package com.dbc.company.sicredi.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorObjectReturn {

    private String nameApplication;
    private String trace;

}
