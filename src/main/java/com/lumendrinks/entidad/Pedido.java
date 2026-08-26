package com.lumendrinks.entidad;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = false)
    private String estado; // RECIBIDO, MEZCLANDO, LISTO

    @Column(nullable = false, unique = true)
    private String numeroPedido; // e.g. "#LUMEN-4829"

    private String tiempoEstimado; // e.g. "04:30 min"

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    // Constructors
    public Pedido() {
    }

    public Pedido(Usuario usuario, LocalDateTime fecha, Double total, String estado, String numeroPedido, String tiempoEstimado) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
        this.numeroPedido = numeroPedido;
        this.tiempoEstimado = tiempoEstimado;
    }

    // Helper methods to manage bi-directional relationship
    public void addItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }

    public void removeItem(ItemPedido item) {
        items.remove(item);
        item.setPedido(null);
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getTiempoEstimado() {
        return tiempoEstimado;
    }

    public void setTiempoEstimado(String tiempoEstimado) {
        this.tiempoEstimado = tiempoEstimado;
    }

    public List<ItemPedido> getItems() {
        return items;
    }

    public void setItems(List<ItemPedido> items) {
        this.items = items;
    }
}
