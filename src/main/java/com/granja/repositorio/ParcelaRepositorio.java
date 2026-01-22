package com.granja.repositorio;

import com.granja.entidad.ParcelaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaRepositorio extends JpaRepository<ParcelaEntity, String> {

}

