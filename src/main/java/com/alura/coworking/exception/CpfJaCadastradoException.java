package com.alura.coworking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CpfJaCadastradoException extends ResponseStatusException {
    public CpfJaCadastradoException() {
        super(HttpStatus.CONFLICT, "CPF já cadastrado");
    }
}
