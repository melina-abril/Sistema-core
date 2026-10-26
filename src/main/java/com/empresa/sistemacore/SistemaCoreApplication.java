package com.empresa.sistemacore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Los controladores de todos los módulos se registran mediante component scanning. */
@SpringBootApplication
public class SistemaCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(SistemaCoreApplication.class, args);
    }
}
