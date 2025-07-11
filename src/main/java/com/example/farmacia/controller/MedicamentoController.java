package com.example.farmacia.controller;

import com.example.farmacia.model.Medicamento;
import com.example.farmacia.service.MedicamentoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Medicamentos", description = "Gestión de medicamentos en la farmacia")
public class MedicamentoController {
    
    private final MedicamentoService medicamentoService;
    
    // GET /api/medicamentos - Obtener todos los medicamentos
    @GetMapping
    public ResponseEntity<List<Medicamento>> obtenerTodosLosMedicamentos() {
        log.info("GET /api/medicamentos - Obteniendo todos los medicamentos");
        List<Medicamento> medicamentos = medicamentoService.obtenerTodosLosMedicamentos();
        return ResponseEntity.ok(medicamentos);
    }
    
    // GET /api/medicamentos/{id} - Obtener medicamento por ID
    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> obtenerMedicamentoPorId(@PathVariable Long id) {
        log.info("GET /api/medicamentos/{} - Obteniendo medicamento por ID", id);
        Optional<Medicamento> medicamento = medicamentoService.obtenerMedicamentoPorId(id);
        return medicamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/medicamentos/codigo/{codigoBarras} - Obtener medicamento por código de barras
    @GetMapping("/codigo/{codigoBarras}")
    public ResponseEntity<Medicamento> obtenerMedicamentoPorCodigoBarras(@PathVariable String codigoBarras) {
        log.info("GET /api/medicamentos/codigo/{} - Obteniendo medicamento por código de barras", codigoBarras);
        Optional<Medicamento> medicamento = medicamentoService.obtenerMedicamentoPorCodigoBarras(codigoBarras);
        return medicamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/medicamentos - Crear nuevo medicamento
    @PostMapping
    public ResponseEntity<Medicamento> crearMedicamento(@Valid @RequestBody Medicamento medicamento) {
        log.info("POST /api/medicamentos - Creando nuevo medicamento: {}", medicamento.getNombre());
        try {
            Medicamento medicamentoCreado = medicamentoService.crearMedicamento(medicamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(medicamentoCreado);
        } catch (RuntimeException e) {
            log.error("Error al crear medicamento: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/medicamentos/{id} - Actualizar medicamento
    @PutMapping("/{id}")
    public ResponseEntity<Medicamento> actualizarMedicamento(@PathVariable Long id, 
                                                               @Valid @RequestBody Medicamento medicamento) {
        log.info("PUT /api/medicamentos/{} - Actualizando medicamento", id);
        try {
            Optional<Medicamento> medicamentoActualizado = medicamentoService.actualizarMedicamento(id, medicamento);
            return medicamentoActualizado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            log.error("Error al actualizar medicamento: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE /api/medicamentos/{id} - Eliminar medicamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMedicamento(@PathVariable Long id) {
        log.info("DELETE /api/medicamentos/{} - Eliminando medicamento", id);
        boolean eliminado = medicamentoService.eliminarMedicamento(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    // GET /api/medicamentos/buscar - Buscar medicamentos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Medicamento>> buscarMedicamentosPorNombre(@RequestParam String nombre) {
        log.info("GET /api/medicamentos/buscar?nombre={} - Buscando medicamentos por nombre", nombre);
        List<Medicamento> medicamentos = medicamentoService.buscarMedicamentosPorNombre(nombre);
        return ResponseEntity.ok(medicamentos);
    }
    
    // GET /api/medicamentos/categoria/{categoria} - Buscar medicamentos por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Medicamento>> buscarMedicamentosPorCategoria(@PathVariable Medicamento.CategoriaMedicamento categoria) {
        log.info("GET /api/medicamentos/categoria/{} - Buscando medicamentos por categoría", categoria);
        List<Medicamento> medicamentos = medicamentoService.buscarMedicamentosPorCategoria(categoria);
        return ResponseEntity.ok(medicamentos);
    }
    
    // GET /api/medicamentos/stock-bajo - Obtener medicamentos con stock bajo
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<Medicamento>> obtenerMedicamentosConStockBajo() {
        log.info("GET /api/medicamentos/stock-bajo - Obteniendo medicamentos con stock bajo");
        List<Medicamento> medicamentos = medicamentoService.obtenerMedicamentosConStockBajo();
        return ResponseEntity.ok(medicamentos);
    }
    
    // GET /api/medicamentos/agotados - Obtener medicamentos agotados
    @GetMapping("/agotados")
    public ResponseEntity<List<Medicamento>> obtenerMedicamentosAgotados() {
        log.info("GET /api/medicamentos/agotados - Obteniendo medicamentos agotados");
        List<Medicamento> medicamentos = medicamentoService.obtenerMedicamentosAgotados();
        return ResponseEntity.ok(medicamentos);
    }
    
    // GET /api/medicamentos/caducidad - Obtener medicamentos próximos a caducar
    @GetMapping("/caducidad")
    public ResponseEntity<List<Medicamento>> obtenerMedicamentosProximosACaducar(@RequestParam(defaultValue = "30") int diasAntes) {
        log.info("GET /api/medicamentos/caducidad?diasAntes={} - Obteniendo medicamentos próximos a caducar", diasAntes);
        List<Medicamento> medicamentos = medicamentoService.obtenerMedicamentosProximosACaducar(diasAntes);
        return ResponseEntity.ok(medicamentos);
    }
    
    // GET /api/medicamentos/caducados - Obtener medicamentos caducados
    @GetMapping("/caducados")
    public ResponseEntity<List<Medicamento>> obtenerMedicamentosCaducados() {
        log.info("GET /api/medicamentos/caducados - Obteniendo medicamentos caducados");
        List<Medicamento> medicamentos = medicamentoService.obtenerMedicamentosCaducados();
        return ResponseEntity.ok(medicamentos);
    }
    
    // PUT /api/medicamentos/{id}/stock - Actualizar stock de medicamento
    @PutMapping("/{id}/stock")
    public ResponseEntity<Medicamento> actualizarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        log.info("PUT /api/medicamentos/{}/stock?cantidad={} - Actualizando stock", id, cantidad);
        Optional<Medicamento> medicamento = medicamentoService.actualizarStock(id, cantidad);
        return medicamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // PUT /api/medicamentos/{id}/reducir-stock - Reducir stock de medicamento
    @PutMapping("/{id}/reducir-stock")
    public ResponseEntity<Void> reducirStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        log.info("PUT /api/medicamentos/{}/reducir-stock?cantidad={} - Reduciendo stock", id, cantidad);
        try {
            boolean reducido = medicamentoService.reducirStock(id, cantidad);
            return reducido ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Error al reducir stock: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/medicamentos/{id}/aumentar-stock - Aumentar stock de medicamento
    @PutMapping("/{id}/aumentar-stock")
    public ResponseEntity<Medicamento> aumentarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        log.info("PUT /api/medicamentos/{}/aumentar-stock?cantidad={} - Aumentando stock", id, cantidad);
        Optional<Medicamento> medicamento = medicamentoService.aumentarStock(id, cantidad);
        return medicamento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/medicamentos/mas-vendidos - Obtener medicamentos más vendidos
    @GetMapping("/mas-vendidos")
    public ResponseEntity<List<Medicamento>> obtenerMedicamentosMasVendidos() {
        log.info("GET /api/medicamentos/mas-vendidos - Obteniendo medicamentos más vendidos");
        List<Medicamento> medicamentos = medicamentoService.obtenerMedicamentosMasVendidos();
        return ResponseEntity.ok(medicamentos);
    }
} 