package com.project.test.srj.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClienteNoEncontradoException extends RuntimeException {

    private final String codigoError;
    private final String origen;

    public ClienteNoEncontradoException(String codigoError, String mensaje, String origen, Throwable cause) {
        super(mensaje, cause);
        this.codigoError = codigoError;
        this.origen = origen;
    }
}
