package com.granja.negocio;

import com.granja.modelo.*;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GestorSensores {
    private GestorGranja gestorGranja;
    private PersistenciaService persistenciaService;

    public GestorSensores(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
    }

    public void setPersistenciaService(PersistenciaService persistenciaService) {
        this.persistenciaService = persistenciaService;
    }

    public void agregarSensoresInventario(int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            String id = gestorGranja.getSiguienteIdSensor();
            SensorHumedad sensor = new SensorHumedad(id);
            gestorGranja.getSensoresInventario().add(sensor);

            if (persistenciaService != null) {
                try {
                    persistenciaService.guardarSensor(sensor);
                } catch (Exception e) {
                    System.out.println("Error guardando sensor en BD: " + e.getMessage());
                }
            }
        }
        System.out.println("Se agregaron " + cantidad + " sensor(es) al inventario.");
    }

    public void mostrarInformacionSensores() {
        System.out.println("\n========== SENSORES EN INVENTARIO ==========");
        if (gestorGranja.getSensoresInventario().isEmpty()) {
            System.out.println("No hay sensores en inventario.");
        } else {
            for (SensorHumedad sensor : gestorGranja.getSensoresInventario()) {
                System.out.println(sensor);
            }
        }

        System.out.println("\n========== SENSORES ASIGNADOS ==========");
        boolean hayAsignados = false;
        for (Parcela parcela : gestorGranja.getParcelas()) {
            for (SensorHumedad sensor : parcela.getSensores()) {
                System.out.println(sensor);
                hayAsignados = true;
            }
        }
        if (!hayAsignados) {
            System.out.println("No hay sensores asignados a parcelas.");
        }
    }

    public void asignarSensorAParcela(String idParcela) throws GranjaException {
        Parcela parcela = gestorGranja.getGestorParcelas().buscarParcela(idParcela);

        if (parcela == null) {
            throw new GranjaException("Parcela no encontrada: " + idParcela);
        }

        if (gestorGranja.getSensoresInventario().isEmpty()) {
            throw new GranjaException("No hay sensores disponibles en el inventario.");
        }

        SensorHumedad sensor = gestorGranja.getSensoresInventario().get(0);
        sensor.setParcela(parcela);
        parcela.agregarSensor(sensor);
        gestorGranja.getSensoresInventario().remove(sensor);

        if (persistenciaService != null) {
            try {
                persistenciaService.guardarSensor(sensor);
            } catch (Exception e) {
                System.out.println("Error actualizando sensor en BD: " + e.getMessage());
            }
        }

        System.out.println("Sensor " + sensor.getId() + " asignado a parcela " + idParcela);
    }

    public void conectarDesconectarSensor(String idSensor) throws GranjaException {
        SensorHumedad sensor = buscarSensor(idSensor);

        if (sensor == null) {
            throw new GranjaException("Sensor no encontrado: " + idSensor);
        }

        sensor.setConectado(!sensor.isConectado());

        if (persistenciaService != null) {
            try {
                persistenciaService.guardarSensor(sensor);
            } catch (Exception e) {
                System.out.println("Error actualizando sensor en BD: " + e.getMessage());
            }
        }

        String estado = sensor.isConectado() ? "conectado" : "desconectado";
        System.out.println("Sensor " + idSensor + " " + estado);
    }

    public void simularLecturasTodasParcelas() {
        System.out.println("\n========== SIMULACIÓN DE LECTURAS ==========");
        boolean hayLecturas = false;

        for (Parcela parcela : gestorGranja.getParcelas()) {
            for (SensorHumedad sensor : parcela.getSensores()) {
                if (sensor.isConectado()) {
                    int nuevaHumedad;

                    if (gestorGranja.getGestorArduino().esDispositivoArduino(sensor.getId())) {
                        nuevaHumedad = gestorGranja.getGestorArduino().leerHumedadReal(sensor.getId());
                        if (nuevaHumedad < 0) {
                            nuevaHumedad = Util.generarHumedadProgresiva(sensor.getHumedadActual());
                            System.out.println(sensor.getId() + " (Arduino sin respuesta, usando simulación) en " + parcela.getId() +
                                    " - Nueva humedad: " + nuevaHumedad + "%");
                        } else {
                            System.out.println(sensor.getId() + " (Arduino) en " + parcela.getId() +
                                    " - Humedad real: " + nuevaHumedad + "%");
                        }
                    } else {
                        nuevaHumedad = Util.generarHumedadProgresiva(sensor.getHumedadActual());
                        System.out.println(sensor.getId() + " (Simulado) en " + parcela.getId() +
                                " - Nueva humedad: " + nuevaHumedad + "%");
                    }

                    sensor.setHumedadActual(nuevaHumedad);
                    sensor.realizarLectura();

                    if (persistenciaService != null) {
                        try {
                            persistenciaService.guardarSensor(sensor);
                            LecturaHumedad ultimaLectura = sensor.getLecturas().get(sensor.getLecturas().size() - 1);
                            persistenciaService.guardarLecturaHumedad(sensor.getId(), ultimaLectura);
                        } catch (Exception e) {
                            System.out.println("Error guardando lectura en BD: " + e.getMessage());
                        }
                    }

                    hayLecturas = true;
                }
            }
        }

        if (!hayLecturas) {
            System.out.println("No hay sensores conectados para realizar lecturas.");
        }
    }

    public void mostrarLecturasSensor(String idSensor) throws GranjaException {
        SensorHumedad sensor = buscarSensor(idSensor);

        if (sensor == null) {
            throw new GranjaException("Sensor no encontrado: " + idSensor);
        }

        System.out.println("\n========== LECTURAS DEL SENSOR " + idSensor + " ==========");
        if (sensor.getLecturas().isEmpty()) {
            System.out.println("No hay lecturas registradas.");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            for (LecturaHumedad lectura : sensor.getLecturas()) {
                System.out.println(lectura.getFecha().format(formatter) + " - " +
                        lectura.getPorcentajeHumedad() + "%");
            }
        }
    }

    public void eliminarSensor(String idSensor) throws GranjaException {
        SensorHumedad sensor = buscarSensor(idSensor);

        if (sensor == null) {
            throw new GranjaException("Sensor no encontrado: " + idSensor);
        }

        if (sensor.getParcela() != null) {
            sensor.getParcela().removerSensor(sensor);
        }

        gestorGranja.getSensoresInventario().remove(sensor);

        if (persistenciaService != null) {
            try {
                persistenciaService.eliminarSensor(idSensor);
            } catch (Exception e) {
                System.out.println("Error eliminando sensor de BD: " + e.getMessage());
            }
        }

        System.out.println("Sensor " + idSensor + " eliminado del sistema.");
    }

    public SensorHumedad buscarSensor(String idSensor) {
        for (SensorHumedad sensor : gestorGranja.getSensoresInventario()) {
            if (sensor.getId().equals(idSensor)) {
                return sensor;
            }
        }
        for (Parcela parcela : gestorGranja.getParcelas()) {
            for (SensorHumedad sensor : parcela.getSensores()) {
                if (sensor.getId().equals(idSensor)) {
                    return sensor;
                }
            }
        }
        return null;
    }
}