package com.example.farmacia.controller;

import com.example.farmacia.model.Alerta;
import com.example.farmacia.service.AlertaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alertas")
@RequiredArgsConstructor
@Slf4j
public class AlertaController {
    
    private final AlertaService alertaService;
    
    // GET /api/alertas - Obtener todas las alertas
    @GetMapping
    public ResponseEntity<List<Alerta>> obtenerTodasLasAlertas() {
        log.info("GET /api/alertas - Obteniendo todas las alertas");
        List<Alerta> alertas = alertaService.obtenerTodasLasAlertas();
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/{id} - Obtener alerta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Alerta> obtenerAlertaPorId(@PathVariable Long id) {
        log.info("GET /api/alertas/{} - Obteniendo alerta por ID", id);
        Optional<Alerta> alerta = alertaService.obtenerAlertaPorId(id);
        return alerta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/alertas/{id} - Eliminar alerta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlerta(@PathVariable Long id) {
        log.info("DELETE /api/alertas/{} - Eliminando alerta por ID", id);
        boolean eliminada = alertaService.eliminarAlerta(id);
        if (eliminada) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // POST /api/alertas - Crear nueva alerta
    @PostMapping
    public ResponseEntity<Alerta> crearAlerta(@Valid @RequestBody Alerta alerta) {
        log.info("POST /api/alertas - Creando nueva alerta: {}", alerta.getTitulo());
        try {
            Alerta alertaCreada = alertaService.crearAlerta(alerta);
            return ResponseEntity.status(HttpStatus.CREATED).body(alertaCreada);
        } catch (RuntimeException e) {
            log.error("Error al crear alerta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/alertas/{id}/estado - Actualizar estado de alerta
    @PutMapping("/{id}/estado")
    public ResponseEntity<Alerta> actualizarEstadoAlerta(@PathVariable Long id, 
                                                           @RequestParam Alerta.EstadoAlerta estado) {
        log.info("PUT /api/alertas/{}/estado?estado={} - Actualizando estado de alerta", id, estado);
        try {
            Optional<Alerta> alertaActualizada = alertaService.actualizarEstadoAlerta(id, estado);
            return alertaActualizada.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            log.error("Error al actualizar estado de alerta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET /api/alertas/tipo/{tipo} - Obtener alertas por tipo
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Alerta>> obtenerAlertasPorTipo(@PathVariable Alerta.TipoAlerta tipo) {
        log.info("GET /api/alertas/tipo/{} - Obteniendo alertas por tipo", tipo);
        List<Alerta> alertas = alertaService.obtenerAlertasPorTipo(tipo);
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/nivel/{nivel} - Obtener alertas por nivel
    @GetMapping("/nivel/{nivel}")
    public ResponseEntity<List<Alerta>> obtenerAlertasPorNivel(@PathVariable Alerta.NivelAlerta nivel) {
        log.info("GET /api/alertas/nivel/{} - Obteniendo alertas por nivel", nivel);
        List<Alerta> alertas = alertaService.obtenerAlertasPorNivel(nivel);
        return ResponseEntity.ok(alertas);
    }

    
    
    // GET /api/alertas/estado/{estado} - Obtener alertas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Alerta>> obtenerAlertasPorEstado(@PathVariable Alerta.EstadoAlerta estado) {
        log.info("GET /api/alertas/estado/{} - Obteniendo alertas por estado", estado);
        List<Alerta> alertas = alertaService.obtenerAlertasPorEstado(estado);
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/no-resueltas - Obtener alertas no resueltas
    @GetMapping("/no-resueltas")
    public ResponseEntity<List<Alerta>> obtenerAlertasNoResueltas() {
        log.info("GET /api/alertas/no-resueltas - Obteniendo alertas no resueltas");
        List<Alerta> alertas = alertaService.obtenerAlertasNoResueltas();
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/dia - Obtener alertas del día
    @GetMapping("/dia")
    public ResponseEntity<List<Alerta>> obtenerAlertasDelDia() {
        log.info("GET /api/alertas/dia - Obteniendo alertas del día");
        List<Alerta> alertas = alertaService.obtenerAlertasDelDia();
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/criticas - Obtener alertas críticas
    @GetMapping("/criticas")
    public ResponseEntity<List<Alerta>> obtenerAlertasCriticas() {
        log.info("GET /api/alertas/criticas - Obteniendo alertas críticas");
        List<Alerta> alertas = alertaService.obtenerAlertasCriticas();
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/medicamento/{medicamentoId} - Obtener alertas por medicamento
    @GetMapping("/medicamento/{medicamentoId}")
    public ResponseEntity<List<Alerta>> obtenerAlertasPorMedicamento(@PathVariable Long medicamentoId) {
        log.info("GET /api/alertas/medicamento/{} - Obteniendo alertas por medicamento", medicamentoId);
        List<Alerta> alertas = alertaService.obtenerAlertasPorMedicamento(medicamentoId);
        return ResponseEntity.ok(alertas);
    }
    

    
    // GET /api/alertas/stock-bajo - Obtener alertas de stock bajo
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Alerta>> obtenerAlertasStockBajo() {
        log.info("GET /api/alertas/stock-bajo - Obteniendo alertas de stock bajo");
        List<Alerta> alertas = alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.STOCK_BAJO);
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/caducidad - Obtener alertas de caducidad
    @GetMapping("/caducidad")
    public ResponseEntity<List<Alerta>> obtenerAlertasCaducidad() {
        log.info("GET /api/alertas/caducidad - Obteniendo alertas de caducidad");
        List<Alerta> alertas = alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.FECHA_CADUCIDAD);
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/stock-agotado - Obtener alertas de stock agotado
    @GetMapping("/stock-agotado")
    public ResponseEntity<List<Alerta>> obtenerAlertasStockAgotado() {
        log.info("GET /api/alertas/stock-agotado - Obteniendo alertas de stock agotado");
        List<Alerta> alertas = alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.STOCK_AGOTADO);
        return ResponseEntity.ok(alertas);
    }
    
    // GET /api/alertas/caducados - Obtener alertas de medicamentos caducados
    @GetMapping("/caducados")
    public ResponseEntity<List<Alerta>> obtenerAlertasCaducados() {
        log.info("GET /api/alertas/caducados - Obteniendo alertas de medicamentos caducados");
        List<Alerta> alertas = alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.MEDICAMENTO_CADUCADO);
        return ResponseEntity.ok(alertas);
    }
} 