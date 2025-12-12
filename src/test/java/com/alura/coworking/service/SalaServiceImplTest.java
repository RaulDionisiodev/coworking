package com.alura.coworking.service;

import com.alura.coworking.domain.Sala;
import com.alura.coworking.dto.SalaDto;
import com.alura.coworking.exception.SalaNaoEncontradaException;
import com.alura.coworking.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class SalaServiceImplTest {

    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private SalaServiceImpl salaService;

    public SalaServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarSalaComSucesso () {
        SalaDto dto = new SalaDto(null, "sala 1", 10,  false);
        Sala sala = new Sala();
        sala.setNomeDaSala(dto.nomeDaSala());
        sala.setCapacidade(dto.capacidade());
        sala.setEmUso(dto.emUso());

        when(salaRepository.save(sala))
            .thenAnswer(invocation -> {
                Sala s = invocation.getArgument(0);
                s.setId("1");
                return s;
            });

        SalaDto resultado = salaService.cadastrarSala(dto);

        assert resultado.id() != null;
        assert resultado.nomeDaSala().equals(dto.nomeDaSala());
        assert resultado.capacidade() == dto.capacidade();
        assert resultado.emUso() == dto.emUso();

    }

    @Test
    void deveObterSalaPorIdComSucesso () {
        String idSala = "1";
        Sala sala = new Sala();
        sala.setId(idSala);
        sala.setNomeDaSala("sala 1");
        sala.setCapacidade(10);
        sala.setEmUso(false);

        when(salaRepository.findById(idSala)).thenReturn(Optional.of(sala));

        SalaDto resultado = salaService.obterSalaPorId(idSala);

        assert resultado.id().equals(idSala);
        assert resultado.nomeDaSala().equals(sala.getNomeDaSala());
        assert resultado.capacidade() == sala.getCapacidade();
        assert resultado.emUso() == sala.isEmUso();
    }

    @Test
    void deveDispararExcecaoQuandoSalaNaoEncontrada () {
        String idSala = "999";

        when(salaRepository.findById(idSala)).thenReturn(Optional.empty());

        try {
            salaService.obterSalaPorId(idSala);
            assert false; // Deve lançar exceção, então falha se chegar aqui
        } catch (Exception e) {
            assert e instanceof SalaNaoEncontradaException;
            assert e.getMessage().equals("Sala com ID " + idSala + " nao encontrada.");
        }
    }

    @Test
    void deveAtualizarSalaComSucesso () {
        String idSala = "1";
        SalaDto dto = new SalaDto(null, "sala atualizada", 20, true);
        Sala salaExistente = new Sala();
        salaExistente.setId(idSala);
        salaExistente.setNomeDaSala("sala 1");
        salaExistente.setCapacidade(10);
        salaExistente.setEmUso(false);

        when(salaRepository.findById(idSala)).thenReturn(Optional.of(salaExistente));
        when(salaRepository.save(salaExistente)).thenReturn(salaExistente);

        boolean resultado = salaService.atualizarSala(idSala, dto);

        assert resultado;
        assert salaExistente.getNomeDaSala().equals(dto.nomeDaSala());
        assert salaExistente.getCapacidade() == dto.capacidade();
        assert salaExistente.isEmUso() == dto.emUso();
    }

    @Test
    void deveDispararExcecaoAoAtualizarSalaInexistente () {
        String idSala = "999";
        SalaDto dto = new SalaDto(null, "sala atualizada", 20, true);

        when(salaRepository.findById(idSala)).thenReturn(Optional.empty());

        try {
            salaService.atualizarSala(idSala, dto);
            assert false;
        } catch (Exception e) {
            assert e instanceof SalaNaoEncontradaException;
            assert e.getMessage().equals("Sala com ID " + idSala + " nao encontrada.");
        }
    }

    @Test
    void deveDeletarSalaComSucesso () {
        String idSala = "1";
        Sala salaExistente = new Sala();
        salaExistente.setId(idSala);
        salaExistente.setNomeDaSala("sala 1");
        salaExistente.setCapacidade(10);
        salaExistente.setEmUso(false);

        when(salaRepository.findById(idSala)).thenReturn(Optional.of(salaExistente));
        doNothing().when(salaRepository).delete(salaExistente);

        salaService.deletarSala(idSala);

        verify(salaRepository, times(1)).delete(salaExistente);
    }

    @Test
    void deveDispararExcecaoAoDeletarSalaInexistente () {
        String idSala = "999";
        when(salaRepository.findById(idSala)).thenReturn(Optional.empty());
        try {
            salaService.deletarSala(idSala);
            assert false;
        }
        catch (Exception e) {
            assert e instanceof SalaNaoEncontradaException;
            assert e.getMessage().equals("Sala com ID " + idSala + " nao encontrada.");
        }
    }

    @Test
    void deveListarSalasComSucesso () {
        Sala sala1 = new Sala();
        sala1.setId("1");
        sala1.setNomeDaSala("sala 1");
        sala1.setCapacidade(10);
        sala1.setEmUso(false);

        Sala sala2 = new Sala();
        sala2.setId("2");
        sala2.setNomeDaSala("sala 2");
        sala2.setCapacidade(20);
        sala2.setEmUso(true);

        when(salaRepository.findAll()).thenReturn(List.of(sala1, sala2));

        List<SalaDto> resultado = salaService.listarSalas();

        assert resultado.size() == 2;

        SalaDto dto1 = resultado.getFirst();
        assert dto1.id().equals(sala1.getId());
        assert dto1.nomeDaSala().equals(sala1.getNomeDaSala());
        assert dto1.capacidade() == sala1.getCapacidade();
        assert dto1.emUso() == sala1.isEmUso();

        SalaDto dto2 = resultado.get(1);
        assert dto2.id().equals(sala2.getId());
        assert dto2.nomeDaSala().equals(sala2.getNomeDaSala());
        assert dto2.capacidade() == sala2.getCapacidade();
        assert dto2.emUso() == sala2.isEmUso();
    }
}
