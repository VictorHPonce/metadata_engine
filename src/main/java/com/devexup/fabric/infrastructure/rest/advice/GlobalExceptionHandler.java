package com.devexup.fabric.infrastructure.rest.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception e) {
        e.printStackTrace(); // Esto imprime el error real en tu consola
        return ResponseEntity.internalServerError()
                .body(Map.of(
                    "error", e.getMessage(),
                    "type", e.getClass().getSimpleName()
                ));
    }
}