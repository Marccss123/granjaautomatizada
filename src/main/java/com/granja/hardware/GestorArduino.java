package com.granja.hardware;

import com.granja.modelo.*;
import com.granja.negocio.GestorGranja;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class GestorArduino {
    private final GestorGranja gestorGranja;
    private final Map<String, ArduinoConnector> conexionesActivas;
    private final Map<String, String> dispositivosPorPuerto;

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
               log.error(e.getMessage());
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

        SensorHumedad sensor = new SensorHumedad(nombreDispositivo);

        gestorGranja.getSensoresInventario().add(sensor);
        dispositivosPorPuerto.put(puerto, nombreDispositivo);

        ArduinoConnector connector = new ArduinoConnector();
        if (connector.conectar(puerto)) {
            conexionesActivas.put(nombreDispositivo, connector);
            sensor.setConectado(true);
            connector.encenderLED();
            System.out.println("Sensor registrado: " + nombreDispositivo + " en puerto " + puerto);
            System.out.println("LED encendido - Sensor conectado");
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

        Aspersor aspersor = new Aspersor(nombreDispositivo);

        gestorGranja.getAspersoresInventario().add(aspersor);
        dispositivosPorPuerto.put(puerto, nombreDispositivo);

        ArduinoConnector connector = new ArduinoConnector();
        if (connector.conectar(puerto)) {
            conexionesActivas.put(nombreDispositivo, connector);
            aspersor.setConectado(true);
            connector.encenderLED();
            System.out.println("Aspersor registrado: " + nombreDispositivo + " en puerto " + puerto);
            System.out.println("LED encendido - Aspersor conectado");
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

    public void desconectarTodos() {
        for (ArduinoConnector connector : conexionesActivas.values()) {
            connector.apagarLED();
            connector.desconectar();
        }
        conexionesActivas.clear();
        dispositivosPorPuerto.clear();
        System.out.println("Todas las conexiones Arduino cerradas");
        System.out.println("Todos los LEDs apagados");
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

}