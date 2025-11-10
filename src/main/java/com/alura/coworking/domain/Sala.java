package com.alura.coworking.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class Sala {

    @Id
    private String id; // identificador unico da sala
    private String nomeDaSala; // nome da sala
    private int capacidade; // capacidade maxima de pessoas na sala
    private boolean emUso; // indica se a sala esta em uso

    public Sala(String id, String nomeDaSala, int capacidade) {
        validaCapacidade(capacidade);
        this.id = id;
        this.nomeDaSala = nomeDaSala;
        this.capacidade = capacidade;
        this.emUso = false;
    }

    public Sala() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeDaSala() {
        return nomeDaSala;
    }

    public void setNomeDaSala(String nomeDaSala) {
        this.nomeDaSala = nomeDaSala;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        validaCapacidade(capacidade);
        this.capacidade = capacidade;
    }

    public boolean isEmUso() {
        return emUso;
    }

    public void setEmUso(boolean emUso) {
        this.emUso = emUso;
    }

    private void validaCapacidade(int capacidade) {
        if (capacidade <= 0) {
            throw new IllegalArgumentException("A capacidade da sala deve ser maior que zero.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Sala sala = (Sala) o;
        return capacidade == sala.capacidade && Objects.equals(id, sala.id) && Objects.equals(nomeDaSala, sala.nomeDaSala);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nomeDaSala, capacidade);
    }
}
