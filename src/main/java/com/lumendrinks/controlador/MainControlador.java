package com.lumendrinks.controlador;

import com.lumendrinks.entidad.Trago;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.servicios.TragoServicio;
import com.lumendrinks.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainControlador {

    private final TragoServicio tragoServicio;
    private final UsuarioServicio usuarioServicio;

    @Autowired
    public MainControlador(TragoServicio tragoServicio, UsuarioServicio usuarioServicio) {
        this.tragoServicio = tragoServicio;
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Trago> tragos = tragoServicio.listarTodos();
        model.addAttribute("tragos", tragos);

        // Buscar el trago insignia para el Hero (ID 1 o el primero que sea insignia)
        Trago tragoInsignia = tragos.stream()
                .filter(t -> t.getCategoria().equalsIgnoreCase("insignia"))
                .findFirst()
                .orElse(tragos.isEmpty() ? null : tragos.get(0));
        
        model.addAttribute("tragoInsignia", tragoInsignia);
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        
        if (usuarioServicio.buscarPorEmail(usuario.getEmail()) != null) {
            model.addAttribute("error", "El correo electrónico ya está registrado en el sistema.");
            return "registro";
        }

        try {
            usuarioServicio.registrar(usuario);
            redirectAttributes.addFlashAttribute("registroExitoso", true);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Hubo un error inesperado al registrar la cuenta: " + e.getMessage());
            return "registro";
        }
    }
}
