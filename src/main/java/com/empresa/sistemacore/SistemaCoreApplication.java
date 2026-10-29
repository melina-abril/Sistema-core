package com.empresa.sistemacore;

import java.time.Clock;
import java.time.ZoneId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SistemaCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaCoreApplication.class, args);
    }

    @Bean
    public Clock clock(@Value("${app.zone-id:UTC}") String zoneId) {
        return Clock.system(ZoneId.of(zoneId));
    }
}
