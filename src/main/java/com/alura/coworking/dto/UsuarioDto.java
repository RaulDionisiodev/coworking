package com.alura.coworking.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioDto(
    @NotBlank(message = "O nome é obrigatório")
    String nome,
    String email,
    @NotBlank(message = "O CPF é obrigatório")
    String cpf) {
}
