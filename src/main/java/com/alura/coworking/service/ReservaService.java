package com.alura.coworking.service;

import com.alura.coworking.dto.ReservaDto;

import java.util.List;

public interface ReservaService {

    void reservarSala(ReservaDto reservaDto);

    List<ReservaDto> listarReservas();

    ReservaDto obterReservaPorId(String idReserva);

    void cancelarReserva(String idReserva);

    boolean atualizarReserva( String id ,ReservaDto reservaDto);



}
