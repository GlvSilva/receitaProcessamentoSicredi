package com.dbc.company.sicredi.exception;

public class EntradaCsvNotFound extends RuntimeException {

    public EntradaCsvNotFound() {
        super("Arquivo CSV de entrada não foi localizado");
    }
}
