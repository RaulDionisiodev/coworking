package com.alura.coworking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SalaDto(
    String id,
    @NotBlank(message = "O nome da sala é obrigatório")
    String nomeDaSala,
    @Min(value = 1, message = "A capacidade deve ser pelo menos 1")
    int capacidade,
    boolean emUso) {

    @Override
    public String id() {
        return id;
    }

    @Override
    public String nomeDaSala() {
        return nomeDaSala;
    }

    @Override
    public int capacidade() {
        return capacidade;
    }

    @Override
    public boolean emUso() {
        return emUso;
    }
}
