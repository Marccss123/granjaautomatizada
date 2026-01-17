package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.*;

public class GestorParcelas {
    private GestorGranja gestorGranja;
    private int contadorParcelas;
    private PersistenciaService persistenciaService;

    public GestorParcelas(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
        this.contadorParcelas = 1;
    }

    public void setPersistenciaService(PersistenciaService persistenciaService) {
        this.persistenciaService = persistenciaService;
    }

    public void crearParcelas(double terrenoTotal) throws GranjaException {
        if (terrenoTotal <= 0) {
            throw new GranjaException("El terreno debe ser mayor a 0 m²");
        }

        Usuario usuarioActual = gestorGranja.getGestorUsuarios().getUsuarioActual();
        if (usuarioActual == null) {
            throw new GranjaException("Debe seleccionar un usuario antes de crear parcelas");
        }

        double tamañoParcela = 50.0;
        int cantidadParcelas = (int) (terrenoTotal / tamañoParcela);
        double terrenoRestante = terrenoTotal % tamañoParcela;

        for (int i = 0; i < cantidadParcelas; i++) {
            String idParcela = "PARCELA_" + contadorParcelas;
            Parcela parcela = new Parcela(idParcela, tamañoParcela);
            parcela.setUsuarioCreador(usuarioActual);
            gestorGranja.getParcelas().add(parcela);

            if (persistenciaService != null) {
                try {
                    persistenciaService.guardarParcela(parcela, usuarioActual);
                } catch (Exception e) {
                    System.out.println("Error guardando parcela en BD: " + e.getMessage());
                }
            }

            contadorParcelas++;
        }

        if (terrenoRestante > 0) {
            String idParcela = "PARCELA_" + contadorParcelas;
            Parcela parcela = new Parcela(idParcela, terrenoRestante);
            parcela.setUsuarioCreador(usuarioActual);
            gestorGranja.getParcelas().add(parcela);

            if (persistenciaService != null) {
                try {
                    persistenciaService.guardarParcela(parcela, usuarioActual);
                } catch (Exception e) {
                    System.out.println("Error guardando parcela en BD: " + e.getMessage());
                }
            }

            contadorParcelas++;
        }

        System.out.println("Se crearon " + gestorGranja.getParcelas().size() + " parcela(s) exitosamente.");
        System.out.println("Creado por: " + usuarioActual.getNombreCompleto());
        asignarDispositivosAutomaticamente();
    }

    private void asignarDispositivosAutomaticamente() {
        for (Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getAspersores().isEmpty() && !gestorGranja.getAspersoresInventario().isEmpty()) {
                Aspersor aspersor = gestorGranja.getAspersoresInventario().get(0);
                aspersor.setParcela(parcela);
                parcela.agregarAspersor(aspersor);
                gestorGranja.getAspersoresInventario().remove(aspersor);
                System.out.println("Aspersor " + aspersor.getId() + " asignado a " + parcela.getId());
            }

            if (parcela.getSensores().isEmpty() && !gestorGranja.getSensoresInventario().isEmpty()) {
                SensorHumedad sensor = gestorGranja.getSensoresInventario().get(0);
                sensor.setParcela(parcela);
                parcela.agregarSensor(sensor);
                gestorGranja.getSensoresInventario().remove(sensor);
                System.out.println("Sensor " + sensor.getId() + " asignado a " + parcela.getId());
            }
        }
    }

    public void mostrarInformacionParcelas() {
        System.out.println("\n========== INFORMACIÓN DE PARCELAS ==========");
        if (gestorGranja.getParcelas().isEmpty()) {
            System.out.println("No hay parcelas creadas.");
            return;
        }

        for (Parcela parcela : gestorGranja.getParcelas()) {
            System.out.println(parcela);
        }
    }

    public void eliminarParcela(String idParcela) throws GranjaException {
        Parcela parcela = buscarParcela(idParcela);

        if (parcela == null) {
            throw new GranjaException("Parcela no encontrada: " + idParcela);
        }

        for (Aspersor aspersor : parcela.getAspersores()) {
            aspersor.setParcela(null);
            gestorGranja.getAspersoresInventario().add(aspersor);
        }

        for (SensorHumedad sensor : parcela.getSensores()) {
            sensor.setParcela(null);
            gestorGranja.getSensoresInventario().add(sensor);
        }

        gestorGranja.getParcelas().remove(parcela);

        if (persistenciaService != null) {
            try {
                persistenciaService.eliminarParcela(idParcela);
            } catch (Exception e) {
                System.out.println("Error eliminando parcela de BD: " + e.getMessage());
            }
        }

        System.out.println("Parcela " + idParcela + " eliminada exitosamente.");
    }

    public Parcela buscarParcela(String idParcela) {
        for (Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getId().equals(idParcela)) {
                return parcela;
            }
        }
        return null;
    }
}