package com.alura.coworking.service;

import com.alura.coworking.dto.UsuarioDto;

import java.util.List;

public interface UsuarioService {

    UsuarioDto criarUsuario(UsuarioDto usuarioDto);

    List<UsuarioDto> listarUsuarios();

    UsuarioDto buscarUsuarioPorId(String id);

    void deletarUsuario(String id);

    UsuarioDto atualizarUsuario(String id, UsuarioDto usuarioDto);
}
