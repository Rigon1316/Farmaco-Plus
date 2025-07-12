package com.example.farmacia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Health", description = "Endpoints para verificar el estado de la aplicación")
public class HealthController {
    
    @GetMapping
    @Operation(summary = "Verificar estado de la aplicación", 
               description = "Retorna el estado actual de la aplicación con información del sistema")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("GET /api/health - Verificando estado de la aplicación");
        
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("status", "UP");
        healthStatus.put("message", "¡Farmacia API está funcionando correctamente! 🎉");
        healthStatus.put("timestamp", LocalDateTime.now());
        healthStatus.put("version", "1.0.0");
        healthStatus.put("environment", "Development");
        
        // Información del sistema
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("javaVersion", System.getProperty("java.version"));
        systemInfo.put("osName", System.getProperty("os.name"));
        systemInfo.put("osVersion", System.getProperty("os.version"));
        systemInfo.put("userTimezone", System.getProperty("user.timezone"));
        
        healthStatus.put("system", systemInfo);
        
        // Endpoints disponibles
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("swagger", "/api/swagger-ui.html");
        endpoints.put("h2Console", "/api/h2-console");
        endpoints.put("medicamentos", "/api/medicamentos");
        endpoints.put("clientes", "/api/clientes");
        endpoints.put("ventas", "/api/ventas");
        endpoints.put("alertas", "/api/alertas");
        endpoints.put("reportes", "/api/reportes");
        endpoints.put("nlq", "/api/nlq");
        
        healthStatus.put("availableEndpoints", endpoints);
        
        return ResponseEntity.ok(healthStatus);
    }
    
    @GetMapping("/ping")
    @Operation(summary = "Ping simple", description = "Endpoint simple para verificar conectividad")
    public ResponseEntity<Map<String, String>> ping() {
        log.info("GET /api/health/ping - Ping recibido");
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "pong");
        response.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(response);
    }
} 