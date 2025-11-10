package com.alura.coworking.service;

import org.springframework.stereotype.Service;

@Service
public interface ReservaService {

    void reservarSala(String idUsuario, String idSala, String dataReserva, String horaInicio, String horaFim);
}
