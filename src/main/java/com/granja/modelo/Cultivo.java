package com.granja.modelo;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getHumedadMinima() {
        return humedadMinima;
    }

    public void setHumedadMinima(int humedadMinima) {
        this.humedadMinima = humedadMinima;
    }

    public int getHumedadMaxima() {
        return humedadMaxima;
    }

    public void setHumedadMaxima(int humedadMaxima) {
        this.humedadMaxima = humedadMaxima;
    }

    public int getFrecuenciaRiegoHoras() {
        return frecuenciaRiegoHoras;
    }

    public void setFrecuenciaRiegoHoras(int frecuenciaRiegoHoras) {
        this.frecuenciaRiegoHoras = frecuenciaRiegoHoras;
    }

    @Override
    public String toString() {
        return nombre + " (Humedad: " + humedadMinima + "%-" + humedadMaxima + "%, Riego cada " + frecuenciaRiegoHoras + " horas)";
    }
}