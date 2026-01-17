package com.granja.hardware;

import com.fazecast.jSerialComm.SerialPort;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArduinoConnector {
    private SerialPort serialPort;
    private boolean conectado;

    public ArduinoConnector() {
        this.conectado = false;
    }

    public List<String> listarPuertosDisponibles() {
        List<String> puertos = new ArrayList<>();
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort port : ports) {
            puertos.add(port.getSystemPortName());
        }

        return puertos;
    }

    public boolean conectar(String nombrePuerto) {
        SerialPort[] ports = SerialPort.getCommPorts();

        for (SerialPort port : ports) {
            if (port.getSystemPortName().equals(nombrePuerto)) {
                serialPort = port;
                serialPort.setComPortParameters(9600, 8, 1, 0);
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

                if (serialPort.openPort()) {
                    conectado = true;
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }

        return false;
    }

    public void desconectar() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            conectado = false;
        }
    }

    public String leerNombreDispositivo() {
        if (!conectado || serialPort == null) {
            return null;
        }

        try {
            enviarComando("GET_NAME");
            Thread.sleep(500);
            return leerRespuesta();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int leerHumedad() {
        if (!conectado || serialPort == null) {
            return -1;
        }

        try {
            enviarComando("GET_HUMIDITY");
            Thread.sleep(500);
            String respuesta = leerRespuesta();
            return Integer.parseInt(Objects.requireNonNull(respuesta).trim());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean activarAspersor() {
        if (!conectado || serialPort == null) {
            return false;
        }

        try {
            enviarComando("ACTIVATE_SPRINKLER");
            Thread.sleep(500);
            String respuesta = leerRespuesta();
            return respuesta != null && respuesta.contains("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivarAspersor() {
        if (!conectado || serialPort == null) {
            return false;
        }

        try {
            enviarComando("DEACTIVATE_SPRINKLER");
            Thread.sleep(500);
            String respuesta = leerRespuesta();
            return respuesta != null && respuesta.contains("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void enviarComando(String comando) {
        if (serialPort != null && serialPort.isOpen()) {
            byte[] buffer = (comando + "\n").getBytes();
            serialPort.writeBytes(buffer, buffer.length);
        }
    }

    private String leerRespuesta() {
        if (serialPort == null || !serialPort.isOpen()) {
            return null;
        }

        try {
            InputStream inputStream = serialPort.getInputStream();
            StringBuilder respuesta = new StringBuilder();
            int bytesDisponibles = inputStream.available();

            if (bytesDisponibles > 0) {
                byte[] buffer = new byte[bytesDisponibles];
                inputStream.read(buffer);
                respuesta.append(new String(buffer));
            }

            return respuesta.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isConectado() {
        return conectado && serialPort != null && serialPort.isOpen();
    }

    public String obtenerInformacionPuerto() {
        if (serialPort != null) {
            return serialPort.getSystemPortName() + " - " + serialPort.getDescriptivePortName();
        }
        return "No conectado";
    }
}
