package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.hardware.GestorArduino;
import com.granja.servicio.PersistenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public class GestorGranja {
    private ArrayList<Parcela> parcelas;
    private ArrayList<Aspersor> aspersoresInventario;
    private ArrayList<SensorHumedad> sensoresInventario;
    private GestorParcelas gestorParcelas;
    private GestorAspersores gestorAspersores;
    private GestorSensores gestorSensores;
    private GestorCultivos gestorCultivos;
    private GestorArduino gestorArduino;
    private GestorUsuarios gestorUsuarios;
    private int contadorIdAspersores;
    private int contadorIdSensores;
    private PersistenciaService persistenciaService;

    public GestorGranja() {
        this.parcelas = new ArrayList<>();
        this.aspersoresInventario = new ArrayList<>();
        this.sensoresInventario = new ArrayList<>();
        this.contadorIdAspersores = 1;
        this.contadorIdSensores = 1;

        this.gestorUsuarios = new GestorUsuarios(this);
        this.gestorParcelas = new GestorParcelas(this);
        this.gestorAspersores = new GestorAspersores(this);
        this.gestorSensores = new GestorSensores(this);
        this.gestorCultivos = new GestorCultivos(this);
        this.gestorArduino = new GestorArduino(this);
    }

    @Autowired(required = false)
    public void setPersistenciaService(PersistenciaService persistenciaService) {
        this.persistenciaService = persistenciaService;
        if (persistenciaService != null) {
            gestorUsuarios.setPersistenciaService(persistenciaService);
            gestorParcelas.setPersistenciaService(persistenciaService);
            gestorAspersores.setPersistenciaService(persistenciaService);
            gestorSensores.setPersistenciaService(persistenciaService);
            gestorCultivos.setPersistenciaService(persistenciaService);
        }
    }

    public PersistenciaService getPersistenciaService() {
        return persistenciaService;
    }

    public ArrayList<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(ArrayList<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public ArrayList<Aspersor> getAspersoresInventario() {
        return aspersoresInventario;
    }

    public void setAspersoresInventario(ArrayList<Aspersor> aspersoresInventario) {
        this.aspersoresInventario = aspersoresInventario;
    }

    public ArrayList<SensorHumedad> getSensoresInventario() {
        return sensoresInventario;
    }

    public void setSensoresInventario(ArrayList<SensorHumedad> sensoresInventario) {
        this.sensoresInventario = sensoresInventario;
    }

    public GestorParcelas getGestorParcelas() {
        return gestorParcelas;
    }

    public GestorAspersores getGestorAspersores() {
        return gestorAspersores;
    }

    public GestorSensores getGestorSensores() {
        return gestorSensores;
    }

    public GestorCultivos getGestorCultivos() {
        return gestorCultivos;
    }

    public GestorArduino getGestorArduino() {
        return gestorArduino;
    }

    public GestorUsuarios getGestorUsuarios() {
        return gestorUsuarios;
    }

    public String getSiguienteIdAspersor() {
        return "ASPERSOR_" + (contadorIdAspersores++);
    }

    public String getSiguienteIdSensor() {
        return "SENSOR_" + (contadorIdSensores++);
    }

    public int getContadorIdAspersores() {
        return contadorIdAspersores;
    }

    public void incrementarContadorAspersores() {
        contadorIdAspersores++;
    }

    public int getContadorIdSensores() {
        return contadorIdSensores;
    }

    public void incrementarContadorSensores() {
        contadorIdSensores++;
    }
}