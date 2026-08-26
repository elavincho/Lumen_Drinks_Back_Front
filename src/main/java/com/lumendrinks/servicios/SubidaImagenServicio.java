package com.lumendrinks.servicios;

import org.springframework.web.multipart.MultipartFile;

public interface SubidaImagenServicio {
    String guardarImagen(MultipartFile archivo);
    void eliminarImagen(String imagenUrl);
}
