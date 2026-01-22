package com.granja.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
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
