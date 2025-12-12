package com.alura.coworking.controller;

import com.alura.coworking.dto.SalaDto;
import com.alura.coworking.service.SalaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SalaControllerTest {

    @Mock
    private SalaService salaService;

    @InjectMocks
    private SalaController salaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarSalas() {
        SalaDto sala1 = new SalaDto("1", "Sala A", 10, false);
        SalaDto sala2 = new SalaDto("2", "Sala B", 20, true);
        List<SalaDto> salas = Arrays.asList(sala1, sala2);

        when(salaService.listarSalas()).thenReturn(salas);

        ResponseEntity<List<SalaDto>> response = salaController.listarSalas();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(salas, response.getBody());
        verify(salaService, times(1)).listarSalas();
    }

    @Test
    void testObterSalaPorId() {
        String idSala = "1";
        SalaDto salaDto = new SalaDto("1", "Sala A", 10, false);
        when(salaService.obterSalaPorId(idSala)).thenReturn(salaDto);

        ResponseEntity<SalaDto> response = salaController.obterSalaPorId(idSala);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(salaDto, response.getBody());
        verify(salaService, times(1)).obterSalaPorId(idSala);
    }

    @Test
    void testCadastrarSala() {
        SalaDto salaDto = new SalaDto("2", "Sala C", 15, false);
        SalaDto novaSala = new SalaDto("3", "Sala C", 15, false);
        when(salaService.cadastrarSala(salaDto)).thenReturn(novaSala);

        ResponseEntity<SalaDto> response = salaController.cadastrarSala(salaDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(novaSala, response.getBody());
        verify(salaService, times(1)).cadastrarSala(salaDto);
    }

    @Test
    void testAtualizarSala_Sucesso() {
        String idSala = "1";
        SalaDto salaDto = new SalaDto("1", "Sala A Atualizada", 12, true);
        when(salaService.atualizarSala(idSala, salaDto)).thenReturn(true);

        ResponseEntity<Void> response = salaController.atualizarSala(idSala, salaDto);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(salaService, times(1)).atualizarSala(idSala, salaDto);
    }

    @Test
    void testAtualizarSala_NaoEncontrado() {
        String idSala = "1";
        SalaDto salaDto = new SalaDto("1", "Sala A Atualizada", 12, true);
        when(salaService.atualizarSala(idSala, salaDto)).thenReturn(false);

        ResponseEntity<Void> response = salaController.atualizarSala(idSala, salaDto);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(salaService, times(1)).atualizarSala(idSala, salaDto);
    }

    @Test
    void testDeletarSala() {
        String idSala = "1";
        doNothing().when(salaService).deletarSala(idSala);

        ResponseEntity<Void> response = salaController.deletarSala(idSala);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(salaService, times(1)).deletarSala(idSala);
    }
}
