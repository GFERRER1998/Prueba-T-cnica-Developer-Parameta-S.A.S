package com.prueba.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingParam(
            MissingServletRequestParameterException ex) {

        log.warn("Parámetro faltante: {}", ex.getParameterName());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error("El parámetro '" + ex.getParameterName() + "' es obligatorio."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {

        log.warn("Error de validación: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
        log.error("Error interno no controlado", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("Error interno del servidor: " + ex.getMessage()));
    }

    private Map<String, String> error(String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", message);
        return body;
    }
}
