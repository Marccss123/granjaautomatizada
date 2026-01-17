package com.granja.entidad;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "aspersor")
@Getter
@Setter
public class AspersorEntidad {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "conectado")
    private boolean conectado;

    @Column(name = "encendido")
    private boolean encendido;

    @ManyToOne
    @JoinColumn(name = "parcela_id")
    private ParcelaEntity parcela;

    public AspersorEntidad() {
    }

    public AspersorEntidad(String id) {
        this.id = id;
        this.conectado = false;
        this.encendido = false;
    }
}