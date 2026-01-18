package com.granja.controlador;

import com.granja.modelo.*;
import com.granja.negocio.*;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.GranjaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component // Marca la clase como un componente gestionado por Spring
public class GranjaControlador { // Controlador principal que maneja las operaciones de la granja
    private final GestorGranja gestorGranja;
    private final PersistenciaService persistenciaService;

    @Autowired // Inyección de dependencias a través del constructor
    public GranjaControlador(GestorGranja gestorGranja, PersistenciaService persistenciaService) {
        this.gestorGranja = gestorGranja;
        this.persistenciaService = persistenciaService;
        gestorGranja.setPersistenciaService(persistenciaService);
        //inicializarSistema();
    }


  /*  private void inicializarSistema() {
        gestorGranja.getGestorAspersores().agregarAspersoresInventario(10);
        gestorGranja.getGestorSensores().agregarSensoresInventario(10);
    }*/

    public ArrayList<Usuario> obtenerUsuarios() {
        return gestorGranja.getGestorUsuarios().getUsuarios();
    }

    public ArrayList<Usuario> obtenerUsuariosActivos() {
        return gestorGranja.getGestorUsuarios().getUsuariosActivos();
    }

    public void seleccionarUsuarioActual(String idUsuario) throws GranjaException {
        gestorGranja.getGestorUsuarios().seleccionarUsuarioActual(idUsuario);
    }

    public Usuario obtenerUsuarioActual() {
        return gestorGranja.getGestorUsuarios().getUsuarioActual();
    }

    public void agregarUsuario(String nombre, String apellido, String email, String telefono, String rol) {
        gestorGranja.getGestorUsuarios().agregarUsuario(nombre, apellido, email, telefono, rol);
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

    public ArrayList<Aspersor> obtenerAspersoresInventario() {
        return gestorGranja.getAspersoresInventario();
    }

    public ArrayList<SensorHumedad> obtenerSensoresInventario() {
        return gestorGranja.getSensoresInventario();
    }

    public ArrayList<Aspersor> obtenerTodosAspersores() {
        ArrayList<Aspersor> todos = new ArrayList<>(gestorGranja.getAspersoresInventario());
        for (Parcela parcela : gestorGranja.getParcelas()) {
            todos.addAll(parcela.getAspersores());
        }
        return todos;
    }

    public ArrayList<SensorHumedad> obtenerTodosSensores() {
        ArrayList<SensorHumedad> todos = new ArrayList<>(gestorGranja.getSensoresInventario());
        for (Parcela parcela : gestorGranja.getParcelas()) {
            todos.addAll(parcela.getSensores());
        }
        return todos;
    }

    public void agregarAspersoresInventario(int cantidad) {
        gestorGranja.getGestorAspersores().agregarAspersoresInventario(cantidad);
    }

    public void agregarSensoresInventario(int cantidad) {
        gestorGranja.getGestorSensores().agregarSensoresInventario(cantidad);
    }

    public void asignarAspersorAParcela(String idParcela) throws GranjaException {
        gestorGranja.getGestorAspersores().asignarAspersorAParcela(idParcela);
    }

    public void asignarSensorAParcela(String idParcela) throws GranjaException {
        gestorGranja.getGestorSensores().asignarSensorAParcela(idParcela);
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

    public void eliminarParcela(String idParcela) throws GranjaException {
        gestorGranja.getGestorParcelas().eliminarParcela(idParcela);
    }

    public void simularLecturasYRiego() {
        gestorGranja.getGestorSensores().simularLecturasTodasParcelas();
        gestorGranja.getGestorAspersores().realizarRiegoAutomatico();
    }

    public void prenderAspersorManualmente(String idAspersor) throws GranjaException {
        gestorGranja.getGestorAspersores().prenderManualmente(idAspersor);
    }

    public void conectarDesconectarAspersor(String idAspersor) throws GranjaException {
        gestorGranja.getGestorAspersores().conectarDesconectarAspersor(idAspersor);
    }

    public void conectarDesconectarSensor(String idSensor) throws GranjaException {
        gestorGranja.getGestorSensores().conectarDesconectarSensor(idSensor);
    }

    public void eliminarAspersor(String idAspersor) throws GranjaException {
        gestorGranja.getGestorAspersores().eliminarAspersor(idAspersor);
    }

    public void eliminarSensor(String idSensor) throws GranjaException {
        gestorGranja.getGestorSensores().eliminarSensor(idSensor);
    }

    public Parcela buscarParcela(String idParcela) {
        return gestorGranja.getGestorParcelas().buscarParcela(idParcela);
    }
}