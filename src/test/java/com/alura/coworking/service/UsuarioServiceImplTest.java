package com.alura.coworking.service;

import com.alura.coworking.domain.Usuario;
import com.alura.coworking.dto.UsuarioDto;
import com.alura.coworking.exception.CpfJaCadastradoException;
import com.alura.coworking.exception.UsuarioNaoEncontradoException;
import com.alura.coworking.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    public UsuarioServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCriarUsuarioQuandoCpfNaoExiste() {
        UsuarioDto dto = new UsuarioDto("João", "joao@email.com", "12345678900");
        when(usuarioRepository.existsByCpf(dto.cpf())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDto resultado = usuarioService.criarUsuario(dto);

        assertEquals(dto.nome(), resultado.nome());
        assertEquals(dto.email(), resultado.email());
        assertEquals(dto.cpf(), resultado.cpf());
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaCadastrado() {
        UsuarioDto dto = new UsuarioDto("Maria", "maria@email.com", "12345678900");
        when(usuarioRepository.existsByCpf(dto.cpf())).thenReturn(true);

        assertThrows(CpfJaCadastradoException.class, () -> usuarioService.criarUsuario(dto));
    }

    @Test
    void deveListarUsuarios() {
        Usuario usuario1 = new Usuario("Ana", "ana@email.com", "11111111111");
        Usuario usuario2 = new Usuario("Bruno", "bruno@email.com", "22222222222");
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<UsuarioDto> usuarios = usuarioService.listarUsuarios();

        assertEquals(2, usuarios.size());
        assertEquals("Ana", usuarios.get(0).nome());
        assertEquals("Bruno", usuarios.get(1).nome());
    }

    @Test
    void deveDeletarUsuario() {
        String id = "1";
        Usuario usuario = new Usuario("Carlos", "carlos@email.com", "33333333333");
        when(usuarioRepository.existsById(id)).thenReturn(true);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(id);

        usuarioService.deletarUsuario(id);

        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        String id = "2";
        when(usuarioRepository.existsById(id)).thenReturn(false);

        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.deletarUsuario(id));
    }

    @Test
    void deveBuscarUsuarioPorId() {
        String id = "1";
        Usuario usuario = new Usuario("Diana", "diana@email.com", "44444444444");
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        UsuarioDto resultado = usuarioService.buscarUsuarioPorId(id);
        assertEquals("Diana", resultado.nome());
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        String id = "2";
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.buscarUsuarioPorId(id));
    }

    @Test
    void deveAtualizarUsuario() {
        String id = "1";
        Usuario usuario = new Usuario("Eduardo", "edu@email.com", "00000000000");
        UsuarioDto dto = new UsuarioDto("Eduardo", "eduardo@email.com", "00000000000");
        Usuario usuarioAtualizado = new Usuario("Eduardo", "eduardo@email.com", "00000000000");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuarioAtualizado)).thenReturn(usuario);

        UsuarioDto result = usuarioService.atualizarUsuario(id, dto);

        assertEquals(usuarioAtualizado.getCpf(), result.cpf());

    }

    @Test
    void deveDispararExcessaoAoAtualizarUsuarioInexistente(){
        String id = "2";
        UsuarioDto dto = new UsuarioDto("Eduardo", "eduardo@email.com", "00000000000");
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(UsuarioNaoEncontradoException.class, () -> usuarioService.atualizarUsuario(id, dto));
    }
}
