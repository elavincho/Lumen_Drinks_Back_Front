package com.lumendrinks.entidad;

import jakarta.persistence.*;

@Entity
@Table(name = "items_carrito")
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trago_id", nullable = false)
    private Trago trago;

    @Column(nullable = false)
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Constructors
    public ItemCarrito() {
    }

    public ItemCarrito(Trago trago, Integer cantidad, Usuario usuario) {
        this.trago = trago;
        this.cantidad = cantidad;
        this.usuario = usuario;
    }

    // Helper method to get total price for this item
    public Double getSubtotal() {
        if (trago == null || cantidad == null) {
            return 0.0;
        }
        return trago.getPrecio() * cantidad;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trago getTrago() {
        return trago;
    }

    public void setTrago(Trago trago) {
        this.trago = trago;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
