package com.granja.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cultivo {
    private String nombre;
    private int humedadMinima;
    private int humedadMaxima;
    private int frecuenciaRiegoHoras;

    public Cultivo(String nombre, int humedadMinima, int humedadMaxima, int frecuenciaRiegoHoras) {
        this.nombre = nombre;
        this.humedadMinima = humedadMinima;
        this.humedadMaxima = humedadMaxima;
        this.frecuenciaRiegoHoras = frecuenciaRiegoHoras;
    }

    @Override
    public String toString() {
        return nombre + " (Humedad: " + humedadMinima + "%-" + humedadMaxima + "%, Riego cada " + frecuenciaRiegoHoras + " horas)";
    }
}