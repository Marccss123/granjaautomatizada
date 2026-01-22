package com.granja.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Usuario {
    private String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private String rol;
    private LocalDateTime fechaRegistro;
    private boolean activo;

    public Usuario(String id, String nombre, String apellido, String email, String telefono, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.rol = rol;
        this.fechaRegistro = LocalDateTime.now();
        this.activo = true;
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Override
    public String toString() {
        return id + " - " + getNombreCompleto() + " (" + rol + ")";
    }
}