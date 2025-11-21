package com.alura.coworking.service;

import com.alura.coworking.domain.Reserva;
import com.alura.coworking.domain.Sala;
import com.alura.coworking.domain.StatusReserva;
import com.alura.coworking.domain.Usuario;
import com.alura.coworking.dto.ReservaDto;
import com.alura.coworking.exception.ReservaInvalidaException;
import com.alura.coworking.exception.ReservaNaoEncontradaException;
import com.alura.coworking.exception.SalaNaoEncontradaException;
import com.alura.coworking.exception.UsuarioNaoEncontradoException;
import com.alura.coworking.repository.ReservaRepository;
import com.alura.coworking.repository.SalaRepository;
import com.alura.coworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ReadOnlyFileSystemException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SalaRepository salaRepository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, SalaRepository salaRepository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.salaRepository = salaRepository;
    }

    @Override
    public void reservarSala(ReservaDto reservaDto) {
        Reserva reserva = new Reserva();

        // buscar usuario pelo idUsuario do reservaDto
        Usuario usuario = usuarioRepository.findById(reservaDto.getIdUsuario())
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));

        // buscar sala pelo idSala do reservaDto
        Sala sala = salaRepository.findById(reservaDto.getIdSala())
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada"));

        // validar se a sala está disponível no horário solicitado
        reserva.setSala(sala);
        reserva.setUsuario(usuario);
        reserva.setDataReserva(reservaDto.getDataReserva());

        java.time.LocalDateTime horaInicioLDT = java.time.LocalDateTime.parse(reservaDto.getHoraInicio());
        java.time.LocalDateTime horaFimLDT = java.time.LocalDateTime.parse(reservaDto.getHoraFim());
        reserva.setHoraInicio(horaInicioLDT);
        reserva.setHoraFim(horaFimLDT);

        boolean salaDisponivel = reservaRepository.findAll().stream()
                .filter(r -> r.getSala().getId().equals(reservaDto.getIdSala()))
                .filter(r -> r.getDataReserva().equals(reservaDto.getDataReserva()))
                .noneMatch(r -> r.getHoraInicio().isBefore(horaInicioLDT) && r.getHoraFim().isAfter(horaFimLDT));

        // Defina a política de conflito: a mesma sala não pode ter reservas sobrepostas.

        if (!salaDisponivel) {
            throw new ReservaInvalidaException("Sala não disponível no horário solicitado");
        }

        if (horaFimLDT.isBefore(horaInicioLDT) || horaFimLDT.isEqual(horaInicioLDT)) {
            throw new ReservaInvalidaException("Hora de fim deve ser depois da hora de início");
        }

        if (sala.isEmUso()) {
            throw new ReservaInvalidaException("A sala já está em uso no horário selecionado.");
        }

        reserva.getSala().setEmUso(true);

        reservaRepository.save(reserva);
    }

    @Override
    public List<ReservaDto> listarReservas() {
        List<Reserva> reservas = reservaRepository.findAll();
        return reservas.stream()
                .map(r -> new ReservaDto(
                        r.getIdReserva(),
                        r.getUsuario().getId(),
                        r.getSala().getId(),
                        r.getDataReserva(),
                        r.getHoraInicio().toString(),
                        r.getHoraFim().toString()
                ))
                .toList();
    }

    @Override
    public ReservaDto obterReservaPorId(String idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));
        if (reserva != null) {
            return new ReservaDto(
                    reserva.getIdReserva(),
                    reserva.getUsuario().getId(),
                    reserva.getSala().getId(),
                    reserva.getDataReserva(),
                    reserva.getHoraInicio().toString(),
                    reserva.getHoraFim().toString()
            );
        }
        return null;
    }

    @Override
    public void cancelarReserva(String idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));
        reserva.getSala().setEmUso(false);
        reserva.setStatusReserva(StatusReserva.CANCELADA);
        reservaRepository.save(reserva);

    }

    @Override
    public boolean atualizarReserva(String id, ReservaDto reservaDto) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ReservaNaoEncontradaException("Reserva não encontrada"));

        Sala sala = salaRepository.findById(reservaDto.getIdSala())
                .orElseThrow(() -> new SalaNaoEncontradaException("Sala não encontrada"));

        if (!reserva.getSala().getId().equals(sala.getId())) {
            reserva.getSala().setEmUso(false);
            sala.setEmUso(true);
            reserva.setSala(sala);
        }

        LocalDateTime horaInicioLDT = LocalDateTime.parse(reservaDto.getHoraInicio());
        LocalDateTime horaFimLDT = LocalDateTime.parse(reservaDto.getHoraFim());

        reserva.setDataReserva(reservaDto.getDataReserva());
        reserva.setHoraInicio(horaInicioLDT);
        reserva.setHoraFim(horaFimLDT);

        reservaRepository.save(reserva);
        return true;
    }
}
