package com.example.farmacia.controller;

import com.example.farmacia.model.Venta;
import com.example.farmacia.service.VentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
@Slf4j
public class VentaController {
    
    private final VentaService ventaService;
    
    // GET /api/ventas - Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        log.info("GET /api/ventas - Obteniendo todas las ventas");
        List<Venta> ventas = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/{id} - Obtener venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerVentaPorId(@PathVariable Long id) {
        log.info("GET /api/ventas/{} - Obteniendo venta por ID", id);
        Optional<Venta> venta = ventaService.obtenerVentaPorId(id);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/ventas/factura/{numeroFactura} - Obtener venta por número de factura
    @GetMapping("/factura/{numeroFactura}")
    public ResponseEntity<Venta> obtenerVentaPorNumeroFactura(@PathVariable String numeroFactura) {
        log.info("GET /api/ventas/factura/{} - Obteniendo venta por número de factura", numeroFactura);
        Optional<Venta> venta = ventaService.obtenerVentaPorNumeroFactura(numeroFactura);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/ventas - Crear nueva venta
    @PostMapping
    public ResponseEntity<?> crearVenta(@Valid @RequestBody Venta venta) {
        log.info("POST /api/ventas - Creando nueva venta: {}", venta.getNumeroFactura());
        try {
            Venta ventaCreada = ventaService.crearVenta(venta);
            return ResponseEntity.status(HttpStatus.CREATED).body(ventaCreada);
        } catch (RuntimeException e) {
            log.error("Error al crear venta: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
    
    // PUT /api/ventas/{id}/estado - Actualizar estado de venta
    @PutMapping("/{id}/estado")
    public ResponseEntity<Venta> actualizarEstadoVenta(@PathVariable Long id, 
                                                         @RequestParam Venta.EstadoVenta estado) {
        log.info("PUT /api/ventas/{}/estado?estado={} - Actualizando estado de venta", id, estado);
        try {
            Optional<Venta> ventaActualizada = ventaService.actualizarEstadoVenta(id, estado);
            return ventaActualizada.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            log.error("Error al actualizar estado de venta: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // GET /api/ventas/cliente/{clienteId} - Obtener ventas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Venta>> obtenerVentasPorCliente(@PathVariable Long clienteId) {
        log.info("GET /api/ventas/cliente/{} - Obteniendo ventas por cliente", clienteId);
        List<Venta> ventas = ventaService.obtenerVentasPorCliente(clienteId);
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/estado/{estado} - Obtener ventas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Venta>> obtenerVentasPorEstado(@PathVariable Venta.EstadoVenta estado) {
        log.info("GET /api/ventas/estado/{} - Obteniendo ventas por estado", estado);
        List<Venta> ventas = ventaService.obtenerVentasPorEstado(estado);
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/metodo-pago/{metodoPago} - Obtener ventas por método de pago
    @GetMapping("/metodo-pago/{metodoPago}")
    public ResponseEntity<List<Venta>> obtenerVentasPorMetodoPago(@PathVariable Venta.MetodoPago metodoPago) {
        log.info("GET /api/ventas/metodo-pago/{} - Obteniendo ventas por método de pago", metodoPago);
        List<Venta> ventas = ventaService.obtenerVentasPorMetodoPago(metodoPago);
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/rango-fechas - Obtener ventas por rango de fechas
    @GetMapping("/rango-fechas")
    public ResponseEntity<List<Venta>> obtenerVentasPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("GET /api/ventas/rango-fechas - Obteniendo ventas por rango de fechas");
        List<Venta> ventas = ventaService.obtenerVentasPorRangoFechas(fechaInicio, fechaFin);
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/dia - Obtener ventas del día
    @GetMapping("/dia")
    public ResponseEntity<List<Venta>> obtenerVentasDelDia() {
        log.info("GET /api/ventas/dia - Obteniendo ventas del día");
        List<Venta> ventas = ventaService.obtenerVentasDelDia();
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/mes - Obtener ventas del mes
    @GetMapping("/mes")
    public ResponseEntity<List<Venta>> obtenerVentasDelMes() {
        log.info("GET /api/ventas/mes - Obteniendo ventas del mes");
        List<Venta> ventas = ventaService.obtenerVentasDelMes();
        return ResponseEntity.ok(ventas);
    }
    
    // GET /api/ventas/total - Calcular total de ventas por período
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> calcularTotalVentasPorPeriodo(
            @RequestParam LocalDateTime fechaInicio,
            @RequestParam LocalDateTime fechaFin) {
        log.info("GET /api/ventas/total - Calculando total de ventas del {} al {}", fechaInicio, fechaFin);
        BigDecimal total = ventaService.calcularTotalVentasPorPeriodo(fechaInicio, fechaFin);
        return ResponseEntity.ok(total);
    }
    
    // GET /api/ventas/promedio-dia - Obtener promedio de ventas por día
    @GetMapping("/promedio-dia")
    public ResponseEntity<BigDecimal> calcularPromedioVentasPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("GET /api/ventas/promedio-dia - Calculando promedio de ventas por día");
        BigDecimal promedio = ventaService.calcularPromedioVentasPorDia(fechaInicio, fechaFin);
        return ResponseEntity.ok(promedio);
    }
    
    // GET /api/ventas/top - Obtener top ventas
    @GetMapping("/top")
    public ResponseEntity<List<Venta>> obtenerTopVentas() {
        log.info("GET /api/ventas/top - Obteniendo top ventas");
        List<Venta> ventas = ventaService.obtenerTopVentas();
        return ResponseEntity.ok(ventas);
    }
    
    // PUT /api/ventas/{id}/cancelar - Cancelar venta
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Venta> cancelarVenta(@PathVariable Long id) {
        log.info("PUT /api/ventas/{}/cancelar - Cancelando venta", id);
        Optional<Venta> venta = ventaService.actualizarEstadoVenta(id, Venta.EstadoVenta.CANCELADA);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // PUT /api/ventas/{id}/devolver - Devolver venta
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Venta> devolverVenta(@PathVariable Long id) {
        log.info("PUT /api/ventas/{}/devolver - Devolviendo venta", id);
        Optional<Venta> venta = ventaService.actualizarEstadoVenta(id, Venta.EstadoVenta.DEVUELTA);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // PUT /api/ventas/{id}/pagar - Marcar venta como pagada
    @PutMapping("/{id}/pagar")
    public ResponseEntity<Venta> pagarVenta(@PathVariable Long id) {
        log.info("PUT /api/ventas/{}/pagar - Marcando venta como pagada", id);
        Optional<Venta> venta = ventaService.actualizarEstadoVenta(id, Venta.EstadoVenta.PAGADA);
        return venta.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 