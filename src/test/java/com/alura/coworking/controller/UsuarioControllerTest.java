package com.alura.coworking.controller;


import com.alura.coworking.dto.UsuarioDto;
import com.alura.coworking.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    public UsuarioControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarUsuarios() {
        when(usuarioService.listarUsuarios()).thenReturn(Collections.emptyList());
        ResponseEntity<?> response = usuarioController.listarUsuarios();
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deveCriarUsuario() {

        UsuarioDto usuarioDto = new UsuarioDto("João", "joao@email.com", "12345678900");
        UsuarioDto usuarioCriado = new UsuarioDto("João", "joao@email.com", "12345678900");
        when(usuarioService.criarUsuario(usuarioDto)).thenReturn(usuarioCriado);
        ResponseEntity<?> response = usuarioController.criarUsuario(usuarioDto);
        assertEquals(201, response.getStatusCode().value());
        assertEquals(usuarioCriado, response.getBody());
    }

    @Test
    void deveBuscarUsuarioPorId() {

        String id = "1";
        UsuarioDto usuarioDto = new UsuarioDto("João", "joao@email.com", "12345678900");
        when(usuarioService.buscarUsuarioPorId(id)).thenReturn(usuarioDto);
        ResponseEntity<?> response = usuarioController.buscarUsuarioPorId(id);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuarioDto, response.getBody());
    }

    @Test
    void deveRetornarNotFoundQuandoUsuarioNaoExistir() {
        String id = "1";
        when(usuarioService.buscarUsuarioPorId(id)).thenReturn(null);
        ResponseEntity<?> response = usuarioController.buscarUsuarioPorId(id);
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void deveAtualizarUsuario() {
        String id = "1";
        UsuarioDto usuarioDto = new UsuarioDto("João", "joao@email.com", "12345678900");
        UsuarioDto usuarioAtualizado = new UsuarioDto("João", "novoEmail@email.com", "12345678900");
        when(usuarioService.atualizarUsuario(id, usuarioDto)).thenReturn(usuarioAtualizado);
        ResponseEntity<?> response = usuarioController.atualizarUsuario(id, usuarioDto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuarioAtualizado, response.getBody());
    }

    @Test
    void deveDeletarUsuario() {
        String id = "1";
        doNothing().when(usuarioService).deletarUsuario(id);
        ResponseEntity<Void> response = usuarioController.deletarUsuario(id);
        assertEquals(204, response.getStatusCode().value());
    }
}
