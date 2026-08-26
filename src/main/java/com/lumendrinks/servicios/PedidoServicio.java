package com.lumendrinks.servicios;

import com.lumendrinks.entidad.Pedido;
import com.lumendrinks.entidad.Usuario;
import java.util.List;

public interface PedidoServicio {
    Pedido realizarPedido(Usuario usuario);
    List<Pedido> listarPedidosPorUsuario(Usuario usuario);
    List<Pedido> listarTodos();
    Pedido buscarPorId(Long id);
    Pedido buscarPorNumero(String numeroPedido);
    void actualizarEstado(Long id, String nuevoEstado);
}
