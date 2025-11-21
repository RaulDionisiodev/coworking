package com.alura.coworking.service;

import com.alura.coworking.domain.Sala;
import com.alura.coworking.dto.SalaDto;
import com.alura.coworking.exception.SalaNaoEncontradaException;
import com.alura.coworking.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SalaServiceImpl implements SalaService{

    private final SalaRepository repository;

    @Autowired
    public SalaServiceImpl(SalaRepository repository) {
        this.repository = repository;
    }


    @Override
    public SalaDto cadastrarSala(SalaDto salaDto) {
        Sala sala = new Sala();
        sala.setNomeDaSala(salaDto.nomeDaSala());
        sala.setCapacidade(salaDto.capacidade());
        sala.setEmUso(salaDto.emUso());
        sala = repository.save(sala);
        return toDto(sala);
    }

    @Override
    public SalaDto obterSalaPorId(String idSala) {
        Sala sala = repository.findById(idSala)
            .orElseThrow(() -> new SalaNaoEncontradaException("Sala com ID " + idSala + " nao encontrada."));
        return toDto(sala);
    }

    @Override
    public boolean atualizarSala(String idSala, SalaDto salaDto) {
        Sala sala = repository.findById(idSala)
            .orElseThrow(() -> new SalaNaoEncontradaException("Sala com ID " + idSala + " nao encontrada."));
        sala.setNomeDaSala(salaDto.nomeDaSala());
        sala.setCapacidade(salaDto.capacidade());
        sala.setEmUso(salaDto.emUso());
        repository.save(sala);
        return true;
    }

    @Override
    public void deletarSala(String idSala) {
        Sala sala = repository.findById(idSala)
            .orElseThrow(() -> new SalaNaoEncontradaException("Sala com ID " + idSala + " nao encontrada."));
        repository.delete(sala);
    }

    @Override
    public List<SalaDto> listarSalas() {
        List<Sala> salas = repository.findAll();
        return salas.stream()
                .map(this::toDto)
                .toList();
    }

    private SalaDto toDto(Sala sala) {
        return new SalaDto(sala.getId(), sala.getNomeDaSala(), sala.getCapacidade(), sala.isEmUso());
    }
}
