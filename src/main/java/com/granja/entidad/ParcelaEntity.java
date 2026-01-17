package com.granja.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "parcela")
@Getter
@Setter
public class ParcelaEntity {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "metros_cuadrados", nullable = false)
    private double metrosCuadrados;

    @ManyToOne
    @JoinColumn(name = "cultivo_id")
    private CultivoEntidad cultivo;

    @ManyToOne
    @JoinColumn(name = "usuario_creador_id")
    private UsuarioEntidad usuarioCreador;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public ParcelaEntity() {
    }

    public ParcelaEntity(String id, double metrosCuadrados) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.fechaCreacion = LocalDateTime.now();
    }

}