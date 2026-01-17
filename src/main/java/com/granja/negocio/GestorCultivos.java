package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.GranjaException;
import java.util.ArrayList;

public class GestorCultivos {
    private GestorGranja gestorGranja;
    private ArrayList<Cultivo> cultivosDisponibles;
    private PersistenciaService persistenciaService;

    public GestorCultivos(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
        this.cultivosDisponibles = new ArrayList<>();
        inicializarCultivos();
    }

    public void setPersistenciaService(PersistenciaService persistenciaService) {
        this.persistenciaService = persistenciaService;
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

        if (persistenciaService != null) {
            try {
                persistenciaService.actualizarCultivoParcela(idParcela, nombreCultivo);
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

        if (persistenciaService != null) {
            try {
                persistenciaService.actualizarCultivoParcela(idParcela, nombreCultivo);
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

    public ArrayList<Cultivo> getCultivosDisponibles() {
        return cultivosDisponibles;
    }
}