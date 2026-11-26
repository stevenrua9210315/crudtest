package com.project.test.srj.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDbException extends RuntimeException {

    private final String codigoError;
    private final String origen;

    public ErrorDbException(String codigoError, String mensaje, String origen, Throwable cause) {
        super(mensaje, cause);
        this.codigoError = codigoError;
        this.origen = origen;
    }
}
