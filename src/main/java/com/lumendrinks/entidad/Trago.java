package com.lumendrinks.entidad;

import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "tragos")
public class Trago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String categoria; // insignia, citricos, ahumados, sin-alcohol

    @Column(nullable = false)
    private Double precio;

    private String badge;

    private String imagenUrl;

    @Column(length = 500)
    private String descripcionCorta;

    @Column(length = 2000)
    private String descripcionLarga;

    @Column(length = 1000)
    private String ingredientes; // Almacenado como texto separado por comas para simplicidad

    // Perfil de Sabor
    private Integer citrico;
    private Integer herbal;
    private Integer dulzura;
    private String alcohol;

    // Constructors
    public Trago() {
    }

    public Trago(String nombre, String categoria, Double precio, String badge, String imagenUrl,
                 String descripcionCorta, String descripcionLarga, String ingredientes,
                 Integer citrico, Integer herbal, Integer dulzura, String alcohol) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.badge = badge;
        this.imagenUrl = imagenUrl;
        this.descripcionCorta = descripcionCorta;
        this.descripcionLarga = descripcionLarga;
        this.ingredientes = ingredientes;
        this.citrico = citrico;
        this.herbal = herbal;
        this.dulzura = dulzura;
        this.alcohol = alcohol;
    }

    // Helper method to get ingredients as a List for Thymeleaf frontend iterations
    public List<String> getIngredientesList() {
        if (this.ingredientes == null || this.ingredientes.isEmpty()) {
            return Arrays.asList();
        }
        return Arrays.stream(this.ingredientes.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }

    public void setDescripcionLarga(String descripcionLarga) {
        this.descripcionLarga = descripcionLarga;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Integer getCitrico() {
        return citrico;
    }

    public void setCitrico(Integer citrico) {
        this.citrico = citrico;
    }

    public Integer getHerbal() {
        return herbal;
    }

    public void setHerbal(Integer herbal) {
        this.herbal = herbal;
    }

    public Integer getDulzura() {
        return dulzura;
    }

    public void setDulzura(Integer dulzura) {
        this.dulzura = dulzura;
    }

    public String getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(String alcohol) {
        this.alcohol = alcohol;
    }
}
