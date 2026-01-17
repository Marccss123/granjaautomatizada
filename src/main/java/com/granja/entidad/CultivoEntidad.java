package com.granja.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cultivo")
@Getter
@Setter
public class CultivoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "humedad_minima", nullable = false)
    private int humedadMinima;

    @Column(name = "humedad_maxima", nullable = false)
    private int humedadMaxima;

    @Column(name = "frecuencia_riego_horas", nullable = false)
    private int frecuenciaRiegoHoras;

    public CultivoEntidad() {
    }

    public CultivoEntidad(String nombre, int humedadMinima, int humedadMaxima, int frecuenciaRiegoHoras) {
        this.nombre = nombre;
        this.humedadMinima = humedadMinima;
        this.humedadMaxima = humedadMaxima;
        this.frecuenciaRiegoHoras = frecuenciaRiegoHoras;
    }
}