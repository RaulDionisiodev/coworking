package com.alura.coworking.service;

import com.alura.coworking.domain.Reserva;
import com.alura.coworking.domain.Sala;
import com.alura.coworking.domain.Usuario;
import com.alura.coworking.repository.ReservaRepository;
import com.alura.coworking.repository.SalaRepository;
import com.alura.coworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void reservarSala(String idUsuario, String idSala, String dataReserva, String horaInicio, String horaFim) {
        Reserva reserva = new Reserva();

        //buscar usuario pelo idUsuario
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

        //buscar sala pelo idSala
        Sala sala = salaRepository.findById(idSala).orElseThrow(() -> new IllegalArgumentException("Sala não encontrada"));

        //validar se a sala esta disponivel no horario solicitado
        reserva.setSala(sala);
        reserva.setUsuario(usuario);
        reserva.setDataReserva(dataReserva);

        java.time.LocalDateTime horaInicioLDT = java.time.LocalDateTime.parse(horaInicio);
        java.time.LocalDateTime horaFimLDT = java.time.LocalDateTime.parse(horaFim);
        reserva.setHoraInicio(horaInicioLDT);
        reserva.setHoraFim(horaFimLDT);

        boolean salaDisponivel = reservaRepository.findAll().stream()
                .filter(r -> r.getSala().getId().equals(idSala))
                .filter(r -> r.getDataReserva().equals(dataReserva))
                .noneMatch(r -> r.getHoraInicio().isBefore(horaInicioLDT) && r.getHoraFim().isAfter(horaFimLDT));

        //Defina a política de conflito: a mesma sala não pode ter reservas sobrepostas.

        if (!salaDisponivel) {
            throw new IllegalArgumentException("Sala não disponível no horário solicitado");
        }

        if (horaFimLDT.isBefore(horaInicioLDT) || horaFimLDT.isEqual(horaInicioLDT)) {
            throw new IllegalArgumentException("Hora de fim deve ser depois da hora de início");
        }

        if (sala.isEmUso()) {
            throw new IllegalStateException("A sala já está em uso no horário selecionado.");
        }

        reserva.getSala().setEmUso(true);

        reservaRepository.save(reserva);
    }
}
