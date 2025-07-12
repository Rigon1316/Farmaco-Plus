package com.example.farmacia.controller;

import com.example.farmacia.service.MedicamentoService;
import com.example.farmacia.service.VentaService;
import com.example.farmacia.service.ClienteService;
import com.example.farmacia.service.AlertaService;
import com.example.farmacia.model.Alerta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {
    
    private final MedicamentoService medicamentoService;
    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final AlertaService alertaService;
    
    // GET /api/reportes/dashboard - Obtener datos del dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> obtenerDatosDashboard() {
        log.info("GET /api/reportes/dashboard - Obteniendo datos del dashboard");
        
        Map<String, Object> dashboard = new HashMap<>();
        
        // Estadísticas de medicamentos
        dashboard.put("medicamentosConStockBajo", medicamentoService.obtenerMedicamentosConStockBajo().size());
        dashboard.put("medicamentosAgotados", medicamentoService.obtenerMedicamentosAgotados().size());
        dashboard.put("medicamentosProximosACaducar", medicamentoService.obtenerMedicamentosProximosACaducar(30).size());
        dashboard.put("medicamentosCaducados", medicamentoService.obtenerMedicamentosCaducados().size());
        
        // Estadísticas de ventas
        dashboard.put("ventasDelDia", ventaService.obtenerVentasDelDia().size());
        dashboard.put("ventasDelMes", ventaService.obtenerVentasDelMes().size());
        
        // Calcular total de ventas del día
        LocalDate hoy = LocalDate.now();
        BigDecimal totalVentasDia = ventaService.calcularTotalVentasPorPeriodo(hoy, hoy);
        dashboard.put("totalVentasDia", totalVentasDia);
        
        // Estadísticas de alertas
        dashboard.put("alertasNoResueltas", alertaService.obtenerAlertasNoResueltas().size());
        dashboard.put("alertasCriticas", alertaService.obtenerAlertasCriticas().size());
        dashboard.put("alertasDelDia", alertaService.obtenerAlertasDelDia().size());
        
        // Top medicamentos más vendidos
        dashboard.put("medicamentosMasVendidos", medicamentoService.obtenerMedicamentosMasVendidos());
        
        // Top clientes más frecuentes
        dashboard.put("clientesMasFrecuentes", clienteService.obtenerClientesMasFrecuentes());
        
        return ResponseEntity.ok(dashboard);
    }
    
    // GET /api/reportes/ventas - Reporte de ventas
    @GetMapping("/ventas")
    public ResponseEntity<Map<String, Object>> obtenerReporteVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate fechaFin) {
        log.info("GET /api/reportes/ventas - Generando reporte de ventas");
        
        Map<String, Object> reporte = new HashMap<>();
        
        // Ventas del período
        reporte.put("ventas", ventaService.obtenerVentasPorRangoFechas(fechaInicio, fechaFin));
        reporte.put("totalVentas", ventaService.calcularTotalVentasPorPeriodo(fechaInicio, fechaFin));
        reporte.put("promedioVentasPorDia", ventaService.calcularPromedioVentasPorDia(fechaInicio, fechaFin));
        reporte.put("topVentas", ventaService.obtenerTopVentas());
        
        return ResponseEntity.ok(reporte);
    }
    
    // GET /api/reportes/inventario - Reporte de inventario
    @GetMapping("/inventario")
    public ResponseEntity<Map<String, Object>> obtenerReporteInventario() {
        log.info("GET /api/reportes/inventario - Generando reporte de inventario");
        
        Map<String, Object> reporte = new HashMap<>();
        
        // Estado del inventario
        reporte.put("medicamentosConStockBajo", medicamentoService.obtenerMedicamentosConStockBajo());
        reporte.put("medicamentosAgotados", medicamentoService.obtenerMedicamentosAgotados());
        reporte.put("medicamentosProximosACaducar", medicamentoService.obtenerMedicamentosProximosACaducar(30));
        reporte.put("medicamentosCaducados", medicamentoService.obtenerMedicamentosCaducados());
        reporte.put("medicamentosMasVendidos", medicamentoService.obtenerMedicamentosMasVendidos());
        
        return ResponseEntity.ok(reporte);
    }
    
    // GET /api/reportes/alertas - Reporte de alertas
    @GetMapping("/alertas")
    public ResponseEntity<Map<String, Object>> obtenerReporteAlertas() {
        log.info("GET /api/reportes/alertas - Generando reporte de alertas");
        
        Map<String, Object> reporte = new HashMap<>();
        
        // Estado de alertas
        reporte.put("alertasNoResueltas", alertaService.obtenerAlertasNoResueltas());
        reporte.put("alertasCriticas", alertaService.obtenerAlertasCriticas());
        reporte.put("alertasDelDia", alertaService.obtenerAlertasDelDia());
        reporte.put("alertasStockBajo", alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.STOCK_BAJO));
        reporte.put("alertasCaducidad", alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.FECHA_CADUCIDAD));
        reporte.put("alertasStockAgotado", alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.STOCK_AGOTADO));
        reporte.put("alertasCaducados", alertaService.obtenerAlertasPorTipo(Alerta.TipoAlerta.MEDICAMENTO_CADUCADO));
        
        return ResponseEntity.ok(reporte);
    }
    
    // GET /api/reportes/clientes - Reporte de clientes
    @GetMapping("/clientes")
    public ResponseEntity<Map<String, Object>> obtenerReporteClientes() {
        log.info("GET /api/reportes/clientes - Generando reporte de clientes");
        
        Map<String, Object> reporte = new HashMap<>();
        
        // Estadísticas de clientes
        reporte.put("clientesActivos", clienteService.obtenerClientesActivos());
        reporte.put("clientesMasFrecuentes", clienteService.obtenerClientesMasFrecuentes());
        reporte.put("clientesConMayorGasto", clienteService.obtenerClientesConMayorGasto());
        
        return ResponseEntity.ok(reporte);
    }
    
    // GET /api/reportes/estadisticas - Estadísticas generales
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticasGenerales() {
        log.info("GET /api/reportes/estadisticas - Obteniendo estadísticas generales");
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Estadísticas de medicamentos
        estadisticas.put("totalMedicamentos", medicamentoService.obtenerTodosLosMedicamentos().size());
        estadisticas.put("medicamentosActivos", medicamentoService.obtenerTodosLosMedicamentos().stream()
                .filter(m -> m.getEstado() == com.example.farmacia.model.Medicamento.EstadoMedicamento.ACTIVO)
                .count());
        estadisticas.put("medicamentosConStockBajo", medicamentoService.obtenerMedicamentosConStockBajo().size());
        estadisticas.put("medicamentosAgotados", medicamentoService.obtenerMedicamentosAgotados().size());
        
        // Estadísticas de clientes
        estadisticas.put("totalClientes", clienteService.obtenerTodosLosClientes().size());
        estadisticas.put("clientesActivos", clienteService.obtenerClientesActivos().size());
        
        // Estadísticas de ventas
        estadisticas.put("totalVentas", ventaService.obtenerTodasLasVentas().size());
        estadisticas.put("ventasDelDia", ventaService.obtenerVentasDelDia().size());
        estadisticas.put("ventasDelMes", ventaService.obtenerVentasDelMes().size());
        
        // Estadísticas de alertas
        estadisticas.put("totalAlertas", alertaService.obtenerTodasLasAlertas().size());
        estadisticas.put("alertasNoResueltas", alertaService.obtenerAlertasNoResueltas().size());
        estadisticas.put("alertasCriticas", alertaService.obtenerAlertasCriticas().size());
        
        return ResponseEntity.ok(estadisticas);
    }
    
    // GET /api/reportes/rendimiento - Reporte de rendimiento
    @GetMapping("/rendimiento")
    public ResponseEntity<Map<String, Object>> obtenerReporteRendimiento(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        log.info("GET /api/reportes/rendimiento - Generando reporte de rendimiento");
        
        Map<String, Object> reporte = new HashMap<>();
        
        // Métricas de rendimiento
        reporte.put("totalVentas", ventaService.calcularTotalVentasPorPeriodo(fechaInicio, fechaFin));
        reporte.put("promedioVentasPorDia", ventaService.calcularPromedioVentasPorDia(fechaInicio, fechaFin));
        reporte.put("topVentas", ventaService.obtenerTopVentas());
        reporte.put("medicamentosMasVendidos", medicamentoService.obtenerMedicamentosMasVendidos());
        reporte.put("clientesMasFrecuentes", clienteService.obtenerClientesMasFrecuentes());
        reporte.put("clientesConMayorGasto", clienteService.obtenerClientesConMayorGasto());
        
        return ResponseEntity.ok(reporte);
    }
} 