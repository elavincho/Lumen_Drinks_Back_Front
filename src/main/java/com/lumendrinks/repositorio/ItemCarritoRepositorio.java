package com.lumendrinks.repositorio;

import com.lumendrinks.entidad.ItemCarrito;
import com.lumendrinks.entidad.Trago;
import com.lumendrinks.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemCarritoRepositorio extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByUsuario(Usuario usuario);
    Optional<ItemCarrito> findByUsuarioAndTrago(Usuario usuario, Trago trago);
    void deleteByUsuario(Usuario usuario);
}
