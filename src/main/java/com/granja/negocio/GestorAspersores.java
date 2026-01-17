package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorAspersores {
    private GestorGranja gestorGranja;
    private PersistenciaService persistenciaService;

    public GestorAspersores(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }

    public void setPersistenciaService(PersistenciaService persistenciaService) {
        this.persistenciaService = persistenciaService;
    }

    public void agregarAspersoresInventario(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            String id = gestorGranja.getSiguienteIdAspersor();
            Aspersor aspersor = new Aspersor(id);
            gestorGranja.getAspersoresInventario().add(aspersor);

            if (persistenciaService != null) {
                try {
                    persistenciaService.guardarAspersor(aspersor);
                } catch (Exception e) {
                    System.out.println("Error guardando aspersor en BD: " + e.getMessage());
                }
            }
        }
        System.out.println("Se agregaron " + cantidad + " aspersor(es) al inventario.");
    }

    public void mostrarInformacionAspersores() {
        System.out.println("\n========== ASPERSORES EN INVENTARIO ==========");
        if (gestorGranja.getAspersoresInventario().isEmpty()) {
            System.out.println("No hay aspersores en inventario.");
        } else {
            for (Aspersor aspersor : gestorGranja.getAspersoresInventario()) {
                System.out.println(aspersor);
            }
        }

        System.out.println("\n========== ASPERSORES ASIGNADOS ==========");
        boolean hayAsignados = false;
        for (Parcela parcela : gestorGranja.getParcelas()) {
            for (Aspersor aspersor : parcela.getAspersores()) {
                System.out.println(aspersor);
                hayAsignados = true;
            }
        }
        if (!hayAsignados) {
            System.out.println("No hay aspersores asignados a parcelas.");
        }
    }

    public void asignarAspersorAParcela(String idParcela) throws GranjaException {
        Parcela parcela = gestorGranja.getGestorParcelas().buscarParcela(idParcela);

        if (parcela == null) {
            throw new GranjaException("Parcela no encontrada: " + idParcela);
        }

        if (gestorGranja.getAspersoresInventario().isEmpty()) {
            throw new GranjaException("No hay aspersores disponibles en el inventario.");
        }

        Aspersor aspersor = gestorGranja.getAspersoresInventario().get(0);
        aspersor.setParcela(parcela);
        parcela.agregarAspersor(aspersor);
        gestorGranja.getAspersoresInventario().remove(aspersor);

        if (persistenciaService != null) {
            try {
                persistenciaService.guardarAspersor(aspersor);
            } catch (Exception e) {
                System.out.println("Error actualizando aspersor en BD: " + e.getMessage());
            }
        }

        System.out.println("Aspersor " + aspersor.getId() + " asignado a parcela " + idParcela);
    }

    public void prenderManualmente(String idAspersor) throws GranjaException {
        Aspersor aspersor = buscarAspersor(idAspersor);

        if (aspersor == null) {
            throw new GranjaException("Aspersor no encontrado: " + idAspersor);
        }

        if (!aspersor.isConectado()) {
            throw new GranjaException("El aspersor debe estar conectado para encenderlo.");
        }

        if (aspersor.isEncendido()) {
            System.out.println("El aspersor " + idAspersor + " ya está encendido.");
            return;
        }

        if (gestorGranja.getGestorArduino().esDispositivoArduino(idAspersor)) {
            boolean exito = gestorGranja.getGestorArduino().activarAspersorReal(idAspersor);
            if (exito) {
                aspersor.encender();

                if (persistenciaService != null) {
                    try {
                        persistenciaService.guardarAspersor(aspersor);
                        persistenciaService.guardarHistorialEncendido(idAspersor, LocalDateTime.now());
                    } catch (Exception e) {
                        System.out.println("Error guardando estado en BD: " + e.getMessage());
                    }
                }

                System.out.println("Aspersor Arduino " + idAspersor + " encendido manualmente.");
            } else {
                throw new GranjaException("Error al comunicarse con el Arduino: " + idAspersor);
            }
        } else {
            aspersor.encender();

            if (persistenciaService != null) {
                try {
                    persistenciaService.guardarAspersor(aspersor);
                    persistenciaService.guardarHistorialEncendido(idAspersor, LocalDateTime.now());
                } catch (Exception e) {
                    System.out.println("Error guardando estado en BD: " + e.getMessage());
                }
            }

            System.out.println("Aspersor " + idAspersor + " encendido manualmente.");
        }
    }

    public void realizarRiegoAutomatico() {
        System.out.println("\n========== RIEGO AUTOMÁTICO ==========");
        gestorGranja.getGestorSensores().simularLecturasTodasParcelas();

        System.out.println("\n========== EVALUANDO NECESIDAD DE RIEGO ==========");
        boolean seRealizaronCambios = false;

        for (Parcela parcela : gestorGranja.getParcelas()) {
            if (parcela.getCultivo() == null) {
                continue;
            }

            if (parcela.getSensores().isEmpty()) {
                continue;
            }

            SensorHumedad primerSensor = parcela.getSensores().get(0);
            if (!primerSensor.isConectado()) {
                continue;
            }

            int humedadActual = primerSensor.getHumedadActual();
            Cultivo cultivo = parcela.getCultivo();

            System.out.println(parcela.getId() + " - Humedad: " + humedadActual + "% (Requerida: " +
                    cultivo.getHumedadMinima() + "%-" + cultivo.getHumedadMaxima() + "%)");

            if (humedadActual < cultivo.getHumedadMinima()) {
                for (Aspersor aspersor : parcela.getAspersores()) {
                    if (aspersor.isConectado() && !aspersor.isEncendido()) {
                        if (gestorGranja.getGestorArduino().esDispositivoArduino(aspersor.getId())) {
                            boolean exito = gestorGranja.getGestorArduino().activarAspersorReal(aspersor.getId());
                            if (exito) {
                                aspersor.encender();

                                if (persistenciaService != null) {
                                    try {
                                        persistenciaService.guardarAspersor(aspersor);
                                        persistenciaService.guardarHistorialEncendido(aspersor.getId(), LocalDateTime.now());
                                    } catch (Exception e) {
                                        System.out.println("Error guardando en BD: " + e.getMessage());
                                    }
                                }

                                System.out.println("  → " + aspersor.getId() + " (Arduino) ACTIVADO automáticamente");
                                seRealizaronCambios = true;
                            }
                        } else {
                            aspersor.encender();

                            if (persistenciaService != null) {
                                try {
                                    persistenciaService.guardarAspersor(aspersor);
                                    persistenciaService.guardarHistorialEncendido(aspersor.getId(), LocalDateTime.now());
                                } catch (Exception e) {
                                    System.out.println("Error guardando en BD: " + e.getMessage());
                                }
                            }

                            System.out.println("  → " + aspersor.getId() + " ACTIVADO automáticamente");
                            seRealizaronCambios = true;
                        }
                    }
                }
            } else if (humedadActual > cultivo.getHumedadMaxima()) {
                for (Aspersor aspersor : parcela.getAspersores()) {
                    if (aspersor.isEncendido()) {
                        if (gestorGranja.getGestorArduino().esDispositivoArduino(aspersor.getId())) {
                            boolean exito = gestorGranja.getGestorArduino().desactivarAspersorReal(aspersor.getId());
                            if (exito) {
                                aspersor.apagar();

                                if (persistenciaService != null) {
                                    try {
                                        persistenciaService.guardarAspersor(aspersor);
                                    } catch (Exception e) {
                                        System.out.println("Error guardando en BD: " + e.getMessage());
                                    }
                                }

                                System.out.println("  → " + aspersor.getId() + " (Arduino) APAGADO (humedad excesiva)");
                                seRealizaronCambios = true;
                            }
                        } else {
                            aspersor.apagar();

                            if (persistenciaService != null) {
                                try {
                                    persistenciaService.guardarAspersor(aspersor);
                                } catch (Exception e) {
                                    System.out.println("Error guardando en BD: " + e.getMessage());
                                }
                            }

                            System.out.println("  → " + aspersor.getId() + " APAGADO (humedad excesiva)");
                            seRealizaronCambios = true;
                        }
                    }
                }
            }
        }

        if (!seRealizaronCambios) {
            System.out.println("No se requieren cambios en el riego.");
        }
    }

    public void conectarDesconectarAspersor(String idAspersor) throws GranjaException {
        Aspersor aspersor = buscarAspersor(idAspersor);

        if (aspersor == null) {
            throw new GranjaException("Aspersor no encontrado: " + idAspersor);
        }

        aspersor.setConectado(!aspersor.isConectado());

        if (persistenciaService != null) {
            try {
                persistenciaService.guardarAspersor(aspersor);
            } catch (Exception e) {
                System.out.println("Error actualizando aspersor en BD: " + e.getMessage());
            }
        }

        String estado = aspersor.isConectado() ? "conectado" : "desconectado";
        System.out.println("Aspersor " + idAspersor + " " + estado);
    }

    public void mostrarHistorialAspersor(String idAspersor) throws GranjaException {
        Aspersor aspersor = buscarAspersor(idAspersor);

        if (aspersor == null) {
            throw new GranjaException("Aspersor no encontrado: " + idAspersor);
        }

        System.out.println("\n========== HISTORIAL DEL ASPERSOR " + idAspersor + " ==========");
        if (aspersor.getHistorialEncendidos().isEmpty()) {
            System.out.println("No hay registros de encendidos.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (LocalDateTime fecha : aspersor.getHistorialEncendidos()) {
                System.out.println("Encendido: " + fecha.format(formatter));
            }
        }
    }

    public void eliminarAspersor(String idAspersor) throws GranjaException {
        Aspersor aspersor = buscarAspersor(idAspersor);

        if (aspersor == null) {
            throw new GranjaException("Aspersor no encontrado: " + idAspersor);
        }

        if (aspersor.getParcela() != null) {
            aspersor.getParcela().removerAspersor(aspersor);
        }

        gestorGranja.getAspersoresInventario().remove(aspersor);

        if (persistenciaService != null) {
            try {
                persistenciaService.eliminarAspersor(idAspersor);
            } catch (Exception e) {
                System.out.println("Error eliminando aspersor de BD: " + e.getMessage());
            }
        }

        System.out.println("Aspersor " + idAspersor + " eliminado del sistema.");
    }

    public Aspersor buscarAspersor(String idAspersor) {
        for (Aspersor aspersor : gestorGranja.getAspersoresInventario()) {
            if (aspersor.getId().equals(idAspersor)) {
                return aspersor;
            }
        }
        for (Parcela parcela : gestorGranja.getParcelas()) {
            for (Aspersor aspersor : parcela.getAspersores()) {
                if (aspersor.getId().equals(idAspersor)) {
                    return aspersor;
                }
            }
        }
        return null;
    }
}