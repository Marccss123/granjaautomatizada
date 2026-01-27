package com.granja.controlador;

import com.granja.modelo.*;
import com.granja.negocio.*;
import com.granja.servicio.OperacionesCrud;
import com.granja.utilitario.GranjaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GranjaControlador {
    private final GestorGranja gestorGranja;
    private final OperacionesCrud operacionesCrud;

    @Autowired
    public GranjaControlador(GestorGranja gestorGranja, OperacionesCrud operacionesCrud) {
        this.gestorGranja = gestorGranja;
        this.operacionesCrud = operacionesCrud;
        gestorGranja.setPersistenciaService(operacionesCrud);
    }

    public ArrayList<Usuario> obtenerUsuarios() {
        return gestorGranja.getGestorUsuarios().getUsuarios();
    }

    public void seleccionarUsuarioActual(String idUsuario) throws GranjaException {
        gestorGranja.getGestorUsuarios().seleccionarUsuarioActual(idUsuario);
    }

    public Usuario obtenerUsuarioActual() {
        return gestorGranja.getGestorUsuarios().getUsuarioActual();
    }

    public boolean agregarUsuario(String nombre, String apellido, String email, String telefono, String rol) {
        return gestorGranja.getGestorUsuarios().agregarUsuario(nombre, apellido, email, telefono, rol);
    }

    public void cerrarSesionUsuario() {
        gestorGranja.getGestorUsuarios().cerrarSesion();
    }


    public void crearParcelas(double terrenoTotal) throws GranjaException {
        gestorGranja.getGestorParcelas().crearParcelas(terrenoTotal);
    }

    public ArrayList<Parcela> obtenerParcelas() {
        return gestorGranja.getParcelas();
    }

    public void eliminarParcela(String idParcela) throws GranjaException {
        gestorGranja.getGestorParcelas().eliminarParcela(idParcela);
    }


    public ArrayList<Aspersor> obtenerAspersoresInventario() {
        return gestorGranja.getAspersoresInventario();
    }

    public ArrayList<Aspersor> obtenerTodosAspersores() {
        ArrayList<Aspersor> todos = new ArrayList<>(gestorGranja.getAspersoresInventario());
        for (Parcela parcela : gestorGranja.getParcelas()) {
            todos.addAll(parcela.getAspersores());
        }
        return todos;
    }

    public void agregarAspersoresInventario(int cantidad) {
        gestorGranja.getGestorAspersores().agregarAspersoresInventario(cantidad);
    }

    public void asignarAspersorEspecificoAParcela(String idAspersor, String idParcela) throws GranjaException {
        gestorGranja.getGestorAspersores().asignarAspersorEspecificoAParcela(idAspersor, idParcela);
    }

    public void prenderAspersorManualmente(String idAspersor) throws GranjaException {
        gestorGranja.getGestorAspersores().prenderManualmente(idAspersor);
    }

    public void conectarDesconectarAspersor(String idAspersor) throws GranjaException {
        gestorGranja.getGestorAspersores().conectarDesconectarAspersor(idAspersor);
    }

    public void eliminarAspersor(String idAspersor) throws GranjaException {
        gestorGranja.getGestorAspersores().eliminarAspersor(idAspersor);
    }


    public boolean editarUsuario(String idUsuario, String nombre, String apellido, String email, String telefono, String rol) throws GranjaException {
        return gestorGranja.getGestorUsuarios().editarUsuario(idUsuario, nombre, apellido, email, telefono, rol);
    }

    public ArrayList<SensorHumedad> obtenerSensoresInventario() {
        return gestorGranja.getSensoresInventario();
    }

    public ArrayList<SensorHumedad> obtenerTodosSensores() {
        ArrayList<SensorHumedad> todos = new ArrayList<>(gestorGranja.getSensoresInventario());
        for (Parcela parcela : gestorGranja.getParcelas()) {
            todos.addAll(parcela.getSensores());
        }
        return todos;
    }

    public void agregarSensoresInventario(int cantidad) {
        gestorGranja.getGestorSensores().agregarSensoresInventario(cantidad);
    }

    public void asignarSensorEspecificoAParcela(String idSensor, String idParcela) throws GranjaException {
        gestorGranja.getGestorSensores().asignarSensorEspecificoAParcela(idSensor, idParcela);
    }

    public void conectarDesconectarSensor(String idSensor) throws GranjaException {
        gestorGranja.getGestorSensores().conectarDesconectarSensor(idSensor);
    }

    public void eliminarSensor(String idSensor) throws GranjaException {
        gestorGranja.getGestorSensores().eliminarSensor(idSensor);
    }


    public ArrayList<Cultivo> obtenerCultivosDisponibles() {
        return gestorGranja.getGestorCultivos().getCultivosDisponibles();
    }

    public void registrarCultivoEnParcela(String idParcela, String nombreCultivo) throws GranjaException {
        gestorGranja.getGestorCultivos().registrarCultivoEnParcela(idParcela, nombreCultivo);
    }

    public void cambiarCultivoParcela(String idParcela, String nombreCultivo) throws GranjaException {
        gestorGranja.getGestorCultivos().cambiarCultivoParcela(idParcela, nombreCultivo);
    }


    public void simularLecturasYRiego() {
        gestorGranja.getGestorSensores().simularLecturasTodasParcelas();
        gestorGranja.getGestorAspersores().realizarRiegoAutomatico();
    }


    public List<String> escanearArduinos() {
        return gestorGranja.getGestorArduino().escanearPuertosUSB();
    }


    public boolean registrarSensorArduino(String puerto) {
        return gestorGranja.getGestorArduino().registrarSensorDesdeArduino(puerto);
    }


    public boolean registrarAspersorArduino(String puerto) {
        return gestorGranja.getGestorArduino().registrarAspersorDesdeArduino(puerto);
    }


    public void desconectarTodosArduinos() {
        gestorGranja.getGestorArduino().desconectarTodos();
    }


    public boolean encenderLEDArduino(String puerto) {
        System.out.println("\n🔵 ========== ENCENDIENDO LED ARDUINO ==========");
        System.out.println("Puerto: " + puerto);
        com.granja.hardware.ArduinoConnector connector = new com.granja.hardware.ArduinoConnector();

        if (connector.conectar(puerto)) {
            System.out.println("✅ Conectado al puerto " + puerto);

            boolean resultado = connector.encenderLED();

            if (resultado) {
                System.out.println("✅ LED ENCENDIDO - Comando CONNECT enviado");
                System.out.println("💡 El LED del Arduino (pin 13) ahora está encendido");
                return true;
            } else {
                System.out.println("❌ Error: No se recibió respuesta del Arduino");
                connector.desconectar();
                return false;
            }
        } else {
            System.out.println("❌ Error: No se pudo conectar al puerto " + puerto);
            return false;
        }
    }

    public boolean apagarLEDArduino(String puerto) {
        System.out.println("\n⚫ ========== APAGANDO LED ARDUINO ==========");
        System.out.println("Puerto: " + puerto);

        com.granja.hardware.ArduinoConnector connector = new com.granja.hardware.ArduinoConnector();

        if (connector.conectar(puerto)) {
            System.out.println("✅ Conectado al puerto " + puerto);

            boolean resultado = connector.apagarLED();

            if (resultado) {
                System.out.println("✅ LED APAGADO - Comando DISCONNECT enviado");
                System.out.println("💡 El LED del Arduino (pin 13) ahora está apagado");
                connector.desconectar();
                return true;
            } else {
                System.out.println("❌ Error: No se recibió respuesta del Arduino");
                connector.desconectar();
                return false;
            }
        } else {
            System.out.println("❌ Error: No se pudo conectar al puerto " + puerto);
            return false;
        }
    }


    public List<String> obtenerDispositivosArduino() {
        return gestorGranja.getGestorArduino().obtenerDispositivosConectados();
    }


}