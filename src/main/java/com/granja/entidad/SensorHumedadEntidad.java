package com.granja.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sensor_humedad")
@Getter
@Setter
public class SensorHumedadEntidad {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "conectado")
    private boolean conectado;

    @Column(name = "humedad_actual")
    private int humedadActual;

    @ManyToOne
    @JoinColumn(name = "parcela_id")
    private ParcelaEntity parcela;

    public SensorHumedadEntidad() {
    }

    public SensorHumedadEntidad(String id) {
        this.id = id;
        this.conectado = false;
        this.humedadActual = 50;
    }
}