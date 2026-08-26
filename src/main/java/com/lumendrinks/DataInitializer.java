package com.lumendrinks;

import com.lumendrinks.entidad.Rol;
import com.lumendrinks.entidad.Trago;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.repositorio.TragoRepositorio;
import com.lumendrinks.repositorio.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepositorio usuarioRepositorio;
    private final TragoRepositorio tragoRepositorio;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UsuarioRepositorio usuarioRepositorio, TragoRepositorio tragoRepositorio, PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.tragoRepositorio = tragoRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        inicializarUsuarios();
        inicializarTragos();
    }

    private void inicializarUsuarios() {
        if (usuarioRepositorio.count() == 0) {
            // Crear Administrador
            Usuario admin = new Usuario();
            admin.setNombre("Lumen Admin");
            admin.setEmail("admin@lumen.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ROLE_ADMIN);
            admin.setActivo(true);
            usuarioRepositorio.save(admin);

            // Crear Cliente de Prueba
            Usuario cliente = new Usuario();
            cliente.setNombre("Juan Pérez");
            cliente.setEmail("cliente@lumen.com");
            cliente.setPassword(passwordEncoder.encode("cliente123"));
            cliente.setRol(Rol.ROLE_CLIENTE);
            cliente.setActivo(true);
            usuarioRepositorio.save(cliente);

            System.out.println("Usuarios por defecto inicializados (admin@lumen.com y cliente@lumen.com).");
        }
    }

    private void inicializarTragos() {
        if (tragoRepositorio.count() == 0) {
            List<Trago> tragos = new ArrayList<>();

            tragos.add(new Trago(
                    "Ruby Rosemary Spritz",
                    "insignia",
                    12.50,
                    "Insignia de la Casa",
                    "/images/ruby_spritz.png",
                    "Gin boutique, jugo fresco de pomelo rubí, romero flameado y tónica cristalina.",
                    "Nuestro cóctel estrella insuflado con los aromas de la barra. Utiliza pomelos rubí seleccionados a mano, macerados en frío con romero silvestre flameado al momento para liberar sus aceites esenciales.",
                    "Gin Boutique, Jugo de Pomelo Rubí, Licor de Flor de Saúco, Romero Flameado, Tónica Premium, Hielo Cristal",
                    95, 85, 40, "14%"
            ));

            tragos.add(new Trago(
                    "Smoked Citrus Bourbon",
                    "ahumados",
                    14.00,
                    "Ahumado a la Madera",
                    "/images/smoked_bourbon.png",
                    "Bourbon añejo 8 años, bitters de naranja, sirope de romero y humo de virutas de roble.",
                    "Una experiencia ahumada inmersiva. Servido en vaso rock con hielo de fusión lenta y infusionado bajo campana con humo aromático de madera de roble tostado.",
                    "Bourbon Añejo 8 Años, Bitters de Naranja, Sirope de Romero, Piel de Pomelo Flameada, Humo de Roble",
                    60, 70, 35, "22%"
            ));

            tragos.add(new Trago(
                    "Midnight Berry Fizz",
                    "citricos",
                    11.50,
                    "Frutal & Efervescente",
                    "/images/berry_fizz.png",
                    "Vodka ultra-premium, macerado de frutos rojos, limón eureka y soda de flor de hibisco.",
                    "Burbujas de tono violeta profundo y sabor vibrante. La dulzura natural de las moras silvestres se equilibra con la acidez refrescante del limón y el hibisco.",
                    "Vodka Ultra-Premium, Macerado de Moras, Licor de Cassis, Jugo de Limón Eureka, Soda de Hibisco",
                    75, 30, 65, "12%"
            ));

            tragos.add(new Trago(
                    "Crimson Grapefruit Paloma",
                    "citricos",
                    13.00,
                    "Cítrico Imperial",
                    "https://images.unsplash.com/photo-1514362545857-3bc16c4c7d1b?auto=format&fit=crop&w=800&q=80",
                    "Mezcal silvestre 100% agave, jugo de pomelo rosado, cordial de lima y sal volcánica.",
                    "Redefinición mexicana del clásico Paloma: notas de agaves ahumados ensambladas con la acidez limpia del pomelo rosa y escarchado de sal negra de romero.",
                    "Mezcal Silvestre 100% Agave, Jugo de Pomelo Rubí, Cordial de Lima, Sal Volcánica de Romero, Soda Cristal",
                    90, 65, 40, "16%"
            ));

            tragos.add(new Trago(
                    "Botanical Velvet Tonic",
                    "insignia",
                    11.00,
                    "Herbal Elegante",
                    "https://images.unsplash.com/photo-1551024709-8f23befc6f87?auto=format&fit=crop&w=800&q=80",
                    "Gin infusionado en pepino y romero, licor de saúco, tónica artesanal y bayas de enebro.",
                    "Trago equilibrado de perfil floral y textura sedosa. La infusión botánica resalta la frescura del enebro y la rodaja de pomelo rubí.",
                    "Gin Infusionado en Pepino, Licor de Saúco, Tónica Artesanal, Bayas de Enebro, Pomelo Rubí",
                    65, 90, 45, "13%"
            ));

            tragos.add(new Trago(
                    "Emerald Mint Julep",
                    "ahumados",
                    12.00,
                    "Refrescante",
                    "https://images.unsplash.com/photo-1556881286-fc6915169721?auto=format&fit=crop&w=800&q=80",
                    "Whiskey de centeno, menta fresca del huerto, azúcar virgen y perfume de romero tostado.",
                    "Clásico sureño renovado con menta recién molienda, hielo triturado de lenta fusión y perfume ahumado de romero tostado en copa de plata.",
                    "Whiskey de Centeno, Menta Fresca de Huerto, Azúcar de Caña Virgen, Hielo Frappé, Perfume de Romero",
                    40, 95, 60, "18%"
            ));

            tragos.add(new Trago(
                    "Golden Passion Negroni",
                    "ahumados",
                    14.50,
                    "Amargo de Autor",
                    "https://images.unsplash.com/photo-1560512823-829485b8bf24?auto=format&fit=crop&w=800&q=80",
                    "Gin London Dry, Campari italiano, Vermut rojo con maracuyá y aceites de pomelo.",
                    "Giro exótico e intrigante al Negroni tradicional. Equilibra el amargor característico del Campari con notas tropicales de maracuyá y aceites cítricos.",
                    "Gin London Dry, Campari Italiano, Vermut Rojo con Maracuyá, Aceites de Pomelo Rubí",
                    65, 50, 35, "24%"
            ));

            tragos.add(new Trago(
                    "Sparkling Rosemary Mocktail",
                    "sin-alcohol",
                    8.50,
                    "Sin Alcohol 0.0%",
                    "https://images.unsplash.com/photo-1621263764928-df1444c5e859?auto=format&fit=crop&w=800&q=80",
                    "Cordial de pomelo rubí, romero fresco, jugo de lima, manzanilla y soda carbonatada.",
                    "Cóctel botánico sin alcohol diseñado con la misma sofisticación. Todo el perfil aromático de cítricos y hierbas sin graduación alcohólica.",
                    "Cordial de Pomelo Rubí, Rama de Romero Fresco, Jugo de Lima Eureka, Extracto de Manzanilla, Soda Artesanal",
                    90, 80, 45, "0.0%"
            ));

            tragos.add(new Trago(
                    "Tropical Coral Dragon",
                    "citricos",
                    13.50,
                    "Exótico Coral",
                    "https://images.unsplash.com/photo-1536935338788-846bb9981813?auto=format&fit=crop&w=800&q=80",
                    "Ron blanco de coco, pitahaya roja, jugo de pomelo rubí, triple sec y bitters de romero.",
                    "Un trago visualmente deslumbrante de tono coral intenso. Fusiona frutas exóticas tropicales con la acidez distintiva de nuestra barra.",
                    "Ron Blanco de Coco, Reducción de Pitahaya Roja, Jugo de Pomelo Rubí, Triple Sec, Bitters de Romero",
                    75, 40, 70, "15%"
            ));

            tragos.add(new Trago(
                    "Aromatic Mezcalita Rosada",
                    "insignia",
                    13.50,
                    "Especialidad Mezcal",
                    "https://images.unsplash.com/photo-1572116469696-31de0f17cc34?auto=format&fit=crop&w=800&q=80",
                    "Mezcal joven ahumado, pomelo rubí, licor Ancho Reyes, jarabe de agave y sal de chile.",
                    "Carácter ahumado profundo combinado con el dulzor orgánico del agave y el picante suave del chile en el borde del vaso.",
                    "Mezcal Joven Ahumado, Extracto de Pomelo Rubí, Licor Ancho Reyes, Jarabe de Agave, Sal de Romero & Chile",
                    80, 60, 50, "17%"
            ));

            tragoRepositorio.saveAll(tragos);
            System.out.println("Los 10 tragos iniciales han sido registrados en la base de datos.");
        }
    }
}
