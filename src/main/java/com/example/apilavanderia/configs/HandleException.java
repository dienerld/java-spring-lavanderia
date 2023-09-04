package com.example.apilavanderia.configs;

import com.example.apilavanderia.customExceptions.AlreadyExistsException;
import com.example.apilavanderia.customExceptions.BookingException;
import com.example.apilavanderia.customExceptions.UnauthorizedException;
import com.example.apilavanderia.dtos.ResponseError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class HandleException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity dataValidationError(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors().stream().map((error) -> new ResponseError(error.getDefaultMessage(), error.getField()));

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFound(EntityNotFoundException exception) {
        return ResponseEntity.badRequest().body(new ResponseError("Entidade nao encontrada", null, "Valor informado invalido"));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity userUnauthorized(UnauthorizedException exception) {
        return ResponseEntity.badRequest().body(new ResponseError(exception.getMessage(), null, "Falta de token de autenticacao"));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity alreadyExists(AlreadyExistsException exception) {
        return ResponseEntity.badRequest().body(new ResponseError("Apartamento já existente!"));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity noSuchElement(NoSuchElementException exception) {
        return ResponseEntity.badRequest().body(new ResponseError("Não encontrado!"));
    }

    @ExceptionHandler(BookingException.class)
    public ResponseEntity bookingError(BookingException exception) {
        return ResponseEntity.badRequest().body(new ResponseError(exception.getMessage(), null, "Erro durante acao"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeError(RuntimeException exception) {
        return ResponseEntity.internalServerError().body(new ResponseError(exception.getMessage(), null, "Erro interno"));
    }
}