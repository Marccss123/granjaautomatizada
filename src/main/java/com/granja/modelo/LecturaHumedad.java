package com.granja.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class LecturaHumedad {
    private LocalDateTime fecha;
    private int porcentajeHumedad;

    public LecturaHumedad(LocalDateTime fecha, int porcentajeHumedad) {
        this.fecha = fecha;
        this.porcentajeHumedad = porcentajeHumedad;
    }


    @Override
    public String toString() {
        return "Fecha: " + fecha + " - Humedad: " + porcentajeHumedad + "%";
    }
}