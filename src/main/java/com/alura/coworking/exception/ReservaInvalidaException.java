package com.alura.coworking.exception;

public class ReservaInvalidaException extends RuntimeException {
    public ReservaInvalidaException() {
        super("Reserva inválida.");
    }

    public ReservaInvalidaException(String message) {
        super(message);
    }
}
