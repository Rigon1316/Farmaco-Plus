package com.example.farmacia.controller;

import com.example.farmacia.model.Lote;
import com.example.farmacia.service.LoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.web.bind.annotation.CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
public class LoteController {
    private final LoteService loteService;

    @GetMapping
    public ResponseEntity<List<Lote>> obtenerTodos() {
        return ResponseEntity.ok(loteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lote> obtenerPorId(@PathVariable Long id) {
        return loteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Lote> crear(@RequestBody Lote lote) {
        return ResponseEntity.ok(loteService.crear(lote));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lote> actualizar(@PathVariable Long id, @RequestBody Lote lote) {
        return loteService.actualizar(id, lote)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (loteService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 