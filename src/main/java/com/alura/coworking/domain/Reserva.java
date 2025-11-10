package com.alura.coworking.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {

    public String idReserva;
    public Usuario usuario;
    public Sala sala;
    public String dataReserva;
    public LocalDateTime horaInicio;
    public LocalDateTime horaFim;
    public StatusReserva statusReserva;

    public Reserva(String idReserva, Usuario usuario, Sala sala, String dataReserva, LocalDateTime horaInicio, LocalDateTime horaFim) {
        this.idReserva = idReserva;
        this.usuario = usuario;
        this.sala = sala;
        this.dataReserva = dataReserva;
        this.validaHoraDaReserva(horaInicio, horaFim);
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.statusReserva = StatusReserva.ATIVA;
    }

    public Reserva() {
    }

    public String getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sala getSala() {
        return sala;
    }

    public void setSala(Sala sala) {
        this.sala = sala;
    }

    public String getDataReserva() {
        return dataReserva;
    }

    public void setDataReserva(String dataReserva) {
        this.dataReserva = dataReserva;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFim() {
        return horaFim;
    }

    public StatusReserva getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(StatusReserva statusReserva) {
        this.statusReserva = statusReserva;
    }

    public void setHoraFim(LocalDateTime horaFim) {
        validaHoraDaReserva(this.horaInicio, horaFim);
        this.horaFim = horaFim;
    }

    private void validaHoraDaReserva(LocalDateTime horaInicio, LocalDateTime horaFim) {
        if (horaFim.isBefore(horaInicio)) {
            throw new IllegalArgumentException("A hora de fim deve ser depois da hora de início.");
        }
        if (horaFim.isEqual(horaInicio)) {
            throw new IllegalArgumentException("A hora de fim deve ser diferente da hora de início.");
        }
    }

    private void reservarSala() {
        if (sala.isEmUso()) {
            throw new IllegalStateException("A sala já está em uso no horário selecionado.");
        }
        sala.setEmUso(true);
        statusReserva = StatusReserva.ATIVA;
    }

    private void finalizarReserva() {
        sala.setEmUso(false);
        statusReserva = StatusReserva.CONCLUIDA;
    }

    private void cancelarReserva() {
        sala.setEmUso(false);
        statusReserva = StatusReserva.CANCELADA;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(idReserva, reserva.idReserva) && Objects.equals(usuario, reserva.usuario) && Objects.equals(sala,
            reserva.sala) && Objects.equals(dataReserva, reserva.dataReserva) && Objects.equals(horaInicio,
            reserva.horaInicio) && Objects.equals(horaFim, reserva.horaFim);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva, usuario, sala, dataReserva, horaInicio, horaFim);
    }
}
