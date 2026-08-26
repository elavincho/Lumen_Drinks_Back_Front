package com.lumendrinks.servicios;

import com.lumendrinks.entidad.Trago;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface TragoServicio {
    List<Trago> listarTodos();
    List<Trago> listarPorCategoria(String categoria);
    Trago buscarPorId(Long id);
    Trago crear(Trago trago, MultipartFile imagen);
    Trago editar(Long id, Trago tragoDetalles, MultipartFile nuevaImagen);
    void eliminar(Long id);
}
