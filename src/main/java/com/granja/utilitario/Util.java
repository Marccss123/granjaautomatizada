package com.granja.utilitario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

public class Util {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static String generarId(String prefijo, int contador) {
        return prefijo + "_" + contador;
    }

    public static int generarHumedadProgresiva(int humedadActual) {
        int cambio = random.nextInt(11) - 5;
        int nuevaHumedad = humedadActual + cambio;
        return Math.max(0, Math.min(100, nuevaHumedad));
    }

    public static String obtenerFechaHoraActual() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static void mostrarMenuPrincipal() {
        System.out.println("\n========== SISTEMA DE RIEGO AUTOMATIZADO ==========");
        System.out.println("GESTIÓN DE USUARIOS:");
        System.out.println("1. Seleccionar usuario actual");
        System.out.println("2. Mostrar usuarios registrados");
        System.out.println("3. Agregar nuevo usuario");
        System.out.println("4. Cerrar sesión de usuario");
        System.out.println("\nGESTIÓN DE PARCELAS:");
        System.out.println("5. Añadir más terreno");
        System.out.println("6. Mostrar información de las parcelas");
        System.out.println("7. Eliminar una parcela");
        System.out.println("\nGESTIÓN DE DISPOSITIVOS:");
        System.out.println("8. Mostrar información de los aspersores");
        System.out.println("9. Mostrar información de los sensores de humedad");
        System.out.println("10. Añadir aspersor a una parcela");
        System.out.println("11. Añadir sensor de humedad a una parcela");
        System.out.println("12. Agregar aspersores al inventario");
        System.out.println("13. Agregar sensores de humedad al inventario");
        System.out.println("\nGESTIÓN DE CULTIVOS:");
        System.out.println("14. Mostrar lista de cultivos disponibles");
        System.out.println("15. Cambiar cultivo de una parcela");
        System.out.println("\nCONTROL DE RIEGO:");
        System.out.println("16. Verificar humedad y realizar riego automático");
        System.out.println("17. Prender manualmente un aspersor");
        System.out.println("18. Conectar o desconectar un aspersor");
        System.out.println("19. Conectar o desconectar un sensor");
        System.out.println("\nCONSULTAS:");
        System.out.println("20. Mostrar lecturas de un sensor");
        System.out.println("21. Mostrar historial de activación de un aspersor");
        System.out.println("\nMANTENIMIENTO:");
        System.out.println("22. Eliminar un aspersor del sistema");
        System.out.println("23. Eliminar un sensor del sistema");
        System.out.println("\nINTEGRACIÓN ARDUINO:");
        System.out.println("24. Escanear puertos USB para Arduino");
        System.out.println("25. Registrar sensor desde Arduino (USB)");
        System.out.println("26. Registrar aspersor desde Arduino (USB)");
        System.out.println("27. Mostrar dispositivos Arduino conectados");
        System.out.println("\n28. Salir del sistema");
        System.out.println("===================================================");
        System.out.print("Seleccione una opción: ");
    }

    public static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.print("Entrada inválida. " + mensaje);
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine();
        return valor;
    }

    public static int leerEnteroPositivo(String mensaje) {
        int valor;
        do {
            valor = leerEntero(mensaje);
            if (valor <= 0) {
                System.out.println("El valor debe ser positivo.");
            }
        } while (valor <= 0);
        return valor;
    }

    public static String leerCadena(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    public static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextDouble()) {
            System.out.print("Entrada inválida. " + mensaje);
            scanner.next();
        }
        double valor = scanner.nextDouble();
        scanner.nextLine();
        return valor;
    }

    public static boolean confirmarAccion(String mensaje) {
        System.out.print(mensaje + " (S/N): ");
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S");
    }
}