package com.granja.negocio;

import com.granja.modelo.Usuario;
import com.granja.servicio.OperacionesCrud;
import com.granja.utilitario.GranjaException;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class GestorUsuarios {
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActual;
    private int contadorUsuarios;
    private OperacionesCrud operacionesCrud;

    public GestorUsuarios(GestorGranja gestorGranja) {
        this.usuarios = new ArrayList<>();
        this.usuarioActual = null;
        this.contadorUsuarios = 1;
    }


    public void setPersistenciaService(OperacionesCrud operacionesCrud) {
        this.operacionesCrud = operacionesCrud;
        cargarUsuariosDesdeDB();
    }

    private void cargarUsuariosDesdeDB() {
        if (operacionesCrud != null) {
            try {
                usuarios = (ArrayList<Usuario>) operacionesCrud.cargarUsuarios();
                if (!usuarios.isEmpty()) {
                    contadorUsuarios = usuarios.size() + 1;
                    return;
                }
            } catch (Exception e) {
                System.out.println("Error cargando usuarios de BD: " + e.getMessage());
            }
        }
        inicializarUsuariosPorDefecto();
    }

    private void inicializarUsuariosPorDefecto() {
        agregarUsuario("Admin", "Sistema", "admin@granja.com", "0999999999", "Administrador");
        agregarUsuario("Juan", "Pérez", "juan.perez@granja.com", "0987654321", "Operador");
        agregarUsuario("María", "González", "maria.gonzalez@granja.com", "0976543210", "Supervisor");
    }

    public boolean agregarUsuario(String nombre, String apellido, String email, String telefono, String rol) {
        String id = "USER_" + contadorUsuarios;
        Usuario usuario = new Usuario(id, nombre, apellido, email, telefono, rol);
        if (operacionesCrud != null) {
            try {
                if(!operacionesCrud.buscarPorEmailActivo(email)) {
                    operacionesCrud.guardarUsuario(usuario);
                    System.out.println("Usuario guardado en BD: " + usuario.getNombreCompleto());
                    usuarios.add(usuario);
                    contadorUsuarios++;
                    return true;
                }else {
                    System.out.println("Este usuario ya se encuentra registrado");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error guardando usuario en BD: " + e.getMessage());
            }
        }
        System.out.println("Usuario registrado: " + usuario.getNombreCompleto() + " (" + id + ")");
        return false;
    }

    public void seleccionarUsuarioActual(String idUsuario) throws GranjaException {
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario == null) {
            throw new GranjaException("Usuario no encontrado: " + idUsuario);
        }
        if (!usuario.isActivo()) {
            throw new GranjaException("El usuario " + idUsuario + " está inactivo");
        }
        usuarioActual = usuario;
        System.out.println("Usuario actual: " + usuario.getNombreCompleto());
    }

    public void mostrarUsuarios() {
        System.out.println("\n========== USUARIOS REGISTRADOS ==========");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }

        for (Usuario usuario : usuarios) {
            String estado = usuario.isActivo() ? "Activo" : "Inactivo";
            System.out.println(usuario + " - " + estado);
        }

        if (usuarioActual != null) {
            System.out.println("\nUsuario actual: " + usuarioActual.getNombreCompleto());
        }
    }

    public Usuario buscarUsuario(String idUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(idUsuario)) {
                return usuario;
            }
        }
        return null;
    }


    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada para: " + usuarioActual.getNombreCompleto());
            usuarioActual = null;
        }
    }
}