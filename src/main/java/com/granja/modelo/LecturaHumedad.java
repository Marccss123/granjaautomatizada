package com.granja.modelo;

import java.time.LocalDateTime;

public class LecturaHumedad {
    private LocalDateTime fecha;
    private int porcentajeHumedad;

    public LecturaHumedad(LocalDateTime fecha, int porcentajeHumedad) {
        this.fecha = fecha;
        this.porcentajeHumedad = porcentajeHumedad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getPorcentajeHumedad() {
        return porcentajeHumedad;
    }

    public void setPorcentajeHumedad(int porcentajeHumedad) {
        this.porcentajeHumedad = porcentajeHumedad;
    }

    @Override
    public String toString() {
        return "Fecha: " + fecha + " - Humedad: " + porcentajeHumedad + "%";
    }
}