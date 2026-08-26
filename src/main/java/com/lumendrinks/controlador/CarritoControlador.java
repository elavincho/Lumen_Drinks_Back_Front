package com.lumendrinks.controlador;

import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.servicios.CarritoServicio;
import com.lumendrinks.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoControlador {

    private final CarritoServicio carritoServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public CarritoControlador(CarritoServicio carritoServicio, UsuarioServicio usuarioServicio) {
        this.carritoServicio = carritoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping
    public String verCarrito(Model model, Principal principal) {
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
        model.addAttribute("items", carritoServicio.obtenerCarrito(usuario));
        model.addAttribute("total", carritoServicio.obtenerTotal(usuario));
        return "carrito";
    }

    // AJAX Endpoint to add drinks to cart from home
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarAlCarritoAjax(@RequestParam("tragoId") Long tragoId, Principal principal) {
        Map<String, Object> response = new HashMap<>();
        
        if (principal == null) {
            response.put("success", false);
            response.put("redirect", "/login");
            return ResponseEntity.ok(response);
        }

        try {
            Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
            carritoServicio.agregarAlCarrito(usuario, tragoId, 1);
            
            response.put("success", true);
            response.put("cantidadTotal", carritoServicio.obtenerCantidadTotal(usuario));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Increment item quantity from Cart Page
    @GetMapping("/sumar/{id}")
    public String sumarItem(@PathVariable("id") Long tragoId, Principal principal) {
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
        carritoServicio.agregarAlCarrito(usuario, tragoId, 1);
        return "redirect:/carrito";
    }

    // Decrement item quantity from Cart Page
    @GetMapping("/restar/{id}")
    public String restarItem(@PathVariable("id") Long tragoId, Principal principal) {
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
        carritoServicio.restarDelCarrito(usuario, tragoId);
        return "redirect:/carrito";
    }

    // Delete item from Cart Page
    @GetMapping("/eliminar/{id}")
    public String eliminarItem(@PathVariable("id") Long tragoId, Principal principal) {
        Usuario usuario = usuarioServicio.buscarPorEmail(principal.getName());
        carritoServicio.eliminarDelCarrito(usuario, tragoId);
        return "redirect:/carrito";
    }
}
