package com.lumendrinks.serviciosImpl;

import com.lumendrinks.entidad.ItemCarrito;
import com.lumendrinks.entidad.ItemPedido;
import com.lumendrinks.entidad.Pedido;
import com.lumendrinks.entidad.Usuario;
import com.lumendrinks.repositorio.PedidoRepositorio;
import com.lumendrinks.servicios.CarritoServicio;
import com.lumendrinks.servicios.PedidoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class PedidoServicioImpl implements PedidoServicio {

    private final PedidoRepositorio pedidoRepositorio;
    private final CarritoServicio carritoServicio;
    private final Random random = new Random();

    @Autowired
    public PedidoServicioImpl(PedidoRepositorio pedidoRepositorio, CarritoServicio carritoServicio) {
        this.pedidoRepositorio = pedidoRepositorio;
        this.carritoServicio = carritoServicio;
    }

    @Override
    public Pedido realizarPedido(Usuario usuario) {
        List<ItemCarrito> itemsCarrito = carritoServicio.obtenerCarrito(usuario);
        if (itemsCarrito.isEmpty()) {
            throw new IllegalStateException("No se puede realizar un pedido con el carrito vacío.");
        }

        Double total = carritoServicio.obtenerTotal(usuario);
        
        // Generar número de pedido único
        String numeroPedido;
        do {
            numeroPedido = "#LUMEN-" + String.format("%04d", random.nextInt(10000));
        } while (pedidoRepositorio.findByNumeroPedido(numeroPedido).isPresent());

        // Generar tiempo estimado de preparación realista (entre 3 y 8 minutos)
        int minutos = 3 + random.nextInt(6);
        int segundos = random.nextInt(60);
        String tiempoEstimado = String.format("%02d:%02d min", minutos, segundos);

        Pedido pedido = new Pedido(usuario, LocalDateTime.now(), total, "RECIBIDO", numeroPedido, tiempoEstimado);

        // Copiar ítems del carrito al pedido
        for (ItemCarrito itemCar : itemsCarrito) {
            ItemPedido itemPed = new ItemPedido(
                    pedido,
                    itemCar.getTrago(),
                    itemCar.getTrago().getNombre(),
                    itemCar.getTrago().getPrecio(),
                    itemCar.getCantidad()
            );
            pedido.addItem(itemPed);
        }

        // Guardar el pedido
        Pedido pedidoGuardado = pedidoRepositorio.save(pedido);

        // Vaciar el carrito
        carritoServicio.vaciarCarrito(usuario);

        return pedidoGuardado;
    }

    @Override
    public List<Pedido> listarPedidosPorUsuario(Usuario usuario) {
        return pedidoRepositorio.findByUsuarioOrderByFechaDesc(usuario);
    }

    @Override
    public List<Pedido> listarTodos() {
        return pedidoRepositorio.findAll();
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return pedidoRepositorio.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));
    }

    @Override
    public Pedido buscarPorNumero(String numeroPedido) {
        return pedidoRepositorio.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con número: " + numeroPedido));
    }

    @Override
    public void actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = buscarPorId(id);
        pedido.setEstado(nuevoEstado);
        pedidoRepositorio.save(pedido);
    }
}
