package com.example.farmacia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
@ComponentScan(basePackages = "com.example.farmacia")
public class FarmaciaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmaciaApplication.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("¡Farmacia API iniciada exitosamente!");
        log.info("Swagger UI disponible en: http://localhost:8080/api/swagger-ui.html");
        log.info("H2 Console disponible en: http://localhost:8080/api/h2-console");
        log.info("Health Check disponible en: http://localhost:8080/api/health");
        log.info("API Base URL: http://localhost:8080/api");
        log.info("La aplicación está lista para recibir peticiones");
    }
}
