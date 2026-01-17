package com.granja.repositorio;

import com.granja.entidad.HistorialEncendidoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistorialEncendidoRepositorio extends JpaRepository<HistorialEncendidoEntidad, Integer> {

    List<HistorialEncendidoEntidad> findByAspersorIdOrderByFechaDesc(String aspersorId);
}