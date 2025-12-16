package com.alura.coworking.exception;

public class HoraInvalidaException extends RuntimeException {
    public HoraInvalidaException() {
        super("A hora de fim deve ser depois da hora de início e diferente da hora de início.");
    }

    public HoraInvalidaException(String message) {
        super(message);
    }
}

