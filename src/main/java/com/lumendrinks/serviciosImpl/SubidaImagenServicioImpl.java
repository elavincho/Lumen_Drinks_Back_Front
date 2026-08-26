package com.lumendrinks.serviciosImpl;

import com.lumendrinks.servicios.SubidaImagenServicio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class SubidaImagenServicioImpl implements SubidaImagenServicio {

    @Value("${lumen.upload-dir}")
    private String directorioSubida;

    @Override
    public String guardarImagen(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

        try {
            // Asegurar que el directorio de subidas exista
            Path directorioPath = Paths.get(directorioSubida).toAbsolutePath().normalize();
            if (!Files.exists(directorioPath)) {
                Files.createDirectories(directorioPath);
            }

            // Generar un nombre único para el archivo
            String nombreOriginal = archivo.getOriginalFilename();
            String extension = "";
            if (nombreOriginal != null && nombreOriginal.contains(".")) {
                extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
            }
            String nombreUnico = UUID.randomUUID().toString() + extension;

            // Ruta final del archivo
            Path destinoPath = directorioPath.resolve(nombreUnico);

            // Copiar el archivo al disco
            Files.copy(archivo.getInputStream(), destinoPath, StandardCopyOption.REPLACE_EXISTING);

            // Retornar la URL relativa para el navegador
            return "/uploads/" + nombreUnico;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la imagen en el servidor: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminarImagen(String imagenUrl) {
        if (imagenUrl == null || !imagenUrl.startsWith("/uploads/")) {
            return;
        }

        try {
            String nombreArchivo = imagenUrl.replace("/uploads/", "");
            Path directorioPath = Paths.get(directorioSubida).toAbsolutePath().normalize();
            Path archivoPath = directorioPath.resolve(nombreArchivo);

            if (Files.exists(archivoPath)) {
                Files.delete(archivoPath);
            }
        } catch (IOException e) {
            System.err.println("No se pudo eliminar la imagen: " + imagenUrl + ". Error: " + e.getMessage());
        }
    }
}
