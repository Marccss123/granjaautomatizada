package com.granja.negocio;

import com.granja.modelo.Usuario;
import com.granja.servicio.PersistenciaService;
import com.granja.utilitario.GranjaException;
import java.util.ArrayList;

public class GestorUsuarios {
    private GestorGranja gestorGranja;
    private ArrayList<Usuario> usuarios;
    private Usuario usuarioActual;
    private int contadorUsuarios;
    private PersistenciaService persistenciaService;

    public GestorUsuarios(GestorGranja gestorGranja) {
        this.gestorGranja = gestorGranja;
        this.usuarios = new ArrayList<>();
        this.usuarioActual = null;
        this.contadorUsuarios = 1;
    }

    public GestorUsuarios() {
        this.usuarios = new ArrayList<>();
        this.usuarioActual = null;
        this.contadorUsuarios = 1;
    }

    public void setPersistenciaService(PersistenciaService persistenciaService) {
        this.persistenciaService = persistenciaService;
        cargarUsuariosDesdeDB();
    }

    private void cargarUsuariosDesdeDB() {
        if (persistenciaService != null) {
            try {
                usuarios = (ArrayList<Usuario>) persistenciaService.cargarUsuarios();
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

    public void agregarUsuario(String nombre, String apellido, String email, String telefono, String rol) {
        String id = "USER_" + contadorUsuarios;
        Usuario usuario = new Usuario(id, nombre, apellido, email, telefono, rol);
        usuarios.add(usuario);
        contadorUsuarios++;

        if (persistenciaService != null) {
            try {
                persistenciaService.guardarUsuario(usuario);
                System.out.println("Usuario guardado en BD: " + usuario.getNombreCompleto());
            } catch (Exception e) {
                System.out.println("Error guardando usuario en BD: " + e.getMessage());
            }
        }

        System.out.println("Usuario registrado: " + usuario.getNombreCompleto() + " (" + id + ")");
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
            System.out.println(usuario.toString() + " - " + estado);
        }

        if (usuarioActual != null) {
            System.out.println("\nUsuario actual: " + usuarioActual.getNombreCompleto());
        }
    }

    public void editarUsuario(String idUsuario, String nombre, String apellido, String email, String telefono, String rol) throws GranjaException {
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario == null) {
            throw new GranjaException("Usuario no encontrado: " + idUsuario);
        }

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuario.setRol(rol);

        System.out.println("Usuario actualizado: " + usuario.getNombreCompleto());
    }

    public void desactivarUsuario(String idUsuario) throws GranjaException {
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario == null) {
            throw new GranjaException("Usuario no encontrado: " + idUsuario);
        }

        usuario.setActivo(false);
        System.out.println("Usuario desactivado: " + usuario.getNombreCompleto());

        if (usuarioActual != null && usuarioActual.getId().equals(idUsuario)) {
            usuarioActual = null;
            System.out.println("Se ha cerrado la sesión del usuario desactivado");
        }
    }

    public void activarUsuario(String idUsuario) throws GranjaException {
        Usuario usuario = buscarUsuario(idUsuario);
        if (usuario == null) {
            throw new GranjaException("Usuario no encontrado: " + idUsuario);
        }

        usuario.setActivo(true);
        System.out.println("Usuario activado: " + usuario.getNombreCompleto());
    }

    public Usuario buscarUsuario(String idUsuario) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(idUsuario)) {
                return usuario;
            }
        }
        return null;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Usuario> getUsuariosActivos() {
        ArrayList<Usuario> activos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.isActivo()) {
                activos.add(usuario);
            }
        }
        return activos;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void setUsuarioActual(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public void cerrarSesion() {
        if (usuarioActual != null) {
            System.out.println("Sesión cerrada para: " + usuarioActual.getNombreCompleto());
            usuarioActual = null;
        }
    }
}