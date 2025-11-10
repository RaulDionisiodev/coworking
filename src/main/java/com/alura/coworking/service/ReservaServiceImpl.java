package com.alura.coworking.service;

import com.alura.coworking.domain.Reserva;

public class ReservaServiceImpl implements ReservaService {



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
