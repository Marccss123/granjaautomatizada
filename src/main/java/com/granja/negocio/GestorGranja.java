package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.hardware.GestorArduino;
import com.granja.servicio.OperacionesCrud;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
@Getter
@Setter
public class GestorGranja {
    private ArrayList<Parcela> parcelas;
    private ArrayList<Aspersor> aspersoresInventario;
    private ArrayList<SensorHumedad> sensoresInventario;
    private final GestorParcelas gestorParcelas;
    private final GestorAspersores gestorAspersores;
    private final GestorSensores gestorSensores;
    private final GestorCultivos gestorCultivos;
    private final GestorArduino gestorArduino;
    private final GestorUsuarios gestorUsuarios;
    private int contadorIdAspersores;
    private int contadorIdSensores;
    private OperacionesCrud operacionesCrud;

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
    public void setPersistenciaService(OperacionesCrud operacionesCrud) {
        this.operacionesCrud = operacionesCrud;
        if (operacionesCrud != null) {
            gestorUsuarios.setPersistenciaService(operacionesCrud);
            gestorParcelas.setPersistenciaService(operacionesCrud);
            gestorAspersores.setPersistenciaService(operacionesCrud);
            gestorSensores.setPersistenciaService(operacionesCrud);
            gestorCultivos.setPersistenciaService(operacionesCrud);
        }
    }

    public String getSiguienteIdAspersor() {
        return "ASPERSOR_" + (contadorIdAspersores++);
    }

    public String getSiguienteIdSensor() {
        return "SENSOR_" + (contadorIdSensores++);
    }
}