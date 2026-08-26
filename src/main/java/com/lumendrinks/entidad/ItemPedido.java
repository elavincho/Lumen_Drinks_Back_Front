package com.lumendrinks.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "items_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trago_id", nullable = true) // nullable para mantener pedidos aunque se elimine un trago
    private Trago trago;

    @Column(nullable = false)
    private String nombreTrago;

    @Column(nullable = false)
    private Double precioTrago;

    @Column(nullable = false)
    private Integer cantidad;

    // Constructors
    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Trago trago, String nombreTrago, Double precioTrago, Integer cantidad) {
        this.pedido = pedido;
        this.trago = trago;
        this.nombreTrago = nombreTrago;
        this.precioTrago = precioTrago;
        this.cantidad = cantidad;
    }

    // Helper method to get total price for this line item
    public Double getSubtotal() {
        return this.precioTrago * this.cantidad;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Trago getTrago() {
        return trago;
    }

    public void setTrago(Trago trago) {
        this.trago = trago;
    }

    public String getNombreTrago() {
        return nombreTrago;
    }

    public void setNombreTrago(String nombreTrago) {
        this.nombreTrago = nombreTrago;
    }

    public Double getPrecioTrago() {
        return precioTrago;
    }

    public void setPrecioTrago(Double precioTrago) {
        this.precioTrago = precioTrago;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
