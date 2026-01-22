package com.granja.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
public class Parcela {
    private String id;
    private double metrosCuadrados;
    private Cultivo cultivo;
    private ArrayList<Aspersor> aspersores;
    private ArrayList<SensorHumedad> sensores;
    private Usuario usuarioCreador;
    private LocalDateTime fechaCreacion;

    public Parcela(String id, double metrosCuadrados) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.cultivo = null;
        this.aspersores = new ArrayList<>();
        this.sensores = new ArrayList<>();
        this.usuarioCreador = null;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void agregarAspersor(Aspersor aspersor) {
        aspersores.add(aspersor);
    }

    public void agregarSensor(SensorHumedad sensor) {
        sensores.add(sensor);
    }

    public void removerAspersor(Aspersor aspersor) {
        aspersores.remove(aspersor);
    }

    public void removerSensor(SensorHumedad sensor) {
        sensores.remove(sensor);
    }

    @Override
    public String toString() {
        String cultivoStr = (cultivo != null) ? cultivo.getNombre() : "Sin cultivo";
        String usuarioStr = (usuarioCreador != null) ? usuarioCreador.getNombreCompleto() : "Sin asignar";
        return "ID: " + id + " - " + metrosCuadrados + " m² - Cultivo: " + cultivoStr +
                " - Aspersores: " + aspersores.size() + " - Sensores: " + sensores.size() +
                " - Creado por: " + usuarioStr;
    }
}