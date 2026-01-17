package com.granja.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lectura_humedad")
@Getter
@Setter
public class LecturaHumedadEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "sensor_id", nullable = false, length = 50)
    private String sensorId;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "porcentaje_humedad", nullable = false)
    private int porcentajeHumedad;

    public LecturaHumedadEntidad() {
    }

    public LecturaHumedadEntidad(String sensorId, LocalDateTime fecha, int porcentajeHumedad) {
        this.sensorId = sensorId;
        this.fecha = fecha;
        this.porcentajeHumedad = porcentajeHumedad;
    }
}