package com.granja.modelo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
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