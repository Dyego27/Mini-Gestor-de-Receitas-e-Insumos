package io.Dyego27.Mini_Gestor.controller.handler;

import io.Dyego27.Mini_Gestor.exception.EstoqueInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<String> handleEstoqueInsuficienteException(EstoqueInsuficienteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
