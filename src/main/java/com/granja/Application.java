package com.granja;

import com.granja.servicio.OperacionesCrud;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    @Bean
    public CommandLineRunner inicializarDatos(OperacionesCrud operacionesCrud) {
        return args -> {
            operacionesCrud.inicializarCultivos();
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
