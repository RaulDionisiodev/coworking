package com.alura.coworking.dto;

public record SalaDto(String id, String nomeDaSala, int capacidade, boolean emUso) {

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
