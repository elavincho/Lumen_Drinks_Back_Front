package com.lumendrinks.controlador;

import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.servicios.CarritoServicio;
import com.lumendrinks.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class CommonModelAttributeAdvice {

    private final UsuarioServicio usuarioServicio;
    private final CarritoServicio carritoServicio;

    @Autowired
    public CommonModelAttributeAdvice(UsuarioServicio usuarioServicio, CarritoServicio carritoServicio) {
        this.usuarioServicio = usuarioServicio;
        this.carritoServicio = carritoServicio;
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
            if (usuario != null) {
                model.addAttribute("usuarioAutenticado", usuario);
                model.addAttribute("cantCarrito", carritoServicio.obtenerCantidadTotal(usuario));
                return;
            }
        }
        
        model.addAttribute("usuarioAutenticado", null);
        model.addAttribute("cantCarrito", 0);
    }
}
