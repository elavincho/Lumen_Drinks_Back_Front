package com.lumendrinks.repositorio;

import com.lumendrinks.entidad.Pedido;
import com.lumendrinks.entidad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepositorio extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioOrderByFechaDesc(Usuario usuario);
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
}
