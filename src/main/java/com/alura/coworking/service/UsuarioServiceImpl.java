package com.alura.coworking.service;

import com.alura.coworking.domain.Usuario;
import com.alura.coworking.dto.UsuarioDto;
import com.alura.coworking.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService{

   private final UsuarioRepository usuarioRepository;


   @Autowired
   public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
       this.usuarioRepository = usuarioRepository;
   }


    @Override
    public UsuarioDto criarUsuario(UsuarioDto usuarioDto) {
        Usuario usuario = new Usuario(
                usuarioDto.nome(),
                usuarioDto.email(),
                usuarioDto.cpf()
        );

       usuario = usuarioRepository.save(usuario);

       return  new UsuarioDto(
               usuario.getNome(),
               usuario.getEmail(),
               usuario.getCpf()
       );
    }

    @Override
    public List<UsuarioDto> listarUsuarios() {
       List <UsuarioDto> usuarios = new ArrayList<>();
       List<Usuario> usuarioList = usuarioRepository.findAll();

       usuarioList.forEach(u -> usuarios.add(new UsuarioDto(
               u.getNome(),
               u.getEmail(),
               u.getCpf()
       )));

         return usuarios;
    }

    @Override
    public UsuarioDto buscarUsuarioPorId(String id) {
       Usuario usuario = usuarioRepository.findById(id).orElse(null);
         if (usuario != null) {
                return new UsuarioDto(
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getCpf()
                );
         }
       return null;
    }

    @Override
    public void deletarUsuario(String id) {
        usuarioRepository.findById(id).ifPresent(usuarioRepository::delete);
    }

    @Override
    public UsuarioDto atualizarUsuario(String id, UsuarioDto usuarioDto) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setNome(usuarioDto.nome());
            usuario.setEmail(usuarioDto.email());
            usuario.setCpf(usuarioDto.cpf());
            usuario = usuarioRepository.save(usuario);
            return new UsuarioDto(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getCpf()
            );
        }
        return null;
    }
}
