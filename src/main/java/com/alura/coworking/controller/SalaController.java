package com.alura.coworking.controller;

import com.alura.coworking.config.ApiV1Prefix;
import com.alura.coworking.dto.SalaDto;
import com.alura.coworking.service.SalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiV1Prefix.API_V1 + "/salas")
public class SalaController {

    private final SalaService service;

    @Autowired
    public SalaController(SalaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SalaDto>> listarSalas() {
         List<SalaDto> salas = service.listarSalas();
         return ResponseEntity.ok(salas);
    }

    @GetMapping("/{idSala}")
    public ResponseEntity<SalaDto> obterSalaPorId(@PathVariable String idSala) {
        SalaDto salaDto = service.obterSalaPorId(idSala);
        return ResponseEntity.ok(salaDto);
    }

    @PostMapping
    public ResponseEntity<SalaDto> cadastrarSala(@RequestBody @Validated SalaDto salaDto) {
        SalaDto novaSala = service.cadastrarSala(salaDto);
        return ResponseEntity.ok(novaSala);
    }

    @PutMapping("/{idSala}")
    public ResponseEntity<Void> atualizarSala(@PathVariable String idSala, @RequestBody SalaDto salaDto) {
        boolean atualizado = service.atualizarSala(idSala, salaDto);
        if (atualizado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{idSala}")
    public ResponseEntity<Void> deletarSala(@PathVariable String idSala) {
        service.deletarSala(idSala);
        return ResponseEntity.noContent().build();
    }
}
