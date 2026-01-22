package com.granja.repositorio;

import com.granja.entidad.SensorHumedadEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorHumedadRepositorio extends JpaRepository<SensorHumedadEntidad, String> {

}