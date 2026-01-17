package com.granja.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_encendido")
@Getter
@Setter
public class HistorialEncendidoEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "aspersor_id", nullable = false, length = 50)
    private String aspersorId;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    public HistorialEncendidoEntidad() {
    }

    public HistorialEncendidoEntidad(String aspersorId, LocalDateTime fecha) {
        this.aspersorId = aspersorId;
        this.fecha = fecha;
    }
}