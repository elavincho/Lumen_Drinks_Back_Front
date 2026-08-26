package com.lumendrinks.repositorio;

import com.lumendrinks.entidad.Trago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TragoRepositorio extends JpaRepository<Trago, Long> {
    List<Trago> findByCategoria(String categoria);
}
