package com.granja.repositorio;

import com.granja.entidad.CultivoEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CultivoRepositorio extends JpaRepository<CultivoEntidad, Integer> {

    Optional<CultivoEntidad> findByNombre(String nombre);
}