package com.lumendrinks.serviciosImpl;

import com.lumendrinks.entidad.ItemCarrito;
import com.lumendrinks.entidad.Trago;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.repositorio.ItemCarritoRepositorio;
import com.lumendrinks.repositorio.TragoRepositorio;
import com.lumendrinks.servicios.CarritoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarritoServicioImpl implements CarritoServicio {

    private final ItemCarritoRepositorio itemCarritoRepositorio;
    private final TragoRepositorio tragoRepositorio;

    @Autowired
    public CarritoServicioImpl(ItemCarritoRepositorio itemCarritoRepositorio, TragoRepositorio tragoRepositorio) {
        this.itemCarritoRepositorio = itemCarritoRepositorio;
        this.tragoRepositorio = tragoRepositorio;
    }

    @Override
    public List<ItemCarrito> obtenerCarrito(Usuario usuario) {
        return itemCarritoRepositorio.findByUsuario(usuario);
    }

    @Override
    public void agregarAlCarrito(Usuario usuario, Long tragoId, Integer cantidad) {
        Trago trago = tragoRepositorio.findById(tragoId)
                .orElseThrow(() -> new IllegalArgumentException("Trago no encontrado con ID: " + tragoId));

        Optional<ItemCarrito> itemExistente = itemCarritoRepositorio.findByUsuarioAndTrago(usuario, trago);

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            itemCarritoRepositorio.save(item);
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(trago, cantidad, usuario);
            itemCarritoRepositorio.save(nuevoItem);
        }
    }

    @Override
    public void restarDelCarrito(Usuario usuario, Long tragoId) {
        Trago trago = tragoRepositorio.findById(tragoId)
                .orElseThrow(() -> new IllegalArgumentException("Trago no encontrado con ID: " + tragoId));

        Optional<ItemCarrito> itemExistente = itemCarritoRepositorio.findByUsuarioAndTrago(usuario, trago);

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            if (item.getCantidad() > 1) {
                item.setCantidad(item.getCantidad() - 1);
                itemCarritoRepositorio.save(item);
            } else {
                itemCarritoRepositorio.delete(item);
            }
        }
    }

    @Override
    public void eliminarDelCarrito(Usuario usuario, Long tragoId) {
        Trago trago = tragoRepositorio.findById(tragoId)
                .orElseThrow(() -> new IllegalArgumentException("Trago no encontrado con ID: " + tragoId));

        itemCarritoRepositorio.findByUsuarioAndTrago(usuario, trago)
                .ifPresent(itemCarritoRepositorio::delete);
    }

    @Override
    public void vaciarCarrito(Usuario usuario) {
        itemCarritoRepositorio.deleteByUsuario(usuario);
    }

    @Override
    public Double obtenerTotal(Usuario usuario) {
        return obtenerCarrito(usuario).stream()
                .mapToDouble(ItemCarrito::getSubtotal)
                .sum();
    }

    @Override
    public Integer obtenerCantidadTotal(Usuario usuario) {
        return obtenerCarrito(usuario).stream()
                .mapToInt(ItemCarrito::getCantidad)
                .sum();
    }
}
