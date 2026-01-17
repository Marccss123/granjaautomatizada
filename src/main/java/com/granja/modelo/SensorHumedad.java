package com.granja.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SensorHumedad {
    private String id;
    private boolean conectado;
    private int humedadActual;
    private Parcela parcela;
    private ArrayList<LecturaHumedad> lecturas;

    public SensorHumedad(String id) {
        this.id = id;
        this.conectado = false;
        this.humedadActual = 50;
        this.parcela = null;
        this.lecturas = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public int getHumedadActual() {
        return humedadActual;
    }

    public void setHumedadActual(int humedadActual) {
        this.humedadActual = humedadActual;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public ArrayList<LecturaHumedad> getLecturas() {
        return lecturas;
    }

    public void setLecturas(ArrayList<LecturaHumedad> lecturas) {
        this.lecturas = lecturas;
    }

    public void realizarLectura() {
        if (conectado) {
            LecturaHumedad lectura = new LecturaHumedad(LocalDateTime.now(), humedadActual);
            lecturas.add(lectura);
        }
    }

    @Override
    public String toString() {
        String parcelaStr = (parcela != null) ? parcela.getId() : "Sin asignar";
        String estadoConexion = conectado ? "Conectado" : "Desconectado";
        return "ID: " + id + " - " + estadoConexion + " - Humedad: " + humedadActual + "% - Parcela: " + parcelaStr;
    }
}