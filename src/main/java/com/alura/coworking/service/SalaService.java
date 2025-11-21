package com.alura.coworking.service;

import com.alura.coworking.dto.SalaDto;

import java.util.List;

public interface SalaService {

    SalaDto cadastrarSala(SalaDto salaDto);

    SalaDto obterSalaPorId(String idSala);

    boolean atualizarSala(String idSala, SalaDto salaDto);

    void deletarSala(String idSala);

    List<SalaDto> listarSalas();

}
