package com.alura.coworking.exception;

public class ReservaNaoEncontradaException extends RuntimeException {
    public ReservaNaoEncontradaException() {
        super("Reserva não encontrada.");
    }

    public ReservaNaoEncontradaException(String message) {
        super(message);
    }
}
