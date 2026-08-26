package com.lumendrinks.servicios;

import com.lumendrinks.entidad.Usuario;
import java.util.List;

public interface UsuarioServicio {
    Usuario registrar(Usuario usuario);
    Usuario buscarPorEmail(String email);
    List<Usuario> listarTodos();
    void cambiarEstadoActivo(Long id);
}
