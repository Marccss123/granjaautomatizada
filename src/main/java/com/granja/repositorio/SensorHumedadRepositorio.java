package com.granja.repositorio;

import com.granja.entidad.SensorHumedadEntidad;
import com.granja.entidad.ParcelaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SensorHumedadRepositorio extends JpaRepository<SensorHumedadEntidad, String> {

    List<SensorHumedadEntidad> findByParcela(ParcelaEntity parcela);

    List<SensorHumedadEntidad> findByParcelaIsNull();
}