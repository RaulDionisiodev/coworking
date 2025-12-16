package com.alura.coworking.service;

import com.alura.coworking.domain.Reserva;
import com.alura.coworking.domain.Sala;
import com.alura.coworking.domain.StatusReserva;
import com.alura.coworking.domain.Usuario;
import com.alura.coworking.dto.ReservaDto;
import com.alura.coworking.exception.*;
import com.alura.coworking.repository.ReservaRepository;
import com.alura.coworking.repository.SalaRepository;
import com.alura.coworking.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SalaRepository salaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private ReservaDto reservaDto;
    private Usuario usuario;
    private Sala sala;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("user1");
        sala = new Sala();
        sala.setId("sala1");
        sala.setEmUso(false);
        // Ajuste para garantir formato correto das datas
        String dataReserva = java.time.LocalDate.now().toString();
        String horaInicio = java.time.LocalDateTime.now().plusHours(1).withNano(0).toString();
        String horaFim = java.time.LocalDateTime.now().plusHours(2).withNano(0).toString();
        reservaDto = new ReservaDto(null, "user1", "sala1", dataReserva, horaInicio, horaFim);
    }

    @Test
    void deveReservarSalaComSucesso() {
        when(usuarioRepository.findById(anyString())).thenReturn(
             Optional.of(usuario)
        );
        when(salaRepository.findById("sala1")).thenReturn(Optional.of(sala));
        when(reservaRepository.findAll()).thenReturn(Collections.emptyList());

        assertNotNull(reservaDto.getIdUsuario(), "O idUsuario do DTO não pode ser nulo");
        assertEquals("user1", reservaDto.getIdUsuario(), "O idUsuario do DTO deve ser 'user1'");

        assertDoesNotThrow(() -> reservaService.reservarSala(reservaDto));
        assertTrue(sala.isEmUso());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoEncontrado() {
        when(usuarioRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> reservaService.reservarSala(reservaDto));
    }

    @Test
    void deveLancarExcecaoSeSalaNaoEncontrada() {
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(salaRepository.findById("sala1")).thenReturn(Optional.empty());
        assertThrows(SalaNaoEncontradaException.class, () -> reservaService.reservarSala(reservaDto));
    }

    @Test
    void deveLancarExcecaoSeSalaNaoDisponivel() {
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(salaRepository.findById("sala1")).thenReturn(Optional.of(sala));
        Reserva reservaExistente = new Reserva();
        reservaExistente.setSala(sala);
        reservaExistente.setDataReserva(reservaDto.getDataReserva());
        reservaExistente.setHoraInicio(LocalDateTime.parse(reservaDto.getHoraInicio()).minusMinutes(30));
        reservaExistente.setHoraFim(LocalDateTime.parse(reservaDto.getHoraFim()).plusMinutes(30));
        when(reservaRepository.findAll()).thenReturn(Collections.singletonList(reservaExistente));
        assertThrows(ReservaInvalidaException.class, () -> reservaService.reservarSala(reservaDto));
    }

    @Test
    void deveLancarExcecaoSeHoraFimAntesOuIgualHoraInicio() {
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(salaRepository.findById("sala1")).thenReturn(Optional.of(sala));
        reservaDto = new ReservaDto(null, "user1", "sala1", LocalDate.now().toString(),
                LocalDateTime.now().plusHours(2).toString(), LocalDateTime.now().plusHours(1).toString());
        assertThrows(HoraInvalidaException.class, () -> reservaService.reservarSala(reservaDto));
    }

    @Test
    void deveLancarExcecaoSeSalaJaEmUso() {
        sala.setEmUso(true);
        when(usuarioRepository.findById("user1")).thenReturn(Optional.of(usuario));
        when(salaRepository.findById("sala1")).thenReturn(Optional.of(sala));
        when(reservaRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(ReservaInvalidaException.class, () -> reservaService.reservarSala(reservaDto));
    }

    @Test
    void deveRetornarListaDeReservaDtoCorretamente() {
        Usuario usuarioLocal = new Usuario();
        usuarioLocal.setId("user1");
        Sala salaLocal = new Sala();
        salaLocal.setId("sala1");
        Reserva reserva = new Reserva();
        reserva.setIdReserva("res1");
        reserva.setUsuario(usuarioLocal);
        reserva.setSala(salaLocal);
        reserva.setDataReserva("2025-12-16");
        reserva.setHoraInicio(LocalDateTime.of(2025, 12, 16, 10, 0));
        reserva.setHoraFim(LocalDateTime.of(2025, 12, 16, 12, 0));
        when(reservaRepository.findAll()).thenReturn(List.of(reserva));

        List<ReservaDto> dtos = reservaService.listarReservas();
        assertEquals(1, dtos.size());
        ReservaDto dto = dtos.get(0);
        assertEquals("res1", dto.getIdReserva());
        assertEquals("user1", dto.getIdUsuario());
        assertEquals("sala1", dto.getIdSala());
        assertEquals("2025-12-16", dto.getDataReserva());
        assertEquals("2025-12-16T10:00", dto.getHoraInicio());
        assertEquals("2025-12-16T12:00", dto.getHoraFim());
        verify(reservaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHaReservas() {
        when(reservaRepository.findAll()).thenReturn(List.of());
        List<ReservaDto> dtos = reservaService.listarReservas();
        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
        verify(reservaRepository).findAll();
    }

    @Test
    void deveRetornarReservaDtoQuandoReservaExiste() {
        Usuario usuarioLocal = new Usuario();
        usuarioLocal.setId("user1");
        Sala salaLocal = new Sala();
        salaLocal.setId("sala1");
        Reserva reserva = new Reserva();
        reserva.setIdReserva("res1");
        reserva.setUsuario(usuarioLocal);
        reserva.setSala(salaLocal);
        reserva.setDataReserva("2025-12-16");
        reserva.setHoraInicio(LocalDateTime.of(2025, 12, 16, 10, 0));
        reserva.setHoraFim(LocalDateTime.of(2025, 12, 16, 12, 0));
        when(reservaRepository.findById("res1")).thenReturn(Optional.of(reserva));

        ReservaDto dto = reservaService.obterReservaPorId("res1");
        assertNotNull(dto);
        assertEquals("res1", dto.getIdReserva());
        assertEquals("user1", dto.getIdUsuario());
        assertEquals("sala1", dto.getIdSala());
        assertEquals("2025-12-16", dto.getDataReserva());
        assertEquals("2025-12-16T10:00", dto.getHoraInicio());
        assertEquals("2025-12-16T12:00", dto.getHoraFim());
        verify(reservaRepository).findById("res1");
    }

    @Test
    void deveLancarExcecaoQuandoReservaNaoExiste() {
        when(reservaRepository.findById("naoExiste")).thenReturn(Optional.empty());
        assertThrows(ReservaNaoEncontradaException.class, () -> reservaService.obterReservaPorId("naoExiste"));
        verify(reservaRepository).findById("naoExiste");
    }

    @Test
    void deveCancelarReservaQuandoExiste() {
        Usuario usuarioLocal = new Usuario();
        usuarioLocal.setId("user1");
        Sala salaLocal = new Sala();
        salaLocal.setId("sala1");
        salaLocal.setEmUso(true);
        Reserva reserva = new Reserva();
        reserva.setIdReserva("res1");
        reserva.setUsuario(usuarioLocal);
        reserva.setSala(salaLocal);
        reserva.setStatusReserva(StatusReserva.ATIVA);
        when(reservaRepository.findById("res1")).thenReturn(Optional.of(reserva));

        reservaService.cancelarReserva("res1");

        assertEquals(StatusReserva.CANCELADA, reserva.getStatusReserva());
        assertFalse(salaLocal.isEmUso());
        verify(reservaRepository).save(reserva);
        verify(reservaRepository).findById("res1");
    }

    @Test
    void deveLancarExcecaoAoCancelarReservaInexistente() {
        when(reservaRepository.findById("naoExiste")).thenReturn(Optional.empty());
        assertThrows(ReservaNaoEncontradaException.class, () -> reservaService.cancelarReserva("naoExiste"));
        verify(reservaRepository).findById("naoExiste");
    }

    @Test
    void deveAtualizarReservaComSucessoTrocaSala() {
        // Reserva original
        Sala salaAntiga = new Sala();
        salaAntiga.setId("sala1");
        salaAntiga.setEmUso(true);
        Sala salaNova = new Sala();
        salaNova.setId("sala2");
        salaNova.setEmUso(false);
        Reserva reserva = new Reserva();
        reserva.setIdReserva("res1");
        reserva.setSala(salaAntiga);
        reserva.setDataReserva("2025-12-16");
        reserva.setHoraInicio(LocalDateTime.of(2025, 12, 16, 10, 0));
        reserva.setHoraFim(LocalDateTime.of(2025, 12, 16, 12, 0));
        ReservaDto dto = new ReservaDto("res1", "user1", "sala2", "2025-12-17", "2025-12-17T14:00", "2025-12-17T16:00");
        when(reservaRepository.findById("res1")).thenReturn(Optional.of(reserva));
        when(salaRepository.findById("sala2")).thenReturn(Optional.of(salaNova));

        boolean result = reservaService.atualizarReserva("res1", dto);

        assertTrue(result);
        assertEquals(salaNova, reserva.getSala());
        assertEquals("2025-12-17", reserva.getDataReserva());
        assertEquals(LocalDateTime.of(2025, 12, 17, 14, 0), reserva.getHoraInicio());
        assertEquals(LocalDateTime.of(2025, 12, 17, 16, 0), reserva.getHoraFim());
        assertTrue(salaNova.isEmUso());
        assertFalse(salaAntiga.isEmUso());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void deveAtualizarReservaComSucessoMesmaSala() {
        Sala sala = new Sala();
        sala.setId("sala1");
        sala.setEmUso(true);
        Reserva reserva = new Reserva();
        reserva.setIdReserva("res1");
        reserva.setSala(sala);
        reserva.setDataReserva("2025-12-16");
        reserva.setHoraInicio(LocalDateTime.of(2025, 12, 16, 10, 0));
        reserva.setHoraFim(LocalDateTime.of(2025, 12, 16, 12, 0));
        ReservaDto dto = new ReservaDto("res1", "user1", "sala1", "2025-12-18", "2025-12-18T09:00", "2025-12-18T11:00");
        when(reservaRepository.findById("res1")).thenReturn(Optional.of(reserva));
        when(salaRepository.findById("sala1")).thenReturn(Optional.of(sala));

        boolean result = reservaService.atualizarReserva("res1", dto);

        assertTrue(result);
        assertEquals(sala, reserva.getSala());
        assertEquals("2025-12-18", reserva.getDataReserva());
        assertEquals(LocalDateTime.of(2025, 12, 18, 9, 0), reserva.getHoraInicio());
        assertEquals(LocalDateTime.of(2025, 12, 18, 11, 0), reserva.getHoraFim());
        assertTrue(sala.isEmUso());
        verify(reservaRepository).save(reserva);
    }

    @Test
    void deveLancarExcecaoSeReservaNaoEncontradaAoAtualizar() {
        ReservaDto dto = new ReservaDto("res1", "user1", "sala1", "2025-12-18", "2025-12-18T09:00", "2025-12-18T11:00");
        when(reservaRepository.findById("res1")).thenReturn(Optional.empty());
        assertThrows(ReservaNaoEncontradaException.class, () -> reservaService.atualizarReserva("res1", dto));
    }

    @Test
    void deveLancarExcecaoSeSalaNaoEncontradaAoAtualizar() {
        Sala sala = new Sala();
        sala.setId("sala1");
        Reserva reserva = new Reserva();
        reserva.setIdReserva("res1");
        reserva.setSala(sala);
        ReservaDto dto = new ReservaDto("res1", "user1", "sala2", "2025-12-18", "2025-12-18T09:00", "2025-12-18T11:00");
        when(reservaRepository.findById("res1")).thenReturn(Optional.of(reserva));
        when(salaRepository.findById("sala2")).thenReturn(Optional.empty());
        assertThrows(SalaNaoEncontradaException.class, () -> reservaService.atualizarReserva("res1", dto));
    }

}
