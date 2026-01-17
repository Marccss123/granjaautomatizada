package com.granja.hardware;

import com.granja.modelo.*;
import com.granja.negocio.GestorGranja;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestorArduino {
    private GestorGranja gestorGranja;
    private Map<String, ArduinoConnector> conexionesActivas;
    private Map<String, String> dispositivosPorPuerto;

    public GestorArduino(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
        this.conexionesActivas = new HashMap<>();
        this.dispositivosPorPuerto = new HashMap<>();
    }

    public List<String> escanearPuertosUSB() {
        ArduinoConnector scanner = new ArduinoConnector();
        List<String> puertos = scanner.listarPuertosDisponibles();
        System.out.println("Puertos USB detectados: " + puertos.size());
        for (String puerto : puertos) {
            System.out.println("  - " + puerto);
        }
        return puertos;
    }

    public String detectarDispositivoEnPuerto(String puerto) {
        ArduinoConnector connector = new ArduinoConnector();

        if (connector.conectar(puerto)) {
            try {
                Thread.sleep(1000);
                String nombreDispositivo = connector.leerNombreDispositivo();
                connector.desconectar();

                if (nombreDispositivo != null && !nombreDispositivo.isEmpty()) {
                    System.out.println("Dispositivo detectado en " + puerto + ": " + nombreDispositivo);
                    return nombreDispositivo;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public boolean registrarSensorDesdeArduino(String puerto) {
        String nombreDispositivo = detectarDispositivoEnPuerto(puerto);

        if (nombreDispositivo == null) {
            System.out.println("No se pudo detectar dispositivo en " + puerto);
            return false;
        }

        String idSensor = nombreDispositivo;
        SensorHumedad sensor = new SensorHumedad(idSensor);

        gestorGranja.getSensoresInventario().add(sensor);
        dispositivosPorPuerto.put(puerto, idSensor);

        ArduinoConnector connector = new ArduinoConnector();
        if (connector.conectar(puerto)) {
            conexionesActivas.put(idSensor, connector);
            sensor.setConectado(true);
            System.out.println("Sensor registrado: " + idSensor + " en puerto " + puerto);
            return true;
        }

        return false;
    }

    public boolean registrarAspersorDesdeArduino(String puerto) {
        String nombreDispositivo = detectarDispositivoEnPuerto(puerto);

        if (nombreDispositivo == null) {
            System.out.println("No se pudo detectar dispositivo en " + puerto);
            return false;
        }

        String idAspersor = nombreDispositivo;
        Aspersor aspersor = new Aspersor(idAspersor);

        gestorGranja.getAspersoresInventario().add(aspersor);
        dispositivosPorPuerto.put(puerto, idAspersor);

        ArduinoConnector connector = new ArduinoConnector();
        if (connector.conectar(puerto)) {
            conexionesActivas.put(idAspersor, connector);
            aspersor.setConectado(true);
            System.out.println("Aspersor registrado: " + idAspersor + " en puerto " + puerto);
            return true;
        }

        return false;
    }

    public int leerHumedadReal(String idSensor) {
        ArduinoConnector connector = conexionesActivas.get(idSensor);

        if (connector != null && connector.isConectado()) {
            int humedad = connector.leerHumedad();
            if (humedad >= 0) {
                return humedad;
            }
        }

        return -1;
    }

    public boolean activarAspersorReal(String idAspersor) {
        ArduinoConnector connector = conexionesActivas.get(idAspersor);

        if (connector != null && connector.isConectado()) {
            return connector.activarAspersor();
        }

        return false;
    }

    public boolean desactivarAspersorReal(String idAspersor) {
        ArduinoConnector connector = conexionesActivas.get(idAspersor);

        if (connector != null && connector.isConectado()) {
            return connector.desactivarAspersor();
        }

        return false;
    }

    public void sincronizarTodosSensores() {
        System.out.println("Sincronizando sensores con hardware...");

        for (Parcela parcela : gestorGranja.getParcelas()) {
            for (SensorHumedad sensor : parcela.getSensores()) {
                if (conexionesActivas.containsKey(sensor.getId())) {
                    int humedadReal = leerHumedadReal(sensor.getId());
                    if (humedadReal >= 0) {
                        sensor.setHumedadActual(humedadReal);
                        sensor.realizarLectura();
                        System.out.println(sensor.getId() + " - Humedad real: " + humedadReal + "%");
                    }
                }
            }
        }
    }

    public void desconectarTodos() {
        for (ArduinoConnector connector : conexionesActivas.values()) {
            connector.desconectar();
        }
        conexionesActivas.clear();
        dispositivosPorPuerto.clear();
        System.out.println("Todas las conexiones Arduino cerradas");
    }

    public List<String> obtenerDispositivosConectados() {
        List<String> dispositivos = new ArrayList<>();
        for (Map.Entry<String, ArduinoConnector> entry : conexionesActivas.entrySet()) {
            if (entry.getValue().isConectado()) {
                dispositivos.add(entry.getKey() + " - " + entry.getValue().obtenerInformacionPuerto());
            }
        }
        return dispositivos;
    }

    public boolean esDispositivoArduino(String idDispositivo) {
        return conexionesActivas.containsKey(idDispositivo);
    }

    public void reconectarDispositivo(String idDispositivo) {
        for (Map.Entry<String, String> entry : dispositivosPorPuerto.entrySet()) {
            if (entry.getValue().equals(idDispositivo)) {
                String puerto = entry.getKey();

                ArduinoConnector oldConnector = conexionesActivas.get(idDispositivo);
                if (oldConnector != null) {
                    oldConnector.desconectar();
                }

                ArduinoConnector newConnector = new ArduinoConnector();
                if (newConnector.conectar(puerto)) {
                    conexionesActivas.put(idDispositivo, newConnector);
                    System.out.println("Dispositivo " + idDispositivo + " reconectado");
                }
                break;
            }
        }
    }
}
