package com.granja.repositorio;

import com.granja.entidad.UsuarioEntidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<UsuarioEntidad, String> {

    Optional<UsuarioEntidad> findByEmail(String email);

}