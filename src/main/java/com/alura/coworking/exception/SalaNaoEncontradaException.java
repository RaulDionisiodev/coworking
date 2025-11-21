package com.alura.coworking.exception;

public class SalaNaoEncontradaException extends RuntimeException {
    public SalaNaoEncontradaException() {
        super("Sala não encontrada.");
    }

    public SalaNaoEncontradaException(String message) {
        super(message);
    }
}
