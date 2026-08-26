package com.lumendrinks.controlador;

import com.lumendrinks.entidad.Pedido;
import com.lumendrinks.entidad.Trago;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.servicios.PedidoServicio;
import com.lumendrinks.servicios.TragoServicio;
import com.lumendrinks.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    private final TragoServicio tragoServicio;
    private final PedidoServicio pedidoServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public AdminControlador(TragoServicio tragoServicio, PedidoServicio pedidoServicio, UsuarioServicio usuarioServicio) {
        this.tragoServicio = tragoServicio;
        this.pedidoServicio = pedidoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Pedido> todosPedidos = pedidoServicio.listarTodos();
        
        // Ordenar pedidos por fecha de forma descendente para mostrar los recientes primero
        List<Pedido> recientes = todosPedidos.stream()
                .sorted(Comparator.comparing(Pedido::getFecha).reversed())
                .limit(6)
                .collect(Collectors.toList());

        model.addAttribute("tragosCount", tragoServicio.listarTodos().size());
        model.addAttribute("pedidosCount", todosPedidos.size());
        model.addAttribute("usuariosCount", usuarioServicio.listarTodos().size());
        model.addAttribute("pedidosRecientes", recientes);
        
        return "admin/dashboard";
    }

    // List of drinks (CRUD view)
    @GetMapping("/tragos")
    public String listarTragos(Model model) {
        model.addAttribute("tragos", tragoServicio.listarTodos());
        return "admin/tragos";
    }

    // New drink form
    @GetMapping("/tragos/nuevo")
    public String nuevoTragoForm(Model model) {
        model.addAttribute("trago", new Trago());
        return "admin/formulario-trago";
    }

    // Edit drink form
    @GetMapping("/tragos/editar/{id}")
    public String editarTragoForm(@PathVariable("id") Long id, Model model) {
        try {
            Trago trago = tragoServicio.buscarPorId(id);
            model.addAttribute("trago", trago);
            return "admin/formulario-trago";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo cargar el trago: " + e.getMessage());
            return "redirect:/admin/tragos";
        }
    }

    // Save or update drink (multipart file upload support)
    @PostMapping("/tragos/guardar")
    public String guardarTrago(@ModelAttribute("trago") Trago trago,
                               @RequestParam("archivoImagen") MultipartFile archivoImagen,
                               RedirectAttributes redirectAttributes) {
        try {
            if (trago.getId() == null) {
                // Crear nuevo
                tragoServicio.crear(trago, archivoImagen);
                redirectAttributes.addFlashAttribute("mensaje", "Trago creado exitosamente.");
            } else {
                // Editar existente
                tragoServicio.editar(trago.getId(), trago, archivoImagen);
                redirectAttributes.addFlashAttribute("mensaje", "Trago actualizado exitosamente.");
            }
            return "redirect:/admin/tragos";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el trago: " + e.getMessage());
            return "redirect:/admin/tragos";
        }
    }

    // Delete drink from database and file system
    @GetMapping("/tragos/eliminar/{id}")
    public String eliminarTrago(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            tragoServicio.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Trago eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el trago: " + e.getMessage());
        }
        return "redirect:/admin/tragos";
    }

    // Orders management page
    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        List<Pedido> todosPedidos = pedidoServicio.listarTodos().stream()
                .sorted(Comparator.comparing(Pedido::getFecha).reversed())
                .collect(Collectors.toList());
        model.addAttribute("pedidos", todosPedidos);
        return "admin/pedidos";
    }

    // Update order status inline
    @PostMapping("/pedidos/actualizar-estado")
    public String actualizarEstadoPedido(@RequestParam("id") Long id,
                                         @RequestParam("nuevoEstado") String nuevoEstado,
                                         RedirectAttributes redirectAttributes) {
        try {
            pedidoServicio.actualizarEstado(id, nuevoEstado);
            redirectAttributes.addFlashAttribute("mensaje", "Estado del pedido actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el estado: " + e.getMessage());
        }
        return "redirect:/admin/pedidos";
    }

    // Users list page
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioServicio.listarTodos());
        return "admin/usuarios";
    }

    // Toggle active status (block / unblock)
    @GetMapping("/usuarios/estado/{id}")
    public String cambiarEstadoUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioServicio.cambiarEstadoActivo(id);
            redirectAttributes.addFlashAttribute("mensaje", "Estado del usuario modificado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al modificar estado del usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }
}
