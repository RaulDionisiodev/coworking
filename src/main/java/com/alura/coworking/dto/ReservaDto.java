package com.alura.coworking.dto;

public record ReservaDto(String idReserva, String idUsuario, String idSala, String dataReserva, String horaInicio, String horaFim) {
    public String getIdReserva() {
        return idReserva;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getIdSala() {
        return idSala;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }
}
