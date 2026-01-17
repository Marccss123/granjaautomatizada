package com.granja.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Aspersor {
    private String id;
    private boolean conectado;
    private boolean encendido;
    private Parcela parcela;
    private ArrayList<LocalDateTime> historialEncendidos;

    public Aspersor(String id) {
        this.id = id;
        this.conectado = false;
        this.encendido = false;
        this.parcela = null;
        this.historialEncendidos = new ArrayList<>();
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

    public boolean isEncendido() {
        return encendido;
    }

    public void setEncendido(boolean encendido) {
        this.encendido = encendido;
    }

    public Parcela getParcela() {
        return parcela;
    }

    public void setParcela(Parcela parcela) {
        this.parcela = parcela;
    }

    public ArrayList<LocalDateTime> getHistorialEncendidos() {
        return historialEncendidos;
    }

    public void setHistorialEncendidos(ArrayList<LocalDateTime> historialEncendidos) {
        this.historialEncendidos = historialEncendidos;
    }

    public void registrarEncendido() {
        historialEncendidos.add(LocalDateTime.now());
    }

    public void encender() {
        if (conectado && !encendido) {
            encendido = true;
            registrarEncendido();
        }
    }

    public void apagar() {
        encendido = false;
    }

    @Override
    public String toString() {
        String parcelaStr = (parcela != null) ? parcela.getId() : "Sin asignar";
        String estadoConexion = conectado ? "Conectado" : "Desconectado";
        String estadoEncendido = encendido ? "Encendido" : "Apagado";
        return "ID: " + id + " - " + estadoConexion + " - " + estadoEncendido + " - Parcela: " + parcelaStr;
    }
}
