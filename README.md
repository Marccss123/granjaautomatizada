# GranjaADM (Sistema administrativo para gestionar riego para granjas)
[![Java-17](https://img.shields.io/badge/Java-17-red.svg?style=flat&logo=Java&logoColor=white)](https://www.azul.com/downloads/?version=java-21-lts&package=jdk#zulu)
[![Spring-Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green.svg?style=flat&logo=Springt&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven 3](https://img.shields.io/badge/Maven-3.x-blue.svg?style=flat&logo=Maven&logoColor=white)](https://maven.apache.org/download.cgi)

Portal Web para editar y administrar solicitudes la automatización de riego de cultivos para granjas.

# Como compilar

`mvn clean compile`

`mvn vaadin:prepare-frontend`

`mvn clean package -P production`

`mvn spring-boot:run`

# Modulos

- Usuarios
- Parcelas
- Aspersores
- Sensores
- Cultivos
- Riego automatico
