package com.granja;

import com.granja.servicio.PersistenciaService;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Bean
    public CommandLineRunner inicializarDatos(PersistenciaService persistenciaService) {
        return args -> {
            persistenciaService.inicializarCultivos();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
