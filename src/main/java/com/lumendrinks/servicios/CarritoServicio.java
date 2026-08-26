package com.lumendrinks.servicios;

import com.lumendrinks.entidad.ItemCarrito;
import com.lumendrinks.entidad.Usuario;
import java.util.List;

public interface CarritoServicio {
    List<ItemCarrito> obtenerCarrito(Usuario usuario);
    void agregarAlCarrito(Usuario usuario, Long tragoId, Integer cantidad);
    void restarDelCarrito(Usuario usuario, Long tragoId);
    void eliminarDelCarrito(Usuario usuario, Long tragoId);
    void vaciarCarrito(Usuario usuario);
    Double obtenerTotal(Usuario usuario);
    Integer obtenerCantidadTotal(Usuario usuario);
}
