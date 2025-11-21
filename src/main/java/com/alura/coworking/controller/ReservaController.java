package com.alura.coworking.controller;

import com.alura.coworking.config.ApiV1Prefix;
import com.alura.coworking.dto.ReservaDto;
import com.alura.coworking.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiV1Prefix.API_V1 + "/reservas")
public class ReservaController {

    private final ReservaService service;

    @Autowired
    public ReservaController (ReservaService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<Void> reservarSala(@RequestBody ReservaDto reservaDto) {
        service.reservarSala(reservaDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<ReservaDto>> listarReservas() {
        List<ReservaDto> reservas = service.listarReservas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> obterReservaPorId(@PathVariable String id) {
        ReservaDto reserva = service.obterReservaPorId(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarReserva(@PathVariable String id) {
        try {
            service.cancelarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizarReserva(@PathVariable String id, @RequestBody @Validated ReservaDto reservaDto) {
        boolean atualizada = service.atualizarReserva(id, reservaDto);
        if (atualizada) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
