package com.lumendrinks.serviciosImpl;

import com.lumendrinks.entidad.Rol;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.repositorio.UsuarioRepositorio;
import com.lumendrinks.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServicioImpl implements UsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioServicioImpl(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registrar(Usuario usuario) {
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ROLE_CLIENTE);
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setActivo(true);
        return usuarioRepositorio.save(usuario);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email).orElse(null);
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public void cambiarEstadoActivo(Long id) {
        usuarioRepositorio.findById(id).ifPresent(usuario -> {
            usuario.setActivo(!usuario.isActivo());
            usuarioRepositorio.save(usuario);
        });
    }
}
