package com.alura.coworking.repository;

import com.alura.coworking.domain.Usuario;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryIT {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void deveVerificarExistenciaPorCpf() {
        Usuario usuario = new Usuario();
        usuario.setCpf("12345678900");
        usuarioRepository.save(usuario);

        boolean existe = usuarioRepository.existsByCpf("12345678900");
        assertThat(existe).isTrue();
    }

    @AfterAll
    static void limparUsuarios(@Autowired UsuarioRepository usuarioRepository) {
        usuarioRepository.deleteAll();
    }
}
