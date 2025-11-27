package com.alura.coworking.dto;

import jakarta.validation.constraints.NotBlank;

public record ReservaDto(
    String idReserva,
    @NotBlank(message = "O idUsuario é obrigatório")
    String idUsuario,
    @NotBlank(message = "O idSala é obrigatório")
    String idSala,
    @NotBlank(message = "A dataReserva é obrigatória")
    String dataReserva,
    @NotBlank(message = "A horaInicio é obrigatória")
    String horaInicio,
    @NotBlank(message = "A horaFim é obrigatória")
    String horaFim
) {


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
