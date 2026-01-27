package com.granja.interfaz;

import com.granja.controlador.GranjaControlador;
import com.granja.modelo.*;
import com.granja.utilitario.GranjaException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class VistaPrincipal extends VerticalLayout {
    private final GranjaControlador controller;
    private final VerticalLayout contentLayout;
    private HorizontalLayout userBar;

    @Autowired
    public VistaPrincipal(GranjaControlador controller) {
        this.controller = controller;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H1 title = new H1("Sistema de Riego Automatizado");
        title.getStyle().set("color", "#2e7d32");

        userBar = createUserBar();
        Tabs tabs = createTabs();
        contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();

        add(title, userBar, tabs, contentLayout);

        mostrarVistaUsuarios();
    }

    private HorizontalLayout createUserBar() {
        HorizontalLayout userBarLayout = new HorizontalLayout();
        userBarLayout.setWidthFull();
        userBarLayout.getStyle().set("background-color", "#f5f5f5");
        userBarLayout.getStyle().set("padding", "10px");
        userBarLayout.getStyle().set("border-radius", "5px");
        userBarLayout.setAlignItems(Alignment.CENTER);
        userBarLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        HorizontalLayout userInfo = new HorizontalLayout();
        userInfo.setAlignItems(Alignment.CENTER);

        Usuario usuarioActual = controller.obtenerUsuarioActual();
        String textoUsuario = usuarioActual != null ?
                "Usuario: " + usuarioActual.getNombreCompleto() + " (" + usuarioActual.getRol() + ")" :
                "Sin usuario seleccionado";

        com.vaadin.flow.component.html.Span userLabel = new com.vaadin.flow.component.html.Span(textoUsuario);
        userLabel.getStyle().set("font-weight", "bold");

        Button cambiarUsuarioBtn = new Button("Cambiar Usuario", e -> mostrarVistaUsuarios());

        userInfo.add(userLabel);
        userBarLayout.add(userInfo, cambiarUsuarioBtn);

        return userBarLayout;
    }

    private void actualizarBarraUsuario() {
        remove(userBar);
        userBar = createUserBar();
        addComponentAtIndex(1, userBar);
    }

    private Tabs createTabs() {
        Tab usuariosTab = new Tab("Usuarios");
        Tab parcelasTab = new Tab("Parcelas");
        Tab aspersoresTab = new Tab("Aspersores");
        Tab sensoresTab = new Tab("Sensores");
        Tab cultivosTab = new Tab("Cultivos");
        Tab riegoTab = new Tab("Riego Automático");
        Tab arduinoTab = new Tab("Arduino"); // NUEVO TAB

        Tabs tabs = new Tabs(usuariosTab, parcelasTab, aspersoresTab, sensoresTab, cultivosTab, riegoTab, arduinoTab);

        tabs.addSelectedChangeListener(event -> {
            Tab selectedTab = event.getSelectedTab();
            if (selectedTab == usuariosTab) {
                mostrarVistaUsuarios();
            } else if (selectedTab == parcelasTab) {
                mostrarVistaParcelas();
            } else if (selectedTab == aspersoresTab) {
                mostrarVistaAspersores();
            } else if (selectedTab == sensoresTab) {
                mostrarVistaSensores();
            } else if (selectedTab == cultivosTab) {
                mostrarVistaCultivos();
            } else if (selectedTab == riegoTab) {
                mostrarVistaRiego();
            } else if (selectedTab == arduinoTab) {
                mostrarVistaArduino();
            }
        });

        return tabs;
    }

    private void mostrarVistaUsuarios() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Usuarios");

        Usuario usuarioActual = controller.obtenerUsuarioActual();

        if (usuarioActual != null) {
            com.vaadin.flow.component.html.Div infoActual = new com.vaadin.flow.component.html.Div();
            infoActual.getStyle().set("background-color", "#e8f5e9");
            infoActual.getStyle().set("padding", "15px");
            infoActual.getStyle().set("border-radius", "5px");
            infoActual.getStyle().set("margin-bottom", "20px");
            infoActual.add(new com.vaadin.flow.component.html.Span(
                    "Usuario actual: " + usuarioActual.getNombreCompleto() + " - " + usuarioActual.getRol()
            ));

            Button cerrarSesionBtn = new Button("Cerrar Sesión", e -> {
                controller.cerrarSesionUsuario();
                mostrarNotificacion("Sesión cerrada", NotificationVariant.LUMO_SUCCESS);
                actualizarBarraUsuario();
                actualizarVistaUsuarios();
            });
            cerrarSesionBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

            infoActual.add(cerrarSesionBtn);
            contentLayout.add(infoActual);
        }

        TextField nombreField = new TextField("Nombre");
        nombreField.setPlaceholder("Ej: Juan");
        nombreField.setPattern("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
        nombreField.setErrorMessage("Solo se permiten letras y espacios");
        nombreField.setRequired(true);
        nombreField.setHelperText("Solo letras");

        nombreField.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null && !valor.isEmpty()) {
                String valorLimpio = valor.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "");
                if (!valor.equals(valorLimpio)) {
                    nombreField.setValue(valorLimpio);
                    nombreField.setInvalid(true);
                } else {
                    nombreField.setInvalid(false);
                }
            }
        });

        TextField apellidoField = new TextField("Apellido");
        apellidoField.setPlaceholder("Ej: Pérez");
        apellidoField.setPattern("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
        apellidoField.setErrorMessage("Solo se permiten letras y espacios");
        apellidoField.setRequired(true);
        apellidoField.setHelperText("Solo letras");

        apellidoField.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null && !valor.isEmpty()) {
                String valorLimpio = valor.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "");
                if (!valor.equals(valorLimpio)) {
                    apellidoField.setValue(valorLimpio);
                    apellidoField.setInvalid(true);
                } else {
                    apellidoField.setInvalid(false);
                }
            }
        });

        TextField emailField = new TextField("Email");
        emailField.setPlaceholder("ejemplo@correo.com");
        emailField.setRequired(true);
        emailField.setHelperText("Debe contener @ y .com");

        emailField.addValueChangeListener(event -> {
            String email = event.getValue();
            if (email != null && !email.isEmpty()) {
                boolean tieneArroba = email.contains("@");
                boolean tieneCom = email.toLowerCase().contains(".com");

                if (!tieneArroba || !tieneCom) {
                    emailField.setInvalid(true);
                    if (!tieneArroba && !tieneCom) {
                        emailField.setErrorMessage("Falta @ y .com");
                    } else if (!tieneArroba) {
                        emailField.setErrorMessage("Falta el símbolo @");
                    } else {
                        emailField.setErrorMessage("Debe terminar con .com");
                    }
                } else {
                    emailField.setInvalid(false);
                    emailField.setErrorMessage("");
                }
            }
        });

        TextField telefonoField = new TextField("Teléfono");
        telefonoField.setPlaceholder("8091234567");
        telefonoField.setPattern("\\d+");
        telefonoField.setHelperText("Solo números");
        telefonoField.setMaxLength(15);

        telefonoField.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null && !valor.isEmpty()) {
                String valorLimpio = valor.replaceAll("[^0-9]", "");
                if (!valor.equals(valorLimpio)) {
                    telefonoField.setValue(valorLimpio);
                }
            }
        });

        TextField rolField = new TextField("Rol");
        rolField.setPlaceholder("Administrador/Supervisor/Operador");
        rolField.setRequired(true);

        Button agregarBtn = new Button("Agregar Usuario", e -> {
            boolean camposValidos = true;
            StringBuilder errores = new StringBuilder();

            if (nombreField.getValue() == null || nombreField.getValue().trim().isEmpty()) {
                errores.append("• El nombre es obligatorio\n");
                nombreField.setInvalid(true);
                camposValidos = false;
            } else if (!nombreField.getValue().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                errores.append("• El nombre solo puede contener letras\n");
                nombreField.setInvalid(true);
                camposValidos = false;
            }

            if (apellidoField.getValue() == null || apellidoField.getValue().trim().isEmpty()) {
                errores.append("• El apellido es obligatorio\n");
                apellidoField.setInvalid(true);
                camposValidos = false;
            } else if (!apellidoField.getValue().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                errores.append("• El apellido solo puede contener letras\n");
                apellidoField.setInvalid(true);
                camposValidos = false;
            }

            String email = emailField.getValue();
            if (email == null || email.trim().isEmpty()) {
                errores.append("• El email es obligatorio\n");
                emailField.setInvalid(true);
                camposValidos = false;
            } else {
                if (!email.contains("@")) {
                    errores.append("• El email debe contener @\n");
                    emailField.setInvalid(true);
                    camposValidos = false;
                }
                if (!email.toLowerCase().contains(".com")) {
                    errores.append("• El email debe contener .com\n");
                    emailField.setInvalid(true);
                    camposValidos = false;
                }
            }

            if (telefonoField.getValue() == null || telefonoField.getValue().trim().isEmpty()) {
                errores.append("• El teléfono es obligatorio\n");
                telefonoField.setInvalid(true);
                camposValidos = false;
            }

            if (rolField.getValue() == null || rolField.getValue().trim().isEmpty()) {
                errores.append("• El rol es obligatorio\n");
                rolField.setInvalid(true);
                camposValidos = false;
            }

            if (!camposValidos) {
                mostrarNotificacion("Errores de validación:\n" + errores,
                        NotificationVariant.LUMO_ERROR);
                return;
            }

            boolean flag = controller.agregarUsuario(
                    nombreField.getValue().trim(),
                    apellidoField.getValue().trim(),
                    emailField.getValue().trim(),
                    telefonoField.getValue().trim(),
                    rolField.getValue().trim()
            );

            if (flag) {
                mostrarNotificacion("Usuario agregado exitosamente", NotificationVariant.LUMO_SUCCESS);
                nombreField.clear();
                apellidoField.clear();
                emailField.clear();
                telefonoField.clear();
                rolField.clear();
                nombreField.setInvalid(false);
                apellidoField.setInvalid(false);
                emailField.setInvalid(false);
                telefonoField.setInvalid(false);
                rolField.setInvalid(false);
                actualizarVistaUsuarios();
            } else {
                mostrarNotificacion("Error al registrar el usuario, favor verificar si el mismo ya se encuentra registrado",
                        NotificationVariant.LUMO_ERROR);
            }
        });
        agregarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout formLayout = new HorizontalLayout(nombreField, apellidoField, emailField,
                telefonoField, rolField, agregarBtn);
        formLayout.setAlignItems(Alignment.BASELINE);

        Grid<Usuario> grid = new Grid<>(Usuario.class, false);
        grid.addColumn(Usuario::getId).setHeader("ID").setWidth("120px").setFlexGrow(0);
        grid.addColumn(Usuario::getNombreCompleto).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Usuario::getEmail).setHeader("Email").setAutoWidth(true);
        grid.addColumn(Usuario::getTelefono).setHeader("Teléfono").setWidth("130px").setFlexGrow(0);
        grid.addColumn(Usuario::getRol).setHeader("Rol").setWidth("130px").setFlexGrow(0);
        grid.addColumn(u -> u.isActivo() ? "Activo" : "Inactivo").setHeader("Estado").setWidth("100px").setFlexGrow(0);

        grid.addComponentColumn(usuario -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);

            Button seleccionarBtn = new Button("Seleccionar", ev -> {
                try {
                    controller.seleccionarUsuarioActual(usuario.getId());
                    mostrarNotificacion("Usuario seleccionado: " + usuario.getNombreCompleto(),
                            NotificationVariant.LUMO_SUCCESS);
                    actualizarBarraUsuario();
                    actualizarVistaUsuarios();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            seleccionarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);

            Button editarBtn = new Button("✏️ Editar", ev -> mostrarDialogoEditarUsuario(usuario));
            editarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);

            actions.add(seleccionarBtn, editarBtn);
            return actions;
        }).setHeader("Acciones").setAutoWidth(true);

        grid.setItems(controller.obtenerUsuarios());
        grid.setHeight("400px");

        contentLayout.add(subtitle, formLayout, grid);
    }

    private void mostrarDialogoEditarUsuario(Usuario usuario) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setPadding(true);
        contenido.setSpacing(true);

        com.vaadin.flow.component.html.H3 titulo = new com.vaadin.flow.component.html.H3("✏️ Editar Usuario: " + usuario.getId());
        titulo.getStyle().set("margin", "0 0 20px 0");
        titulo.getStyle().set("color", "#1976d2");

        TextField nombreField = new TextField("Nombre");
        nombreField.setValue(usuario.getNombre());
        nombreField.setRequired(true);
        nombreField.setPattern("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
        nombreField.setErrorMessage("Solo se permiten letras y espacios");
        nombreField.setWidthFull();

        nombreField.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null && !valor.isEmpty()) {
                String valorLimpio = valor.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "");
                if (!valor.equals(valorLimpio)) {
                    nombreField.setValue(valorLimpio);
                    nombreField.setInvalid(true);
                } else {
                    nombreField.setInvalid(false);
                }
            }
        });

        TextField apellidoField = new TextField("Apellido");
        apellidoField.setValue(usuario.getApellido());
        apellidoField.setRequired(true);
        apellidoField.setPattern("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
        apellidoField.setErrorMessage("Solo se permiten letras y espacios");
        apellidoField.setWidthFull();

        apellidoField.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null && !valor.isEmpty()) {
                String valorLimpio = valor.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]", "");
                if (!valor.equals(valorLimpio)) {
                    apellidoField.setValue(valorLimpio);
                    apellidoField.setInvalid(true);
                } else {
                    apellidoField.setInvalid(false);
                }
            }
        });

        TextField emailField = new TextField("Email");
        emailField.setValue(usuario.getEmail());
        emailField.setRequired(true);
        emailField.setHelperText("Debe contener @ y .com");
        emailField.setWidthFull();

        emailField.addValueChangeListener(event -> {
            String email = event.getValue();
            if (email != null && !email.isEmpty()) {
                boolean tieneArroba = email.contains("@");
                boolean tieneCom = email.toLowerCase().contains(".com");

                if (!tieneArroba || !tieneCom) {
                    emailField.setInvalid(true);
                    if (!tieneArroba && !tieneCom) {
                        emailField.setErrorMessage("Falta @ y .com");
                    } else if (!tieneArroba) {
                        emailField.setErrorMessage("Falta el símbolo @");
                    } else {
                        emailField.setErrorMessage("Debe terminar con .com");
                    }
                } else {
                    emailField.setInvalid(false);
                    emailField.setErrorMessage("");
                }
            }
        });

        TextField telefonoField = new TextField("Teléfono");
        telefonoField.setValue(usuario.getTelefono());
        telefonoField.setRequired(true);
        telefonoField.setPattern("\\d+");
        telefonoField.setHelperText("Solo números");
        telefonoField.setMaxLength(15);
        telefonoField.setWidthFull();

        telefonoField.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null && !valor.isEmpty()) {
                String valorLimpio = valor.replaceAll("[^0-9]", "");
                if (!valor.equals(valorLimpio)) {
                    telefonoField.setValue(valorLimpio);
                }
            }
        });

        TextField rolField = new TextField("Rol");
        rolField.setValue(usuario.getRol());
        rolField.setRequired(true);
        rolField.setWidthFull();

        Button guardarBtn = new Button("💾 Guardar Cambios", e -> {
            boolean camposValidos = true;
            StringBuilder errores = new StringBuilder();

            if (nombreField.getValue() == null || nombreField.getValue().trim().isEmpty()) {
                errores.append("• El nombre es obligatorio\n");
                nombreField.setInvalid(true);
                camposValidos = false;
            } else if (!nombreField.getValue().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                errores.append("• El nombre solo puede contener letras\n");
                nombreField.setInvalid(true);
                camposValidos = false;
            }

            if (apellidoField.getValue() == null || apellidoField.getValue().trim().isEmpty()) {
                errores.append("• El apellido es obligatorio\n");
                apellidoField.setInvalid(true);
                camposValidos = false;
            } else if (!apellidoField.getValue().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
                errores.append("• El apellido solo puede contener letras\n");
                apellidoField.setInvalid(true);
                camposValidos = false;
            }

            String email = emailField.getValue();
            if (email == null || email.trim().isEmpty()) {
                errores.append("• El email es obligatorio\n");
                emailField.setInvalid(true);
                camposValidos = false;
            } else {
                if (!email.contains("@")) {
                    errores.append("• El email debe contener @\n");
                    emailField.setInvalid(true);
                    camposValidos = false;
                }
                if (!email.toLowerCase().contains(".com")) {
                    errores.append("• El email debe contener .com\n");
                    emailField.setInvalid(true);
                    camposValidos = false;
                }
            }

            if (telefonoField.getValue() == null || telefonoField.getValue().trim().isEmpty()) {
                errores.append("• El teléfono es obligatorio\n");
                telefonoField.setInvalid(true);
                camposValidos = false;
            }

            if (rolField.getValue() == null || rolField.getValue().trim().isEmpty()) {
                errores.append("• El rol es obligatorio\n");
                rolField.setInvalid(true);
                camposValidos = false;
            }

            if (!camposValidos) {
                mostrarNotificacion("Errores de validación:\n" + errores,
                        NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                boolean exito = controller.editarUsuario(
                        usuario.getId(),
                        nombreField.getValue().trim(),
                        apellidoField.getValue().trim(),
                        emailField.getValue().trim(),
                        telefonoField.getValue().trim(),
                        rolField.getValue().trim()
                );

                if (exito) {
                    mostrarNotificacion("Usuario actualizado exitosamente", NotificationVariant.LUMO_SUCCESS);
                    dialog.close();
                    actualizarBarraUsuario();
                    actualizarVistaUsuarios();
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        guardarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        Button cancelarBtn = new Button("Cancelar", e -> dialog.close());
        cancelarBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout botonesLayout = new HorizontalLayout(guardarBtn, cancelarBtn);
        botonesLayout.setJustifyContentMode(JustifyContentMode.END);
        botonesLayout.setWidthFull();
        botonesLayout.getStyle().set("margin-top", "20px");

        contenido.add(titulo, nombreField, apellidoField, emailField, telefonoField, rolField, botonesLayout);
        dialog.add(contenido);
        dialog.open();
    }

    private void mostrarVistaParcelas() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Parcelas");

        NumberField terrenoField = new NumberField("Terreno Total (m²)");
        terrenoField.setMin(1);
        terrenoField.setValue(100.0);

        Button crearButton = new Button("Crear Parcelas", e -> {
            try {
                controller.crearParcelas(terrenoField.getValue());
                mostrarNotificacion("Parcelas creadas exitosamente", NotificationVariant.LUMO_SUCCESS);
                actualizarGridParcelas();
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        crearButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout formLayout = new HorizontalLayout(terrenoField, crearButton);
        formLayout.setAlignItems(Alignment.BASELINE);

        Grid<Parcela> grid = new Grid<>(Parcela.class, false);
        grid.addColumn(Parcela::getId).setHeader("ID");
        grid.addColumn(Parcela::getMetrosCuadrados).setHeader("Área (m²)");
        grid.addColumn(p -> p.getCultivo() != null ? p.getCultivo().getNombre() : "Sin cultivo").setHeader("Cultivo");
        grid.addColumn(p -> p.getAspersores().size()).setHeader("Aspersores");
        grid.addColumn(p -> p.getSensores().size()).setHeader("Sensores");
        grid.addColumn(p -> p.getUsuarioCreador() != null ? p.getUsuarioCreador().getNombreCompleto() : "N/A").setHeader("Creado por");

        grid.addComponentColumn(parcela -> {
            Button eliminarBtn = new Button("Eliminar", ev -> mostrarDialogoConfirmacion("¿Eliminar parcela " + parcela.getId() + "?", () -> {
                try {
                    controller.eliminarParcela(parcela.getId());
                    mostrarNotificacion("Parcela eliminada", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridParcelas();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            }));
            eliminarBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            return eliminarBtn;
        }).setHeader("Acciones");

        grid.setItems(controller.obtenerParcelas());

        contentLayout.add(subtitle, formLayout, grid);
    }


    private void mostrarVistaAspersores() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Aspersores");

        NumberField cantidadField = new NumberField("Cantidad");
        cantidadField.setMin(1);
        cantidadField.setValue(5.0);

        Button agregarButton = new Button("Agregar al Inventario", e -> {
            controller.agregarAspersoresInventario(cantidadField.getValue().intValue());
            mostrarNotificacion("Aspersores agregados al inventario", NotificationVariant.LUMO_SUCCESS);
            actualizarGridAspersores();
        });
        agregarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        ComboBox<Aspersor> aspersorCombo = new ComboBox<>("Seleccionar Aspersor");
        aspersorCombo.setItems(controller.obtenerAspersoresInventario());
        aspersorCombo.setItemLabelGenerator(Aspersor::getId);
        aspersorCombo.setPlaceholder("Elegir aspersor...");
        aspersorCombo.setWidth("200px");

        ComboBox<Parcela> parcelaCombo = new ComboBox<>("Seleccionar Parcela");
        parcelaCombo.setItems(controller.obtenerParcelas());
        parcelaCombo.setItemLabelGenerator(Parcela::getId);
        parcelaCombo.setPlaceholder("Elegir parcela...");
        parcelaCombo.setWidth("200px");

        Button asignarButton = new Button("Asignar a Parcela", e -> {
            try {
                if (aspersorCombo.getValue() != null && parcelaCombo.getValue() != null) {
                    controller.asignarAspersorEspecificoAParcela(
                            aspersorCombo.getValue().getId(),
                            parcelaCombo.getValue().getId()
                    );
                    mostrarNotificacion("Aspersor asignado exitosamente", NotificationVariant.LUMO_SUCCESS);
                    aspersorCombo.clear();
                    parcelaCombo.clear();
                    actualizarGridAspersores();
                } else {
                    mostrarNotificacion("Debe seleccionar un aspersor y una parcela", NotificationVariant.LUMO_ERROR);
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        asignarButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout formLayout1 = new HorizontalLayout(cantidadField, agregarButton);
        HorizontalLayout formLayout2 = new HorizontalLayout(aspersorCombo, parcelaCombo, asignarButton);
        formLayout1.setAlignItems(Alignment.BASELINE);
        formLayout2.setAlignItems(Alignment.BASELINE);

        Grid<Aspersor> grid = new Grid<>(Aspersor.class, false);
        grid.addColumn(Aspersor::getId).setHeader("ID").setWidth("120px").setFlexGrow(0);
        grid.addColumn(a -> a.isConectado() ? "Conectado" : "Desconectado").setHeader("Estado").setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(a -> a.isEncendido() ? "Encendido" : "Apagado").setHeader("Riego").setWidth("100px").setFlexGrow(0);
        grid.addColumn(a -> a.getParcela() != null ? a.getParcela().getId() : "Inventario").setHeader("Ubicación").setWidth("120px").setFlexGrow(0);

        grid.addComponentColumn(aspersor -> {
            VerticalLayout actions = new VerticalLayout();
            actions.setPadding(false);
            actions.setSpacing(true);

            HorizontalLayout fila1 = new HorizontalLayout();
            fila1.setSpacing(true);

            Button conectarBtn = new Button(aspersor.isConectado() ? "Desconectar" : "Conectar", ev -> {
                try {
                    controller.conectarDesconectarAspersor(aspersor.getId());
                    mostrarNotificacion("Estado actualizado", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridAspersores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            conectarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            conectarBtn.setWidth("110px");

            Button encenderBtn = new Button("💧 Encender", ev -> {
                try {
                    controller.prenderAspersorManualmente(aspersor.getId());
                    mostrarNotificacion("Aspersor encendido", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridAspersores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            encenderBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);
            encenderBtn.setWidth("110px");

            fila1.add(conectarBtn, encenderBtn);

            HorizontalLayout fila2 = new HorizontalLayout();
            fila2.setSpacing(true);

            Button historialBtn = new Button("📊 Historial", ev -> mostrarHistorialAspersorPopup(aspersor));
            historialBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            historialBtn.setWidth("110px");

            Button eliminarBtn = new Button("🗑️ Eliminar", ev -> mostrarDialogoConfirmacion("¿Eliminar aspersor " + aspersor.getId() + "?", () -> {
                try {
                    controller.eliminarAspersor(aspersor.getId());
                    mostrarNotificacion("Aspersor eliminado", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridAspersores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            }));
            eliminarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            eliminarBtn.setWidth("110px");

            fila2.add(historialBtn, eliminarBtn);

            actions.add(fila1, fila2);
            return actions;
        }).setHeader("Acciones").setAutoWidth(true);

        grid.setItems(controller.obtenerTodosAspersores());
        grid.setHeight("500px");

        contentLayout.add(subtitle, formLayout1, formLayout2, grid);
    }

    private void mostrarHistorialAspersorPopup(Aspersor aspersor) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setMaxHeight("80vh");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setPadding(true);
        contenido.setSpacing(true);

        com.vaadin.flow.component.html.H3 titulo = new com.vaadin.flow.component.html.H3("📊 Historial del Aspersor " + aspersor.getId());
        titulo.getStyle().set("margin", "0 0 20px 0");
        titulo.getStyle().set("color", "#1976d2");

        VerticalLayout infoGeneral = new VerticalLayout();
        infoGeneral.setPadding(true);
        infoGeneral.setSpacing(false);
        infoGeneral.getStyle().set("background-color", "#f5f5f5");
        infoGeneral.getStyle().set("border-radius", "5px");
        infoGeneral.getStyle().set("margin-bottom", "15px");

        com.vaadin.flow.component.html.Div infoEstado = new com.vaadin.flow.component.html.Div();
        infoEstado.setText("🔌 Estado: " + (aspersor.isConectado() ? "Conectado" : "Desconectado"));
        infoEstado.getStyle().set("padding", "5px 0");

        com.vaadin.flow.component.html.Div infoRiego = new com.vaadin.flow.component.html.Div();
        infoRiego.setText("💧 Riego: " + (aspersor.isEncendido() ? "Encendido" : "Apagado"));
        infoRiego.getStyle().set("padding", "5px 0");

        com.vaadin.flow.component.html.Div infoUbicacion = new com.vaadin.flow.component.html.Div();
        infoUbicacion.setText("📍 Ubicación: " + (aspersor.getParcela() != null ? aspersor.getParcela().getId() : "Inventario"));
        infoUbicacion.getStyle().set("padding", "5px 0");

        infoGeneral.add(infoEstado, infoRiego, infoUbicacion);

        com.vaadin.flow.component.html.H4 subtituloHistorial = new com.vaadin.flow.component.html.H4("📅 Historial de Encendidos");
        subtituloHistorial.getStyle().set("margin", "10px 0");

        VerticalLayout listaHistorial = new VerticalLayout();
        listaHistorial.setPadding(false);
        listaHistorial.setSpacing(true);
        listaHistorial.getStyle().set("max-height", "300px");
        listaHistorial.getStyle().set("overflow-y", "auto");

        if (aspersor.getHistorialEncendidos().isEmpty()) {
            com.vaadin.flow.component.html.Div mensajeVacio = new com.vaadin.flow.component.html.Div();
            mensajeVacio.setText("ℹ️ No hay registros de encendidos para este aspersor");
            mensajeVacio.getStyle().set("padding", "20px");
            mensajeVacio.getStyle().set("text-align", "center");
            mensajeVacio.getStyle().set("color", "#757575");
            mensajeVacio.getStyle().set("background-color", "#f5f5f5");
            mensajeVacio.getStyle().set("border-radius", "5px");
            listaHistorial.add(mensajeVacio);
        } else {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            int contador = 1;

            java.util.List<java.time.LocalDateTime> historialOrdenado = new java.util.ArrayList<>(aspersor.getHistorialEncendidos());
            java.util.Collections.reverse(historialOrdenado);

            for (java.time.LocalDateTime fecha : historialOrdenado) {
                com.vaadin.flow.component.html.Div itemHistorial = new com.vaadin.flow.component.html.Div();
                itemHistorial.setText("🕐 #" + contador + " - " + fecha.format(formatter));
                itemHistorial.getStyle().set("padding", "10px");
                itemHistorial.getStyle().set("background-color", "#e3f2fd");
                itemHistorial.getStyle().set("border-radius", "5px");
                itemHistorial.getStyle().set("border-left", "4px solid #2196f3");
                itemHistorial.getStyle().set("margin-bottom", "5px");
                listaHistorial.add(itemHistorial);
                contador++;
            }

            com.vaadin.flow.component.html.Div resumen = new com.vaadin.flow.component.html.Div();
            resumen.setText("📈 Total de encendidos: " + aspersor.getHistorialEncendidos().size());
            resumen.getStyle().set("padding", "10px");
            resumen.getStyle().set("background-color", "#c8e6c9");
            resumen.getStyle().set("border-radius", "5px");
            resumen.getStyle().set("font-weight", "bold");
            resumen.getStyle().set("margin-top", "10px");
            listaHistorial.add(resumen);
        }

        Button cerrarBtn = new Button("Cerrar", e -> dialog.close());
        cerrarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cerrarBtn.getStyle().set("margin-top", "15px");

        contenido.add(titulo, infoGeneral, subtituloHistorial, listaHistorial, cerrarBtn);
        dialog.add(contenido);
        dialog.open();
    }

    private void mostrarVistaSensores() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Sensores");

        NumberField cantidadField = new NumberField("Cantidad");
        cantidadField.setMin(1);
        cantidadField.setValue(5.0);

        Button agregarButton = new Button("Agregar al Inventario", e -> {
            controller.agregarSensoresInventario(cantidadField.getValue().intValue());
            mostrarNotificacion("Sensores agregados al inventario", NotificationVariant.LUMO_SUCCESS);
            actualizarGridSensores();
        });
        agregarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        ComboBox<SensorHumedad> sensorCombo = new ComboBox<>("Seleccionar Sensor");
        sensorCombo.setItems(controller.obtenerSensoresInventario());
        sensorCombo.setItemLabelGenerator(SensorHumedad::getId);
        sensorCombo.setPlaceholder("Elegir sensor...");
        sensorCombo.setWidth("200px");

        ComboBox<Parcela> parcelaCombo = new ComboBox<>("Seleccionar Parcela");
        parcelaCombo.setItems(controller.obtenerParcelas());
        parcelaCombo.setItemLabelGenerator(Parcela::getId);
        parcelaCombo.setPlaceholder("Elegir parcela...");
        parcelaCombo.setWidth("200px");

        Button asignarButton = new Button("Asignar a Parcela", e -> {
            try {
                if (sensorCombo.getValue() != null && parcelaCombo.getValue() != null) {
                    controller.asignarSensorEspecificoAParcela(
                            sensorCombo.getValue().getId(),
                            parcelaCombo.getValue().getId()
                    );
                    mostrarNotificacion("Sensor asignado exitosamente", NotificationVariant.LUMO_SUCCESS);
                    sensorCombo.clear();
                    parcelaCombo.clear();
                    actualizarGridSensores();
                } else {
                    mostrarNotificacion("Debe seleccionar un sensor y una parcela", NotificationVariant.LUMO_ERROR);
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        asignarButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout formLayout1 = new HorizontalLayout(cantidadField, agregarButton);
        HorizontalLayout formLayout2 = new HorizontalLayout(sensorCombo, parcelaCombo, asignarButton);
        formLayout1.setAlignItems(Alignment.BASELINE);
        formLayout2.setAlignItems(Alignment.BASELINE);

        Grid<SensorHumedad> grid = new Grid<>(SensorHumedad.class, false);
        grid.addColumn(SensorHumedad::getId).setHeader("ID").setWidth("120px").setFlexGrow(0);
        grid.addColumn(s -> s.isConectado() ? "Conectado" : "Desconectado").setHeader("Estado").setWidth("120px").setFlexGrow(0);
        grid.addColumn(s -> s.getHumedadActual() + "%").setHeader("Humedad").setWidth("100px").setFlexGrow(0);
        grid.addColumn(s -> s.getParcela() != null ? s.getParcela().getId() : "Inventario").setHeader("Ubicación").setWidth("120px").setFlexGrow(0);
        grid.addColumn(s -> s.getLecturas().size()).setHeader("Lecturas").setWidth("100px").setFlexGrow(0);

        grid.addComponentColumn(sensor -> {
            VerticalLayout actions = new VerticalLayout();
            actions.setPadding(false);
            actions.setSpacing(true);

            HorizontalLayout fila1 = new HorizontalLayout();
            fila1.setSpacing(true);

            Button conectarBtn = new Button(sensor.isConectado() ? "Desconectar" : "Conectar", ev -> {
                try {
                    controller.conectarDesconectarSensor(sensor.getId());
                    mostrarNotificacion("Estado actualizado", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridSensores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            conectarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
            conectarBtn.setWidth("110px");

            Button lecturasBtn = new Button("📊 Lecturas", ev -> mostrarLecturasSensorPopup(sensor));
            lecturasBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_CONTRAST);
            lecturasBtn.setWidth("110px");

            fila1.add(conectarBtn, lecturasBtn);

            HorizontalLayout fila2 = new HorizontalLayout();
            fila2.setSpacing(true);

            Button eliminarBtn = new Button("🗑️ Eliminar", ev -> mostrarDialogoConfirmacion("¿Eliminar sensor " + sensor.getId() + "?", () -> {
                try {
                    controller.eliminarSensor(sensor.getId());
                    mostrarNotificacion("Sensor eliminado", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridSensores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            }));
            eliminarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
            eliminarBtn.setWidth("110px");

            fila2.add(eliminarBtn);

            actions.add(fila1, fila2);
            return actions;
        }).setHeader("Acciones").setAutoWidth(true);

        grid.setItems(controller.obtenerTodosSensores());
        grid.setHeight("500px");

        contentLayout.add(subtitle, formLayout1, formLayout2, grid);
    }

    // Método auxiliar para mostrar las lecturas del sensor en un pop-up
    private void mostrarLecturasSensorPopup(SensorHumedad sensor) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        dialog.setMaxHeight("80vh");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setPadding(true);
        contenido.setSpacing(true);

        com.vaadin.flow.component.html.H3 titulo = new com.vaadin.flow.component.html.H3("📊 Lecturas del Sensor " + sensor.getId());
        titulo.getStyle().set("margin", "0 0 20px 0");
        titulo.getStyle().set("color", "#1976d2");

        // Información general del sensor
        VerticalLayout infoGeneral = new VerticalLayout();
        infoGeneral.setPadding(true);
        infoGeneral.setSpacing(false);
        infoGeneral.getStyle().set("background-color", "#f5f5f5");
        infoGeneral.getStyle().set("border-radius", "5px");
        infoGeneral.getStyle().set("margin-bottom", "15px");

        com.vaadin.flow.component.html.Div infoEstado = new com.vaadin.flow.component.html.Div();
        infoEstado.setText("🔌 Estado: " + (sensor.isConectado() ? "Conectado" : "Desconectado"));
        infoEstado.getStyle().set("padding", "5px 0");

        com.vaadin.flow.component.html.Div infoHumedad = new com.vaadin.flow.component.html.Div();
        infoHumedad.setText("💧 Humedad Actual: " + sensor.getHumedadActual() + "%");
        infoHumedad.getStyle().set("padding", "5px 0");
        infoHumedad.getStyle().set("font-weight", "bold");
        infoHumedad.getStyle().set("color", "#2196f3");

        com.vaadin.flow.component.html.Div infoUbicacion = new com.vaadin.flow.component.html.Div();
        infoUbicacion.setText("📍 Ubicación: " + (sensor.getParcela() != null ? sensor.getParcela().getId() : "Inventario"));
        infoUbicacion.getStyle().set("padding", "5px 0");

        infoGeneral.add(infoEstado, infoHumedad, infoUbicacion);

        // Historial de lecturas
        com.vaadin.flow.component.html.H4 subtituloLecturas = new com.vaadin.flow.component.html.H4("📅 Historial de Lecturas");
        subtituloLecturas.getStyle().set("margin", "10px 0");

        VerticalLayout listaLecturas = new VerticalLayout();
        listaLecturas.setPadding(false);
        listaLecturas.setSpacing(true);
        listaLecturas.getStyle().set("max-height", "300px");
        listaLecturas.getStyle().set("overflow-y", "auto");

        if (sensor.getLecturas().isEmpty()) {
            com.vaadin.flow.component.html.Div mensajeVacio = new com.vaadin.flow.component.html.Div();
            mensajeVacio.setText("ℹ️ No hay lecturas registradas para este sensor");
            mensajeVacio.getStyle().set("padding", "20px");
            mensajeVacio.getStyle().set("text-align", "center");
            mensajeVacio.getStyle().set("color", "#757575");
            mensajeVacio.getStyle().set("background-color", "#f5f5f5");
            mensajeVacio.getStyle().set("border-radius", "5px");
            listaLecturas.add(mensajeVacio);
        } else {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            int contador = 1;

            // Invertir la lista para mostrar las más recientes primero
            java.util.List<LecturaHumedad> lecturasOrdenadas = new java.util.ArrayList<>(sensor.getLecturas());
            java.util.Collections.reverse(lecturasOrdenadas);

            // Variables para estadísticas
            int sumaHumedad = 0;
            int humedadMax = Integer.MIN_VALUE;
            int humedadMin = Integer.MAX_VALUE;

            for (LecturaHumedad lectura : lecturasOrdenadas) {
                com.vaadin.flow.component.html.Div itemLectura = new com.vaadin.flow.component.html.Div();

                // Determinar color según el valor de humedad
                String colorFondo = "#e3f2fd";
                String colorBorde = "#2196f3";

                if (lectura.getPorcentajeHumedad() < 30) {
                    colorFondo = "#ffebee";
                    colorBorde = "#f44336";
                } else if (lectura.getPorcentajeHumedad() > 70) {
                    colorFondo = "#e8f5e9";
                    colorBorde = "#4caf50";
                }

                itemLectura.setText("🕐 #" + contador + " - " + lectura.getFecha().format(formatter) +
                        " → Humedad: " + lectura.getPorcentajeHumedad() + "%");
                itemLectura.getStyle().set("padding", "10px");
                itemLectura.getStyle().set("background-color", colorFondo);
                itemLectura.getStyle().set("border-radius", "5px");
                itemLectura.getStyle().set("border-left", "4px solid " + colorBorde);
                itemLectura.getStyle().set("margin-bottom", "5px");
                itemLectura.getStyle().set("font-family", "monospace");
                listaLecturas.add(itemLectura);

                sumaHumedad += lectura.getPorcentajeHumedad();
                if (lectura.getPorcentajeHumedad() > humedadMax) humedadMax = lectura.getPorcentajeHumedad();
                if (lectura.getPorcentajeHumedad() < humedadMin) humedadMin = lectura.getPorcentajeHumedad();

                contador++;
            }

            VerticalLayout panelEstadisticas = new VerticalLayout();
            panelEstadisticas.setPadding(true);
            panelEstadisticas.setSpacing(false);
            panelEstadisticas.getStyle().set("background-color", "#fff3e0");
            panelEstadisticas.getStyle().set("border-radius", "5px");
            panelEstadisticas.getStyle().set("margin-top", "10px");
            panelEstadisticas.getStyle().set("border-left", "4px solid #ff9800");

            com.vaadin.flow.component.html.H5 tituloEstadisticas = new com.vaadin.flow.component.html.H5("📈 Estadísticas");
            tituloEstadisticas.getStyle().set("margin", "0 0 10px 0");
            tituloEstadisticas.getStyle().set("color", "#e65100");

            double promedio = (double) sumaHumedad / sensor.getLecturas().size();

            com.vaadin.flow.component.html.Div statTotal = new com.vaadin.flow.component.html.Div();
            statTotal.setText("📋 Total de lecturas: " + sensor.getLecturas().size());
            statTotal.getStyle().set("padding", "3px 0");

            com.vaadin.flow.component.html.Div statPromedio = new com.vaadin.flow.component.html.Div();
            statPromedio.setText(String.format("📊 Promedio: %.2f%%", promedio));
            statPromedio.getStyle().set("padding", "3px 0");

            com.vaadin.flow.component.html.Div statMax = new com.vaadin.flow.component.html.Div();
            statMax.setText("⬆️ Máxima: " + humedadMax + "%");
            statMax.getStyle().set("padding", "3px 0");
            statMax.getStyle().set("color", "#4caf50");

            com.vaadin.flow.component.html.Div statMin = new com.vaadin.flow.component.html.Div();
            statMin.setText("⬇️ Mínima: " + humedadMin + "%");
            statMin.getStyle().set("padding", "3px 0");
            statMin.getStyle().set("color", "#f44336");

            panelEstadisticas.add(tituloEstadisticas, statTotal, statPromedio, statMax, statMin);
            listaLecturas.add(panelEstadisticas);
        }

        Button cerrarBtn = new Button("Cerrar", e -> dialog.close());
        cerrarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cerrarBtn.getStyle().set("margin-top", "15px");

        contenido.add(titulo, infoGeneral, subtituloLecturas, listaLecturas, cerrarBtn);
        dialog.add(contenido);
        dialog.open();
    }


    private void mostrarVistaCultivos() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Cultivos");

        H3 seccionAgregar = new H3("Agregar Nuevo Cultivo");
        seccionAgregar.getStyle().set("margin-top", "0");

        TextField nombreCultivoField = new TextField("Nombre del Cultivo");
        nombreCultivoField.setPlaceholder("Ej: Arroz, Papa, etc.");
        nombreCultivoField.setRequired(true);
        nombreCultivoField.setWidth("200px");

        NumberField humedadMinField = new NumberField("Humedad Mínima (%)");
        humedadMinField.setPlaceholder("0-100");
        humedadMinField.setMin(0);
        humedadMinField.setMax(100);
        humedadMinField.setValue(40.0);
        humedadMinField.setWidth("180px");

        NumberField humedadMaxField = new NumberField("Humedad Máxima (%)");
        humedadMaxField.setPlaceholder("0-100");
        humedadMaxField.setMin(0);
        humedadMaxField.setMax(100);
        humedadMaxField.setValue(60.0);
        humedadMaxField.setWidth("180px");

        NumberField frecuenciaField = new NumberField("Frecuencia Riego (hrs)");
        frecuenciaField.setPlaceholder("1-999");
        frecuenciaField.setMin(1);
        frecuenciaField.setMax(999);
        frecuenciaField.setValue(48.0);
        frecuenciaField.setWidth("200px");

        Button agregarCultivoBtn = new Button("➕ Agregar Cultivo", e -> {
            boolean camposValidos = true;
            StringBuilder errores = new StringBuilder();

            if (nombreCultivoField.getValue() == null || nombreCultivoField.getValue().trim().isEmpty()) {
                errores.append("• El nombre del cultivo es obligatorio\n");
                nombreCultivoField.setInvalid(true);
                camposValidos = false;
            } else {
                nombreCultivoField.setInvalid(false);
            }

            if (humedadMinField.getValue() == null) {
                errores.append("• La humedad mínima es obligatoria\n");
                humedadMinField.setInvalid(true);
                camposValidos = false;
            } else if (humedadMinField.getValue() < 0 || humedadMinField.getValue() > 100) {
                errores.append("• La humedad mínima debe estar entre 0 y 100\n");
                humedadMinField.setInvalid(true);
                camposValidos = false;
            } else {
                humedadMinField.setInvalid(false);
            }

            if (humedadMaxField.getValue() == null) {
                errores.append("• La humedad máxima es obligatoria\n");
                humedadMaxField.setInvalid(true);
                camposValidos = false;
            } else if (humedadMaxField.getValue() < 0 || humedadMaxField.getValue() > 100) {
                errores.append("• La humedad máxima debe estar entre 0 y 100\n");
                humedadMaxField.setInvalid(true);
                camposValidos = false;
            } else {
                humedadMaxField.setInvalid(false);
            }

            if (humedadMinField.getValue() != null && humedadMaxField.getValue() != null) {
                if (humedadMinField.getValue() >= humedadMaxField.getValue()) {
                    errores.append("• La humedad mínima debe ser menor que la máxima\n");
                    humedadMinField.setInvalid(true);
                    humedadMaxField.setInvalid(true);
                    camposValidos = false;
                }
            }

            if (frecuenciaField.getValue() == null || frecuenciaField.getValue() <= 0) {
                errores.append("• La frecuencia de riego debe ser mayor a 0\n");
                frecuenciaField.setInvalid(true);
                camposValidos = false;
            } else {
                frecuenciaField.setInvalid(false);
            }

            if (!camposValidos) {
                mostrarNotificacion("Errores de validación:\n" + errores, NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                boolean exito = controller.agregarCultivo(
                        nombreCultivoField.getValue().trim(),
                        humedadMinField.getValue().intValue(),
                        humedadMaxField.getValue().intValue(),
                        frecuenciaField.getValue().intValue()
                );

                if (exito) {
                    mostrarNotificacion("Cultivo agregado exitosamente", NotificationVariant.LUMO_SUCCESS);
                    nombreCultivoField.clear();
                    humedadMinField.setValue(40.0);
                    humedadMaxField.setValue(60.0);
                    frecuenciaField.setValue(48.0);
                    nombreCultivoField.setInvalid(false);
                    humedadMinField.setInvalid(false);
                    humedadMaxField.setInvalid(false);
                    frecuenciaField.setInvalid(false);
                    mostrarVistaCultivos(); // Refrescar vista
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        agregarCultivoBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout formAgregarCultivo = new HorizontalLayout(
                nombreCultivoField, humedadMinField, humedadMaxField, frecuenciaField, agregarCultivoBtn
        );
        formAgregarCultivo.setAlignItems(Alignment.END);
        formAgregarCultivo.getStyle().set("background-color", "#e8f5e9");
        formAgregarCultivo.getStyle().set("padding", "15px");
        formAgregarCultivo.getStyle().set("border-radius", "5px");
        formAgregarCultivo.getStyle().set("margin-bottom", "20px");

        H3 seccionLista = new H3("Cultivos Disponibles");

        Grid<Cultivo> gridCultivos = new Grid<>(Cultivo.class, false);
        gridCultivos.addColumn(Cultivo::getNombre).setHeader("Nombre").setAutoWidth(true);
        gridCultivos.addColumn(Cultivo::getHumedadMinima).setHeader("Humedad Mín (%)").setWidth("150px").setFlexGrow(0);
        gridCultivos.addColumn(Cultivo::getHumedadMaxima).setHeader("Humedad Máx (%)").setWidth("150px").setFlexGrow(0);
        gridCultivos.addColumn(Cultivo::getFrecuenciaRiegoHoras).setHeader("Frecuencia (hrs)").setWidth("150px").setFlexGrow(0);
        gridCultivos.setItems(controller.obtenerCultivosDisponibles());
        gridCultivos.setHeight("300px");

        H3 seccionAsignar = new H3("Asignar/Cambiar Cultivo a Parcela");

        TextField parcelaField = new TextField("ID Parcela");
        parcelaField.setPlaceholder("Ej: PARCELA_1");
        parcelaField.setWidth("200px");

        ComboBox<Cultivo> cultivoCombo = new ComboBox<>("Cultivo");
        cultivoCombo.setItems(controller.obtenerCultivosDisponibles());
        cultivoCombo.setItemLabelGenerator(Cultivo::getNombre);
        cultivoCombo.setPlaceholder("Seleccione un cultivo");
        cultivoCombo.setWidth("200px");

        Button asignarButton = new Button("Asignar Cultivo", e -> {
            try {
                if (cultivoCombo.getValue() != null && !parcelaField.getValue().trim().isEmpty()) {
                    controller.registrarCultivoEnParcela(
                            parcelaField.getValue().trim(),
                            cultivoCombo.getValue().getNombre()
                    );
                    mostrarNotificacion("Cultivo asignado exitosamente", NotificationVariant.LUMO_SUCCESS);
                    parcelaField.clear();
                    cultivoCombo.clear();
                    actualizarGridParcelas();
                } else {
                    mostrarNotificacion("Debe ingresar ID de parcela y seleccionar cultivo", NotificationVariant.LUMO_ERROR);
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        asignarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cambiarButton = new Button("Cambiar Cultivo", e -> {
            try {
                if (cultivoCombo.getValue() != null && !parcelaField.getValue().trim().isEmpty()) {
                    controller.cambiarCultivoParcela(
                            parcelaField.getValue().trim(),
                            cultivoCombo.getValue().getNombre()
                    );
                    mostrarNotificacion("Cultivo cambiado exitosamente", NotificationVariant.LUMO_SUCCESS);
                    parcelaField.clear();
                    cultivoCombo.clear();
                    actualizarGridParcelas();
                } else {
                    mostrarNotificacion("Debe ingresar ID de parcela y seleccionar cultivo", NotificationVariant.LUMO_ERROR);
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        cambiarButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout formAsignarCultivo = new HorizontalLayout(
                parcelaField, cultivoCombo, asignarButton, cambiarButton
        );
        formAsignarCultivo.setAlignItems(Alignment.BASELINE);
        formAsignarCultivo.getStyle().set("background-color", "#e3f2fd");
        formAsignarCultivo.getStyle().set("padding", "15px");
        formAsignarCultivo.getStyle().set("border-radius", "5px");
        formAsignarCultivo.getStyle().set("margin-bottom", "20px");

        contentLayout.add(
                subtitle,
                seccionAgregar,
                formAgregarCultivo,
                seccionLista,
                gridCultivos,
                seccionAsignar,
                formAsignarCultivo
        );
    }

    private void mostrarVistaRiego() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Control de Riego Automático");

        Button simularButton = new Button("Simular Lecturas y Riego", e -> {
            controller.simularLecturasYRiego();
            mostrarNotificacion("Simulación completada", NotificationVariant.LUMO_SUCCESS);
            actualizarGridRiego();
        });
        simularButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);

        Grid<Parcela> grid = new Grid<>(Parcela.class, false);
        grid.addColumn(Parcela::getId).setHeader("Parcela");
        grid.addColumn(p -> p.getCultivo() != null ? p.getCultivo().getNombre() : "Sin cultivo").setHeader("Cultivo");
        grid.addColumn(p -> {
            if (!p.getSensores().isEmpty()) {
                return p.getSensores().get(0).getHumedadActual() + "%";
            }
            return "N/A";
        }).setHeader("Humedad Actual");
        grid.addColumn(p -> {
            if (p.getCultivo() != null) {
                return p.getCultivo().getHumedadMinima() + "% - " + p.getCultivo().getHumedadMaxima() + "%";
            }
            return "N/A";
        }).setHeader("Rango Ideal");
        grid.addColumn(p -> {
            long encendidos = p.getAspersores().stream().filter(Aspersor::isEncendido).count();
            return encendidos + " de " + p.getAspersores().size();
        }).setHeader("Aspersores Activos");

        grid.setItems(controller.obtenerParcelas());

        contentLayout.add(subtitle, simularButton, grid);
    }

    private void mostrarVistaArduino() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Dispositivos Arduino");

        H3 seccion1 = new H3("1. Escanear Puertos USB");
        Button escanearBtn = new Button("Escanear Puertos", e -> {
            java.util.List<String> puertos = controller.escanearArduinos();

            Dialog dialogPuertos = new Dialog();
            dialogPuertos.setWidth("500px");

            VerticalLayout contenido = new VerticalLayout();
            contenido.setPadding(true);
            contenido.setSpacing(true);

            com.vaadin.flow.component.html.H3 titulo = new com.vaadin.flow.component.html.H3("🔌 Puertos USB Detectados");
            titulo.getStyle().set("margin", "0");
            titulo.getStyle().set("color", "#1976d2");

            if (puertos.isEmpty()) {
                com.vaadin.flow.component.html.Div mensaje = new com.vaadin.flow.component.html.Div();
                mensaje.setText("❌ No se detectaron puertos USB");
                mensaje.getStyle().set("color", "#d32f2f");
                mensaje.getStyle().set("padding", "20px");
                mensaje.getStyle().set("text-align", "center");
                contenido.add(titulo, mensaje);
            } else {
                com.vaadin.flow.component.html.Div info = new com.vaadin.flow.component.html.Div();
                info.setText("✅ Total de puertos encontrados: " + puertos.size());
                info.getStyle().set("color", "#388e3c");
                info.getStyle().set("font-weight", "bold");
                info.getStyle().set("margin-bottom", "10px");

                VerticalLayout listaPuertos = new VerticalLayout();
                listaPuertos.setPadding(false);
                listaPuertos.setSpacing(false);
                listaPuertos.getStyle().set("background-color", "#f5f5f5");
                listaPuertos.getStyle().set("border-radius", "5px");
                listaPuertos.getStyle().set("padding", "10px");

                for (String puerto : puertos) {
                    com.vaadin.flow.component.html.Div itemPuerto = new com.vaadin.flow.component.html.Div();
                    itemPuerto.setText("📍 " + puerto);
                    itemPuerto.getStyle().set("padding", "8px");
                    itemPuerto.getStyle().set("border-bottom", "1px solid #e0e0e0");
                    itemPuerto.getStyle().set("font-family", "monospace");
                    listaPuertos.add(itemPuerto);
                }

                contenido.add(titulo, info, listaPuertos);
            }

            Button cerrarBtn = new Button("Cerrar", ev -> dialogPuertos.close());
            cerrarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            contenido.add(cerrarBtn);
            dialogPuertos.add(contenido);
            dialogPuertos.open();
        });
        escanearBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout seccionEscanear = new VerticalLayout(seccion1, escanearBtn);
        seccionEscanear.getStyle().set("background-color", "#f5f5f5");
        seccionEscanear.getStyle().set("padding", "15px");
        seccionEscanear.getStyle().set("border-radius", "5px");
        seccionEscanear.getStyle().set("margin-bottom", "20px");

        H3 seccion2 = new H3("2. Registrar Sensor Arduino");
        TextField puertoSensorField = new TextField("Puerto USB");
        puertoSensorField.setPlaceholder("Ej: COM3, /dev/ttyUSB0");
        puertoSensorField.setWidth("300px");

        Button registrarSensorBtn = new Button("Registrar Sensor", e -> {
            String puerto = puertoSensorField.getValue();
            if (puerto != null && !puerto.trim().isEmpty()) {

                Dialog dialogProgreso = new Dialog();
                dialogProgreso.setCloseOnOutsideClick(false);
                dialogProgreso.setCloseOnEsc(false);

                VerticalLayout contenidoProgreso = new VerticalLayout();
                contenidoProgreso.setPadding(true);
                contenidoProgreso.setSpacing(true);
                contenidoProgreso.setAlignItems(Alignment.CENTER);

                com.vaadin.flow.component.html.Span mensajeProgreso = new com.vaadin.flow.component.html.Span("🔄 Conectando al puerto " + puerto + "...");
                mensajeProgreso.getStyle().set("font-size", "16px");

                contenidoProgreso.add(mensajeProgreso);
                dialogProgreso.add(contenidoProgreso);
                dialogProgreso.open();

                new Thread(() -> {
                    boolean exito = controller.registrarSensorArduino(puerto.trim());

                    getUI().ifPresent(ui -> ui.access(() -> {
                        dialogProgreso.close();

                        if (exito) {
                            mostrarDialogoExito(
                                    "✅ Sensor Arduino Registrado",
                                    "El sensor se registró exitosamente en el puerto " + puerto,
                                    "ID: SENSOR_ARDUINO (ver consola para detalles)",
                                    "🔵 LED encendido - Sensor conectado"
                            );
                            puertoSensorField.clear();
                            actualizarGridSensores();
                        } else {
                            mostrarDialogoError(
                                    "❌ Error al Registrar Sensor",
                                    "No se pudo registrar el sensor Arduino",
                                    "• Verifique que el Arduino esté conectado al puerto " + puerto,
                                    "• Asegúrese de que el código correcto esté cargado en el Arduino",
                                    "• El puerto puede estar en uso por otra aplicación"
                            );
                        }
                    }));
                }).start();
            } else {
                mostrarNotificacion("Debe ingresar un puerto USB", NotificationVariant.LUMO_ERROR);
            }
        });
        registrarSensorBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout formSensor = new HorizontalLayout(puertoSensorField, registrarSensorBtn);
        formSensor.setAlignItems(Alignment.END);

        VerticalLayout seccionSensor = new VerticalLayout(seccion2, formSensor);
        seccionSensor.getStyle().set("background-color", "#e3f2fd");
        seccionSensor.getStyle().set("padding", "15px");
        seccionSensor.getStyle().set("border-radius", "5px");
        seccionSensor.getStyle().set("margin-bottom", "20px");

        H3 seccion3 = new H3("3. Registrar Aspersor Arduino");
        TextField puertoAspersorField = new TextField("Puerto USB");
        puertoAspersorField.setPlaceholder("Ej: COM3, /dev/ttyUSB0");
        puertoAspersorField.setWidth("300px");

        Button registrarAspersorBtn = new Button("Registrar Aspersor", e -> {
            String puerto = puertoAspersorField.getValue();
            if (puerto != null && !puerto.trim().isEmpty()) {

                Dialog dialogProgreso = new Dialog();
                dialogProgreso.setCloseOnOutsideClick(false);
                dialogProgreso.setCloseOnEsc(false);

                VerticalLayout contenidoProgreso = new VerticalLayout();
                contenidoProgreso.setPadding(true);
                contenidoProgreso.setSpacing(true);
                contenidoProgreso.setAlignItems(Alignment.CENTER);

                com.vaadin.flow.component.html.Span mensajeProgreso = new com.vaadin.flow.component.html.Span("🔄 Conectando al puerto " + puerto + "...");
                mensajeProgreso.getStyle().set("font-size", "16px");

                contenidoProgreso.add(mensajeProgreso);
                dialogProgreso.add(contenidoProgreso);
                dialogProgreso.open();

                new Thread(() -> {
                    boolean exito = controller.registrarAspersorArduino(puerto.trim());

                    getUI().ifPresent(ui -> ui.access(() -> {
                        dialogProgreso.close();

                        if (exito) {
                            mostrarDialogoExito(
                                    "✅ Aspersor Arduino Registrado",
                                    "El aspersor se registró exitosamente en el puerto " + puerto,
                                    "ID: ASPERSOR_ARDUINO (ver consola para detalles)",
                                    "🔵 LED encendido - Aspersor conectado"
                            );
                            puertoAspersorField.clear();
                            actualizarGridAspersores();
                        } else {
                            mostrarDialogoError(
                                    "❌ Error al Registrar Aspersor",
                                    "No se pudo registrar el aspersor Arduino",
                                    "• Verifique que el Arduino esté conectado al puerto " + puerto,
                                    "• Asegúrese de que el código correcto esté cargado en el Arduino",
                                    "• El puerto puede estar en uso por otra aplicación"
                            );
                        }
                    }));
                }).start();
            } else {
                mostrarNotificacion("Debe ingresar un puerto USB", NotificationVariant.LUMO_ERROR);
            }
        });
        registrarAspersorBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout formAspersor = new HorizontalLayout(puertoAspersorField, registrarAspersorBtn);
        formAspersor.setAlignItems(Alignment.END);

        VerticalLayout seccionAspersor = new VerticalLayout(seccion3, formAspersor);
        seccionAspersor.getStyle().set("background-color", "#f3e5f5");
        seccionAspersor.getStyle().set("padding", "15px");
        seccionAspersor.getStyle().set("border-radius", "5px");
        seccionAspersor.getStyle().set("margin-bottom", "20px");

        H3 seccion4 = new H3("4. Control de LED (Conexión)");

        TextField puertoLEDField = new TextField("Puerto USB");
        puertoLEDField.setPlaceholder("Ej: COM3");
        puertoLEDField.setWidth("200px");

        Button encenderLEDBtn = new Button("🔵 Encender LED", e -> {
            String puerto = puertoLEDField.getValue();
            if (puerto != null && !puerto.trim().isEmpty()) {

                Dialog dialogProgreso = new Dialog();
                dialogProgreso.setCloseOnOutsideClick(false);
                dialogProgreso.setCloseOnEsc(false);

                VerticalLayout contenidoProgreso = new VerticalLayout();
                contenidoProgreso.setPadding(true);
                contenidoProgreso.setSpacing(true);
                contenidoProgreso.setAlignItems(Alignment.CENTER);

                com.vaadin.flow.component.html.Span mensajeProgreso = new com.vaadin.flow.component.html.Span("🔄 Enviando comando CONNECT...");
                mensajeProgreso.getStyle().set("font-size", "16px");

                contenidoProgreso.add(mensajeProgreso);
                dialogProgreso.add(contenidoProgreso);
                dialogProgreso.open();

                new Thread(() -> {
                    boolean exito = controller.encenderLEDArduino(puerto.trim());

                    getUI().ifPresent(ui -> ui.access(() -> {
                        dialogProgreso.close();

                        if (exito) {
                            mostrarDialogoExito(
                                    "🔵 LED Encendido",
                                    "El LED del Arduino se encendió correctamente",
                                    "Puerto: " + puerto,
                                    "Comando: CONNECT enviado exitosamente",
                                    "💡 El LED del pin 13 ahora está ENCENDIDO"
                            );
                        } else {
                            mostrarDialogoError(
                                    "❌ Error al Encender LED",
                                    "No se pudo encender el LED",
                                    "• Verifique que el Arduino esté conectado al puerto " + puerto,
                                    "• Asegúrese de que el código esté cargado en el Arduino",
                                    "• Intente con el Monitor Serie: escriba 'CONNECT' y verifique si responde"
                            );
                        }
                    }));
                }).start();
            } else {
                mostrarNotificacion("Debe ingresar un puerto USB", NotificationVariant.LUMO_ERROR);
            }
        });
        encenderLEDBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button apagarLEDBtn = new Button("⚫ Apagar LED", e -> {
            String puerto = puertoLEDField.getValue();
            if (puerto != null && !puerto.trim().isEmpty()) {

                Dialog dialogProgreso = new Dialog();
                dialogProgreso.setCloseOnOutsideClick(false);
                dialogProgreso.setCloseOnEsc(false);

                VerticalLayout contenidoProgreso = new VerticalLayout();
                contenidoProgreso.setPadding(true);
                contenidoProgreso.setSpacing(true);
                contenidoProgreso.setAlignItems(Alignment.CENTER);

                com.vaadin.flow.component.html.Span mensajeProgreso = new com.vaadin.flow.component.html.Span("🔄 Enviando comando DISCONNECT...");
                mensajeProgreso.getStyle().set("font-size", "16px");

                contenidoProgreso.add(mensajeProgreso);
                dialogProgreso.add(contenidoProgreso);
                dialogProgreso.open();

                new Thread(() -> {
                    boolean exito = controller.apagarLEDArduino(puerto.trim());

                    getUI().ifPresent(ui -> ui.access(() -> {
                        dialogProgreso.close();

                        if (exito) {
                            mostrarDialogoExito(
                                    "⚫ LED Apagado",
                                    "El LED del Arduino se apagó correctamente",
                                    "Puerto: " + puerto,
                                    "Comando: DISCONNECT enviado exitosamente",
                                    "💡 El LED del pin 13 ahora está APAGADO"
                            );
                        } else {
                            mostrarDialogoError(
                                    "❌ Error al Apagar LED",
                                    "No se pudo apagar el LED",
                                    "• Verifique que el Arduino esté conectado al puerto " + puerto,
                                    "• El puerto puede haberse desconectado"
                            );
                        }
                    }));
                }).start();
            } else {
                mostrarNotificacion("Debe ingresar un puerto USB", NotificationVariant.LUMO_ERROR);
            }
        });
        apagarLEDBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout formLED = new HorizontalLayout(puertoLEDField, encenderLEDBtn, apagarLEDBtn);
        formLED.setAlignItems(Alignment.END);

        VerticalLayout seccionLED = new VerticalLayout(seccion4, formLED);
        seccionLED.getStyle().set("background-color", "#fff9c4");
        seccionLED.getStyle().set("padding", "15px");
        seccionLED.getStyle().set("border-radius", "5px");
        seccionLED.getStyle().set("margin-bottom", "20px");

        H3 seccion5 = new H3("5. Dispositivos Conectados");
        Button mostrarDispositivosBtn = new Button("Mostrar Dispositivos", e -> {
            java.util.List<String> dispositivos = controller.obtenerDispositivosArduino();

            Dialog dialogDispositivos = new Dialog();
            dialogDispositivos.setWidth("600px");

            VerticalLayout contenido = new VerticalLayout();
            contenido.setPadding(true);
            contenido.setSpacing(true);

            com.vaadin.flow.component.html.H3 titulo = new com.vaadin.flow.component.html.H3("🔌 Dispositivos Arduino Conectados");
            titulo.getStyle().set("margin", "0");
            titulo.getStyle().set("color", "#1976d2");

            if (dispositivos.isEmpty()) {
                com.vaadin.flow.component.html.Div mensaje = new com.vaadin.flow.component.html.Div();
                mensaje.setText("ℹ️ No hay dispositivos Arduino conectados actualmente");
                mensaje.getStyle().set("color", "#757575");
                mensaje.getStyle().set("padding", "30px");
                mensaje.getStyle().set("text-align", "center");
                mensaje.getStyle().set("background-color", "#f5f5f5");
                mensaje.getStyle().set("border-radius", "5px");
                contenido.add(titulo, mensaje);
            } else {
                com.vaadin.flow.component.html.Div info = new com.vaadin.flow.component.html.Div();
                info.setText("✅ Total de dispositivos conectados: " + dispositivos.size());
                info.getStyle().set("color", "#388e3c");
                info.getStyle().set("font-weight", "bold");
                info.getStyle().set("margin-bottom", "10px");

                VerticalLayout listaDispositivos = new VerticalLayout();
                listaDispositivos.setPadding(false);
                listaDispositivos.setSpacing(false);

                for (String dispositivo : dispositivos) {
                    com.vaadin.flow.component.html.Div itemDispositivo = new com.vaadin.flow.component.html.Div();
                    itemDispositivo.setText("🔹 " + dispositivo);
                    itemDispositivo.getStyle().set("padding", "12px");
                    itemDispositivo.getStyle().set("background-color", "#e3f2fd");
                    itemDispositivo.getStyle().set("border-radius", "5px");
                    itemDispositivo.getStyle().set("margin-bottom", "8px");
                    itemDispositivo.getStyle().set("font-family", "monospace");
                    itemDispositivo.getStyle().set("border-left", "4px solid #2196f3");
                    listaDispositivos.add(itemDispositivo);
                }

                contenido.add(titulo, info, listaDispositivos);
            }

            Button cerrarBtn = new Button("Cerrar", ev -> dialogDispositivos.close());
            cerrarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

            contenido.add(cerrarBtn);
            dialogDispositivos.add(contenido);
            dialogDispositivos.open();
        });
        mostrarDispositivosBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        Button desconectarTodosBtn = new Button("Desconectar Todos", e -> mostrarDialogoConfirmacion("¿Desconectar todos los dispositivos Arduino?", () -> {
            controller.desconectarTodosArduinos();
            mostrarDialogoExito(
                    "✅ Dispositivos Desconectados",
                    "Todos los dispositivos Arduino han sido desconectados",
                    "🔴 Todos los LEDs apagados",
                    "🔌 Todas las conexiones cerradas"
            );
        }));
        desconectarTodosBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout formDispositivos = new HorizontalLayout(mostrarDispositivosBtn, desconectarTodosBtn);

        VerticalLayout seccionDispositivos = new VerticalLayout(seccion5, formDispositivos);
        seccionDispositivos.getStyle().set("background-color", "#ffebee");
        seccionDispositivos.getStyle().set("padding", "15px");
        seccionDispositivos.getStyle().set("border-radius", "5px");

        contentLayout.add(subtitle, seccionEscanear, seccionSensor, seccionAspersor,
                seccionLED, seccionDispositivos);
    }


    private void mostrarDialogoExito(String titulo, String... mensajes) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setPadding(true);
        contenido.setSpacing(true);

        com.vaadin.flow.component.html.H3 tituloH3 = new com.vaadin.flow.component.html.H3(titulo);
        tituloH3.getStyle().set("margin", "0");
        tituloH3.getStyle().set("color", "#388e3c");

        contenido.add(tituloH3);

        for (String mensaje : mensajes) {
            com.vaadin.flow.component.html.Div div = new com.vaadin.flow.component.html.Div();
            div.setText(mensaje);
            div.getStyle().set("padding", "8px");
            div.getStyle().set("line-height", "1.6");
            contenido.add(div);
        }

        Button cerrarBtn = new Button("Aceptar", e -> dialog.close());
        cerrarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        cerrarBtn.getStyle().set("margin-top", "10px");

        contenido.add(cerrarBtn);
        dialog.add(contenido);
        dialog.open();
    }

    private void mostrarDialogoError(String titulo, String... mensajes) {
        Dialog dialog = new Dialog();
        dialog.setWidth("500px");

        VerticalLayout contenido = new VerticalLayout();
        contenido.setPadding(true);
        contenido.setSpacing(true);

        com.vaadin.flow.component.html.H3 tituloH3 = new com.vaadin.flow.component.html.H3(titulo);
        tituloH3.getStyle().set("margin", "0");
        tituloH3.getStyle().set("color", "#d32f2f");

        contenido.add(tituloH3);

        for (String mensaje : mensajes) {
            com.vaadin.flow.component.html.Div div = new com.vaadin.flow.component.html.Div();
            div.setText(mensaje);
            div.getStyle().set("padding", "8px");
            div.getStyle().set("line-height", "1.6");
            contenido.add(div);
        }

        Button cerrarBtn = new Button("Cerrar", e -> dialog.close());
        cerrarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        cerrarBtn.getStyle().set("margin-top", "10px");

        contenido.add(cerrarBtn);
        dialog.add(contenido);
        dialog.open();
    }

    private void actualizarGridParcelas() {
        mostrarVistaParcelas();
    }

    private void actualizarGridAspersores() {
        mostrarVistaAspersores();
    }

    private void actualizarGridSensores() {
        mostrarVistaSensores();
    }

    private void actualizarGridRiego() {
        mostrarVistaRiego();
    }

    private void actualizarVistaUsuarios() {
        mostrarVistaUsuarios();
    }

    private void mostrarNotificacion(String mensaje, NotificationVariant variant) {
        Notification notification = new Notification(mensaje, 3000);
        notification.addThemeVariants(variant);
        notification.open();
    }

    private void mostrarDialogoConfirmacion(String mensaje, Runnable onConfirm) {
        Dialog dialog = new Dialog();
        dialog.add(mensaje);

        Button confirmarBtn = new Button("Confirmar", e -> {
            onConfirm.run();
            dialog.close();
        });
        confirmarBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button cancelarBtn = new Button("Cancelar", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(confirmarBtn, cancelarBtn);
        dialog.add(buttons);
        dialog.open();
    }
}