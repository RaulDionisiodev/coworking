package com.alura.coworking.service;

import com.alura.coworking.domain.Reserva;
import com.alura.coworking.domain.Sala;
import com.alura.coworking.repository.ReservaRepository;
import com.alura.coworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ReservaServiceImpl implements ReservaService {


    private ReservaRepository reservaRepository;
    private UsuarioRepository usuarioRepository;
    private Sala repository;

    @Autowired
    public ReservaServiceImpl(ReservaRepository reservaRepository, UsuarioRepository usuarioRepository, Sala repository) {
        this.reservaRepository = reservaRepository;
        this.usuarioRepository = usuarioRepository;
        this.repository = repository;
    }

    @Override
    public void reservarSala(String idUsuario, String idSala, String dataReserva, String horaInicio, String horaFim) {
        Reserva reserva = new Reserva();

        //buscar usuario pelo idUsuario
        //buscar sala pelo idSala
        //validar se a sala esta disponivel no horario solicitado
        //Defina a política de conflito: a mesma sala não pode ter reservas sobrepostas.
        // Use intervalo semiaberto (incluindo o início mas sem incluir o fim) para comparação.
        // Documente exemplos-limite (fim igual ao início é permitido). Regras claras evitam
        // bugs sutis e guiam os testes.
        //salvar reserva no repositorio
    }
}
