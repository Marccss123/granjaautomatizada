package com.granja.repositorio;

import com.granja.entidad.AspersorEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Comunicarse con la entidad AspersorEntidad en la base de datos
@Repository
public interface AspersorRepositorio extends JpaRepository<AspersorEntidad, String> {

}