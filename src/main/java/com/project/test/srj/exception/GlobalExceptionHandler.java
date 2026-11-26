package com.project.test.srj.exception;

import com.project.test.srj.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
        }

        String detalle = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("; "));

        ApiErrorResponse response = new ApiErrorResponse(
                "VAL_400",
                detalle.isBlank() ? "Error de validación en los datos enviados" : detalle,
                "MethodArgumentNotValidException",
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ErrorDbException.class)
    public ResponseEntity<ApiErrorResponse> handleSinConexionDb(ErrorDbException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCodigoError(),
                ex.getMessage(),
                ex.getOrigen(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<ApiErrorResponse> handleCannotCreateTransaction(CannotCreateTransactionException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                "DB_001",
                "No fue posible conectar con la base de datos. Verifica que SQL Server esté activo.",
                "TransactionManager / HikariCP",
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleClienteNoEncontrado(ClienteNoEncontradoException ex) {
        ApiErrorResponse response = new ApiErrorResponse(
                ex.getCodigoError(),
                ex.getMessage(),
                ex.getOrigen(),
                Instant.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}