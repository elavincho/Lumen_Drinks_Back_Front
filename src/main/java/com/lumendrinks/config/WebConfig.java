package com.lumendrinks.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${lumen.upload-dir}")
    private String directorioSubida;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Exponer la carpeta de subidas física en la URL /uploads/**
        Path rutaAbsoluta = Paths.get(directorioSubida).toAbsolutePath().normalize();
        String localizacion = "file:" + rutaAbsoluta.toString() + "/";
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(localizacion);
    }
}
