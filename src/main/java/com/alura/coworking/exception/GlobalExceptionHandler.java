package com.alura.coworking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SalaNaoEncontradaException.class)
    public ResponseEntity<Void> handleSalaNaoEncontrada(SalaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(ReservaNaoEncontradaException.class)
    public ResponseEntity<Void> handleReservaNaoEncontrada(ReservaNaoEncontradaException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<String> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ReservaInvalidaException.class)
    public ResponseEntity<String> handleReservaInvalida(ReservaInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
    }

    @ExceptionHandler(CpfJaCadastradoException.class)
    public ResponseEntity<String> handleCpfJaCadastrado(CpfJaCadastradoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getReason());
    }

    @ExceptionHandler(HoraInvalidaException.class)
    public ResponseEntity<String> handleHoraInvalida(HoraInvalidaException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
