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

        Tabs tabs = new Tabs(usuariosTab, parcelasTab, aspersoresTab, sensoresTab, cultivosTab, riegoTab);

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
            // Validar todos los campos antes de agregar
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

            // Si todo es válido, agregar usuario
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
        grid.addColumn(Usuario::getId).setHeader("ID");
        grid.addColumn(Usuario::getNombreCompleto).setHeader("Nombre");
        grid.addColumn(Usuario::getEmail).setHeader("Email");
        grid.addColumn(Usuario::getTelefono).setHeader("Teléfono");
        grid.addColumn(Usuario::getRol).setHeader("Rol");
        grid.addColumn(u -> u.isActivo() ? "Activo" : "Inactivo").setHeader("Estado");

        grid.addComponentColumn(usuario -> {
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
            return seleccionarBtn;
        }).setHeader("Acción");

        grid.setItems(controller.obtenerUsuarios());

        contentLayout.add(subtitle, formLayout, grid);
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
        grid.addColumn(Aspersor::getId).setHeader("ID");
        grid.addColumn(a -> a.isConectado() ? "Conectado" : "Desconectado").setHeader("Estado");
        grid.addColumn(a -> a.isEncendido() ? "Encendido" : "Apagado").setHeader("Riego");
        grid.addColumn(a -> a.getParcela() != null ? a.getParcela().getId() : "Inventario").setHeader("Ubicación");

        grid.addComponentColumn(aspersor -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button conectarBtn = new Button(aspersor.isConectado() ? "Desconectar" : "Conectar", ev -> {
                try {
                    controller.conectarDesconectarAspersor(aspersor.getId());
                    actualizarGridAspersores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            conectarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Button encenderBtn = new Button("Encender Manual", ev -> {
                try {
                    controller.prenderAspersorManualmente(aspersor.getId());
                    mostrarNotificacion("Aspersor encendido", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridAspersores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            encenderBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_SUCCESS);

            Button eliminarBtn = new Button("Eliminar", ev -> mostrarDialogoConfirmacion("¿Eliminar aspersor " + aspersor.getId() + "?", () -> {
                try {
                    controller.eliminarAspersor(aspersor.getId());
                    actualizarGridAspersores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            }));
            eliminarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

            actions.add(conectarBtn, encenderBtn, eliminarBtn);
            return actions;
        }).setHeader("Acciones");

        grid.setItems(controller.obtenerTodosAspersores());

        contentLayout.add(subtitle, formLayout1, formLayout2, grid);
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
        grid.addColumn(SensorHumedad::getId).setHeader("ID");
        grid.addColumn(s -> s.isConectado() ? "Conectado" : "Desconectado").setHeader("Estado");
        grid.addColumn(s -> s.getHumedadActual() + "%").setHeader("Humedad");
        grid.addColumn(s -> s.getParcela() != null ? s.getParcela().getId() : "Inventario").setHeader("Ubicación");
        grid.addColumn(s -> s.getLecturas().size()).setHeader("Lecturas");

        grid.addComponentColumn(sensor -> {
            HorizontalLayout actions = new HorizontalLayout();

            Button conectarBtn = new Button(sensor.isConectado() ? "Desconectar" : "Conectar", ev -> {
                try {
                    controller.conectarDesconectarSensor(sensor.getId());
                    actualizarGridSensores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            });
            conectarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);

            Button eliminarBtn = new Button("Eliminar", ev -> mostrarDialogoConfirmacion("¿Eliminar sensor " + sensor.getId() + "?", () -> {
                try {
                    controller.eliminarSensor(sensor.getId());
                    actualizarGridSensores();
                } catch (GranjaException ex) {
                    mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                }
            }));
            eliminarBtn.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);

            actions.add(conectarBtn, eliminarBtn);
            return actions;
        }).setHeader("Acciones");

        grid.setItems(controller.obtenerTodosSensores());

        contentLayout.add(subtitle, formLayout1, formLayout2, grid);
    }

    private void mostrarVistaCultivos() {
        contentLayout.removeAll();

        H2 subtitle = new H2("Gestión de Cultivos");

        Grid<Cultivo> gridCultivos = new Grid<>(Cultivo.class, false);
        gridCultivos.addColumn(Cultivo::getNombre).setHeader("Nombre");
        gridCultivos.addColumn(Cultivo::getHumedadMinima).setHeader("Humedad Mín (%)");
        gridCultivos.addColumn(Cultivo::getHumedadMaxima).setHeader("Humedad Máx (%)");
        gridCultivos.addColumn(Cultivo::getFrecuenciaRiegoHoras).setHeader("Frecuencia (hrs)");
        gridCultivos.setItems(controller.obtenerCultivosDisponibles());

        TextField parcelaField = new TextField("ID Parcela");

        ComboBox<Cultivo> cultivoCombo = new ComboBox<>("Cultivo");
        cultivoCombo.setItems(controller.obtenerCultivosDisponibles());
        cultivoCombo.setItemLabelGenerator(Cultivo::getNombre);

        Button asignarButton = new Button("Asignar Cultivo a Parcela", e -> {
            try {
                if (cultivoCombo.getValue() != null) {
                    controller.registrarCultivoEnParcela(parcelaField.getValue(), cultivoCombo.getValue().getNombre());
                    mostrarNotificacion("Cultivo asignado", NotificationVariant.LUMO_SUCCESS);
                    actualizarGridParcelas();
                }
            } catch (GranjaException ex) {
                mostrarNotificacion("Error: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
            }
        });
        asignarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout formLayout = new HorizontalLayout(parcelaField, cultivoCombo, asignarButton);
        formLayout.setAlignItems(Alignment.BASELINE);

        contentLayout.add(subtitle, new H2("Cultivos Disponibles"), gridCultivos,
                new H2("Asignar Cultivo"), formLayout);
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