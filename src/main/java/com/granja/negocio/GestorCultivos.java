package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.servicio.OperacionesCrud;
import com.granja.utilitario.GranjaException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Getter
@Slf4j
public class GestorCultivos {
    private final GestorGranja gestorGranja;
    private final ArrayList<Cultivo> cultivosDisponibles;
    private OperacionesCrud operacionesCrud;

    public GestorCultivos(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
        this.cultivosDisponibles = new ArrayList<>();
        inicializarCultivos();
    }

    public void setPersistenciaService(OperacionesCrud operacionesCrud) {
        this.operacionesCrud = operacionesCrud;
    }

    private void inicializarCultivos() {
        cultivosDisponibles.add(new Cultivo("Tomate", 50, 70, 72));
        cultivosDisponibles.add(new Cultivo("Lechuga", 40, 60, 48));
        cultivosDisponibles.add(new Cultivo("Maíz", 45, 65, 96));
        cultivosDisponibles.add(new Cultivo("Zanahoria", 35, 55, 120));
        cultivosDisponibles.add(new Cultivo("Fresa", 55, 75, 60));
        cultivosDisponibles.add(new Cultivo("Pepino", 50, 70, 72));
        cultivosDisponibles.add(new Cultivo("Cebolla", 30, 50, 144));
    }


    public boolean agregarCultivo(String nombre, int humedadMinima, int humedadMaxima, int frecuenciaRiegoHoras) throws GranjaException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new GranjaException("El nombre del cultivo es obligatorio");
        }
        if (buscarCultivo(nombre) != null) {
            throw new GranjaException("Ya existe un cultivo con el nombre: " + nombre);
        }

        if (humedadMinima < 0 || humedadMinima > 100) {
            throw new GranjaException("La humedad mínima debe estar entre 0 y 100");
        }

        if (humedadMaxima < 0 || humedadMaxima > 100) {
            throw new GranjaException("La humedad máxima debe estar entre 0 y 100");
        }

        if (humedadMinima >= humedadMaxima) {
            throw new GranjaException("La humedad mínima debe ser menor que la máxima");
        }

        if (frecuenciaRiegoHoras <= 0) {
            throw new GranjaException("La frecuencia de riego debe ser mayor a 0 horas");
        }

        Cultivo nuevoCultivo = new Cultivo(nombre.trim(), humedadMinima, humedadMaxima, frecuenciaRiegoHoras);
        cultivosDisponibles.add(nuevoCultivo);

        if (operacionesCrud != null) {
            try {
                operacionesCrud.guardarCultivo(nuevoCultivo);
                log.info("Cultivo guardado en BD: {}", nombre);
                return true;
            } catch (Exception e) {
                log.info("Error guardando cultivo en BD: {}", e.getMessage());
                cultivosDisponibles.remove(nuevoCultivo);
                throw new GranjaException("Error al guardar el cultivo en la base de datos");
            }
        }
        log.info("Cultivo agregado: {}", nombre);
        return true;
    }

    public void mostrarCultivosDisponibles() {
        System.out.println("\n========== CULTIVOS DISPONIBLES ==========");
        for (Cultivo cultivo : cultivosDisponibles) {
            System.out.println(cultivo.getNombre() + " - Humedad: " + cultivo.getHumedadMinima() +
                    "-" + cultivo.getHumedadMaxima() + "% - Riego cada " +
                    cultivo.getFrecuenciaRiegoHoras() + " horas");
        }
    }

    public void registrarCultivoEnParcela(String idParcela, String nombreCultivo) throws GranjaException {
        Parcela parcela = gestorGranja.getGestorParcelas().buscarParcela(idParcela);

        if (parcela == null) {
            throw new GranjaException("Parcela no encontrada: " + idParcela);
        }

        Cultivo cultivo = buscarCultivo(nombreCultivo);
        if (cultivo == null) {
            throw new GranjaException("Cultivo no encontrado: " + nombreCultivo);
        }

        if (parcela.getCultivo() != null) {
            throw new GranjaException("La parcela ya tiene un cultivo asignado. Use cambiar cultivo.");
        }

        parcela.setCultivo(cultivo);

        if (operacionesCrud != null) {
            try {
                operacionesCrud.actualizarCultivoParcela(idParcela, nombreCultivo);
            } catch (Exception e) {
                System.out.println("Error actualizando cultivo en BD: " + e.getMessage());
            }
        }

        System.out.println("Cultivo " + nombreCultivo + " registrado en parcela " + idParcela);
    }

    public void cambiarCultivoParcela(String idParcela, String nombreCultivo) throws GranjaException {
        Parcela parcela = gestorGranja.getGestorParcelas().buscarParcela(idParcela);

        if (parcela == null) {
            throw new GranjaException("Parcela no encontrada: " + idParcela);
        }

        Cultivo cultivo = buscarCultivo(nombreCultivo);
        if (cultivo == null) {
            throw new GranjaException("Cultivo no encontrado: " + nombreCultivo);
        }

        parcela.setCultivo(cultivo);

        if (operacionesCrud != null) {
            try {
                operacionesCrud.actualizarCultivoParcela(idParcela, nombreCultivo);
            } catch (Exception e) {
                System.out.println("Error actualizando cultivo en BD: " + e.getMessage());
            }
        }

        System.out.println("Cultivo de la parcela " + idParcela + " cambiado a " + nombreCultivo);
    }

    private Cultivo buscarCultivo(String nombre) {
        for (Cultivo cultivo : cultivosDisponibles) {
            if (cultivo.getNombre().equalsIgnoreCase(nombre)) {
                return cultivo;
            }
        }
        return null;
    }
}