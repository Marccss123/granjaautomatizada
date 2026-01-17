package com.granja.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(double metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public Cultivo getCultivo() {
        return cultivo;
    }

    public void setCultivo(Cultivo cultivo) {
        this.cultivo = cultivo;
    }

    public ArrayList<Aspersor> getAspersores() {
        return aspersores;
    }

    public void setAspersores(ArrayList<Aspersor> aspersores) {
        this.aspersores = aspersores;
    }

    public ArrayList<SensorHumedad> getSensores() {
        return sensores;
    }

    public void setSensores(ArrayList<SensorHumedad> sensores) {
        this.sensores = sensores;
    }

    public Usuario getUsuarioCreador() {
        return usuarioCreador;
    }

    public void setUsuarioCreador(Usuario usuarioCreador) {
        this.usuarioCreador = usuarioCreador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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