package com.granja.interfaz;

import com.granja.negocio.*;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@ComponentScan(basePackages = "com.granja")
@EntityScan(basePackages = "com.granja.entidad")
@EnableJpaRepositories(basePackages = "com.granja.repositorio")
public class VentanaComandos implements CommandLineRunner {

    @Autowired
    private GestorGranja gestorGranja;

    @Autowired
    private PersistenciaService persistenciaService;

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication app = new SpringApplication(VentanaComandos.class);
        app.setHeadless(false);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========== INICIANDO SISTEMA DE CONSOLA ==========");
        System.out.println("¿Desea ejecutar el modo consola? (s/n)");
        String respuesta = scanner.nextLine().trim().toLowerCase();

        if (respuesta.equals("s") || respuesta.equals("si")) {
            inicializarSistema();
            ejecutarMenuPrincipal();
        } else {
            System.out.println("Ejecutando solo interfaz web Vaadin en http://localhost:8080");
        }
    }

    private void inicializarSistema() {
        gestorGranja.setPersistenciaService(persistenciaService);
        gestorGranja.getGestorAspersores().agregarAspersoresInventario(10);
        gestorGranja.getGestorSensores().agregarSensoresInventario(10);
        System.out.println("Sistema inicializado con 10 aspersores y 10 sensores en inventario.");
    }

    private void ejecutarMenuPrincipal() {
        boolean continuar = true;

        while (continuar) {
            try {
                Util.mostrarMenuPrincipal();
                int opcion = Util.leerEntero("");

                switch (opcion) {
                    case 1:
                        opcionSeleccionarUsuario();
                        break;
                    case 2:
                        gestorGranja.getGestorUsuarios().mostrarUsuarios();
                        break;
                    case 3:
                        opcionAgregarUsuario();
                        break;
                    case 4:
                        gestorGranja.getGestorUsuarios().cerrarSesion();
                        break;
                    case 5:
                        opcionAnadirTerreno();
                        break;
                    case 6:
                        gestorGranja.getGestorParcelas().mostrarInformacionParcelas();
                        break;
                    case 7:
                        opcionEliminarParcela();
                        break;
                    case 8:
                        gestorGranja.getGestorAspersores().mostrarInformacionAspersores();
                        break;
                    case 9:
                        gestorGranja.getGestorSensores().mostrarInformacionSensores();
                        break;
                    case 10:
                        opcionAsignarAspersor();
                        break;
                    case 11:
                        opcionAsignarSensor();
                        break;
                    case 12:
                        opcionAgregarAspersores();
                        break;
                    case 13:
                        opcionAgregarSensores();
                        break;
                    case 14:
                        gestorGranja.getGestorCultivos().mostrarCultivosDisponibles();
                        opcionRegistrarCultivo();
                        break;
                    case 15:
                        opcionCambiarCultivo();
                        break;
                    case 16:
                        opcionRiegoAutomatico();
                        break;
                    case 17:
                        opcionPrenderAspersor();
                        break;
                    case 18:
                        opcionConectarDesconectarAspersor();
                        break;
                    case 19:
                        opcionConectarDesconectarSensor();
                        break;
                    case 20:
                        opcionMostrarLecturasSensor();
                        break;
                    case 21:
                        opcionMostrarHistorialAspersor();
                        break;
                    case 22:
                        opcionEliminarAspersor();
                        break;
                    case 23:
                        opcionEliminarSensor();
                        break;
                    case 24:
                        opcionEscanearArduinos();
                        break;
                    case 25:
                        opcionRegistrarSensorArduino();
                        break;
                    case 26:
                        opcionRegistrarAspersorArduino();
                        break;
                    case 27:
                        opcionMostrarDispositivosArduino();
                        break;
                    case 28:
                        System.out.println("Desconectando dispositivos Arduino...");
                        gestorGranja.getGestorArduino().desconectarTodos();
                        System.out.println("Saliendo del sistema...");
                        continuar = false;
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }
            } catch (GranjaException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void opcionAnadirTerreno() throws GranjaException {
        double terreno = Util.leerDouble("Ingrese el total de terreno en m²: ");
        gestorGranja.getGestorParcelas().crearParcelas(terreno);
    }

    private void opcionAsignarAspersor() throws GranjaException {
        String idParcela = Util.leerCadena("Ingrese el ID de la parcela: ");
        gestorGranja.getGestorAspersores().asignarAspersorAParcela(idParcela);
    }

    private void opcionAsignarSensor() throws GranjaException {
        String idParcela = Util.leerCadena("Ingrese el ID de la parcela: ");
        gestorGranja.getGestorSensores().asignarSensorAParcela(idParcela);
    }

    private void opcionAgregarAspersores() {
        int cantidad = Util.leerEnteroPositivo("¿Cuántos aspersores desea agregar? ");
        gestorGranja.getGestorAspersores().agregarAspersoresInventario(cantidad);
    }

    private void opcionAgregarSensores() {
        int cantidad = Util.leerEnteroPositivo("¿Cuántos sensores desea agregar? ");
        gestorGranja.getGestorSensores().agregarSensoresInventario(cantidad);
    }

    private void opcionRegistrarCultivo() throws GranjaException {
        String idParcela = Util.leerCadena("Ingrese el ID de la parcela: ");
        gestorGranja.getGestorCultivos().mostrarCultivosDisponibles();
        String nombreCultivo = Util.leerCadena("Ingrese el nombre del cultivo: ");
        gestorGranja.getGestorCultivos().registrarCultivoEnParcela(idParcela, nombreCultivo);
    }

    private void opcionEliminarParcela() throws GranjaException {
        String idParcela = Util.leerCadena("Ingrese el ID de la parcela a eliminar: ");
        gestorGranja.getGestorParcelas().eliminarParcela(idParcela);
    }

    private void opcionCambiarCultivo() throws GranjaException {
        String idParcela = Util.leerCadena("Ingrese el ID de la parcela: ");
        gestorGranja.getGestorCultivos().mostrarCultivosDisponibles();
        String nombreCultivo = Util.leerCadena("Ingrese el nombre del cultivo: ");
        gestorGranja.getGestorCultivos().cambiarCultivoParcela(idParcela, nombreCultivo);
    }

    private void opcionRiegoAutomatico() {
        gestorGranja.getGestorSensores().simularLecturasTodasParcelas();
        gestorGranja.getGestorAspersores().realizarRiegoAutomatico();
    }

    private void opcionMostrarLecturasSensor() throws GranjaException {
        String idSensor = Util.leerCadena("Ingrese el ID del sensor: ");
        gestorGranja.getGestorSensores().mostrarLecturasSensor(idSensor);
    }

    private void opcionMostrarHistorialAspersor() throws GranjaException {
        String idAspersor = Util.leerCadena("Ingrese el ID del aspersor: ");
        gestorGranja.getGestorAspersores().mostrarHistorialAspersor(idAspersor);
    }

    private void opcionPrenderAspersor() throws GranjaException {
        String idAspersor = Util.leerCadena("Ingrese el ID del aspersor: ");
        gestorGranja.getGestorAspersores().prenderManualmente(idAspersor);
    }

    private void opcionConectarDesconectarAspersor() throws GranjaException {
        String idAspersor = Util.leerCadena("Ingrese el ID del aspersor: ");
        gestorGranja.getGestorAspersores().conectarDesconectarAspersor(idAspersor);
    }

    private void opcionConectarDesconectarSensor() throws GranjaException {
        String idSensor = Util.leerCadena("Ingrese el ID del sensor: ");
        gestorGranja.getGestorSensores().conectarDesconectarSensor(idSensor);
    }

    private void opcionEliminarAspersor() throws GranjaException {
        String idAspersor = Util.leerCadena("Ingrese el ID del aspersor: ");
        gestorGranja.getGestorAspersores().eliminarAspersor(idAspersor);
    }

    private void opcionEliminarSensor() throws GranjaException {
        String idSensor = Util.leerCadena("Ingrese el ID del sensor: ");
        gestorGranja.getGestorSensores().eliminarSensor(idSensor);
    }

    private void opcionEscanearArduinos() {
        System.out.println("\n========== ESCANEO DE PUERTOS USB ==========");
        List<String> puertos = gestorGranja.getGestorArduino().escanearPuertosUSB();
        if (puertos.isEmpty()) {
            System.out.println("No se detectaron puertos USB.");
        }
    }

    private void opcionRegistrarSensorArduino() {
        String puerto = Util.leerCadena("Ingrese el puerto USB (ej: COM3, /dev/ttyUSB0): ");
        boolean exito = gestorGranja.getGestorArduino().registrarSensorDesdeArduino(puerto);
        if (exito) {
            System.out.println("Sensor Arduino registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar el sensor Arduino.");
        }
    }

    private void opcionRegistrarAspersorArduino() {
        String puerto = Util.leerCadena("Ingrese el puerto USB (ej: COM3, /dev/ttyUSB0): ");
        boolean exito = gestorGranja.getGestorArduino().registrarAspersorDesdeArduino(puerto);
        if (exito) {
            System.out.println("Aspersor Arduino registrado exitosamente.");
        } else {
            System.out.println("No se pudo registrar el aspersor Arduino.");
        }
    }

    private void opcionMostrarDispositivosArduino() {
        System.out.println("\n========== DISPOSITIVOS ARDUINO CONECTADOS ==========");
        List<String> dispositivos = gestorGranja.getGestorArduino().obtenerDispositivosConectados();
        if (dispositivos.isEmpty()) {
            System.out.println("No hay dispositivos Arduino conectados.");
        } else {
            for (String dispositivo : dispositivos) {
                System.out.println("  - " + dispositivo);
            }
        }
    }

    private void opcionSeleccionarUsuario() throws GranjaException {
        gestorGranja.getGestorUsuarios().mostrarUsuarios();
        String idUsuario = Util.leerCadena("\nIngrese el ID del usuario: ");
        gestorGranja.getGestorUsuarios().seleccionarUsuarioActual(idUsuario);
    }

    private void opcionAgregarUsuario() {
        System.out.println("\n========== REGISTRAR NUEVO USUARIO ==========");
        String nombre = Util.leerCadena("Nombre: ");
        String apellido = Util.leerCadena("Apellido: ");
        String email = Util.leerCadena("Email: ");
        String telefono = Util.leerCadena("Teléfono: ");
        String rol = Util.leerCadena("Rol (Administrador/Supervisor/Operador): ");
        gestorGranja.getGestorUsuarios().agregarUsuario(nombre, apellido, email, telefono, rol);
    }
}