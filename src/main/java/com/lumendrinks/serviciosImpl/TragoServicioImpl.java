package com.lumendrinks.serviciosImpl;

import com.lumendrinks.entidad.Trago;
import com.lumendrinks.repositorio.TragoRepositorio;
import com.lumendrinks.servicios.SubidaImagenServicio;
import com.lumendrinks.servicios.TragoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TragoServicioImpl implements TragoServicio {

    private final TragoRepositorio tragoRepositorio;
    private final SubidaImagenServicio subidaImagenServicio;

    @Autowired
    public TragoServicioImpl(TragoRepositorio tragoRepositorio, SubidaImagenServicio subidaImagenServicio) {
        this.tragoRepositorio = tragoRepositorio;
        this.subidaImagenServicio = subidaImagenServicio;
    }

    @Override
    public List<Trago> listarTodos() {
        return tragoRepositorio.findAll();
    }

    @Override
    public List<Trago> listarPorCategoria(String categoria) {
        if (categoria == null || categoria.equals("all") || categoria.isEmpty()) {
            return tragoRepositorio.findAll();
        }
        return tragoRepositorio.findByCategoria(categoria);
    }

    @Override
    public Trago buscarPorId(Long id) {
        return tragoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trago no encontrado con ID: " + id));
    }

    @Override
    public Trago crear(Trago trago, MultipartFile imagen) {
        if (imagen != null && !imagen.isEmpty()) {
            String urlImagen = subidaImagenServicio.guardarImagen(imagen);
            trago.setImagenUrl(urlImagen);
        }
        return tragoRepositorio.save(trago);
    }

    @Override
    public Trago editar(Long id, Trago tragoDetalles, MultipartFile nuevaImagen) {
        Trago tragoExistente = buscarPorId(id);

        tragoExistente.setNombre(tragoDetalles.getNombre());
        tragoExistente.setCategoria(tragoDetalles.getCategoria());
        tragoExistente.setPrecio(tragoDetalles.getPrecio());
        tragoExistente.setBadge(tragoDetalles.getBadge());
        tragoExistente.setDescripcionCorta(tragoDetalles.getDescripcionCorta());
        tragoExistente.setDescripcionLarga(tragoDetalles.getDescripcionLarga());
        tragoExistente.setIngredientes(tragoDetalles.getIngredientes());
        
        // Perfil de Sabor
        tragoExistente.setCitrico(tragoDetalles.getCitrico());
        tragoExistente.setHerbal(tragoDetalles.getHerbal());
        tragoExistente.setDulzura(tragoDetalles.getDulzura());
        tragoExistente.setAlcohol(tragoDetalles.getAlcohol());

        // Manejar subida de nueva imagen
        if (nuevaImagen != null && !nuevaImagen.isEmpty()) {
            // Eliminar imagen vieja si existía
            if (tragoExistente.getImagenUrl() != null) {
                subidaImagenServicio.eliminarImagen(tragoExistente.getImagenUrl());
            }
            // Guardar nueva imagen
            String urlImagen = subidaImagenServicio.guardarImagen(nuevaImagen);
            tragoExistente.setImagenUrl(urlImagen);
        }

        return tragoRepositorio.save(tragoExistente);
    }

    @Override
    public void eliminar(Long id) {
        Trago trago = buscarPorId(id);
        
        // Eliminar archivo del disco si existe
        if (trago.getImagenUrl() != null) {
            subidaImagenServicio.eliminarImagen(trago.getImagenUrl());
        }
        
        tragoRepositorio.delete(trago);
    }
}
