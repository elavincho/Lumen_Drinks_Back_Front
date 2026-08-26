package com.lumendrinks.controlador;

import com.lumendrinks.entidad.Pedido;
import com.lumendrinks.entidad.Rol;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.servicios.PedidoServicio;
import com.lumendrinks.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/pedido")
public class PedidoControlador {

    private final PedidoServicio pedidoServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public PedidoControlador(PedidoServicio pedidoServicio, UsuarioServicio usuarioServicio) {
        this.pedidoServicio = pedidoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    // Checkout: transforms cart items into a real database Order
    @PostMapping("/checkout")
    public String procesarCheckout(Principal principal) {
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
        try {
            Pedido pedido = pedidoServicio.realizarPedido(usuario);
            return "redirect:/pedido/estado/" + pedido.getNumeroPedido().replace("#", "");
        } catch (Exception e) {
            return "redirect:/carrito?error=" + e.getMessage();
        }
    }

    // Tracker page (reads order tracking number)
    @GetMapping("/estado/{numero}")
    public String verEstadoPedido(@PathVariable("numero") String numero, Model model, Principal principal) {
        String numFormateado = "#" + numero;
        Pedido pedido = pedidoServicio.buscarPorNumero(numFormateado);
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());

        // Seguridad: solo el dueño del pedido o un administrador pueden ver el estado del pedido
        if (!pedido.getUsuario().getId().equals(usuario.getId()) && usuario.getRol() != Rol.ROLE_ADMIN) {
            return "redirect:/?denegado=true";
        }

        model.addAttribute("pedido", pedido);
        return "pedido-status";
    }
}
