package com.granja.repositorio;

import com.granja.entidad.LecturaHumedadEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LecturaHumedadRepositorio extends JpaRepository<LecturaHumedadEntidad, Integer> {

    List<LecturaHumedadEntidad> findBySensorIdOrderByFechaDesc(String sensorId);
}