package com.example.farmacia.controller;

import com.example.farmacia.model.Cliente;
import com.example.farmacia.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
public class ClienteController {
    
    private final ClienteService clienteService;
    
    // GET /api/clientes - Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerTodosLosClientes() {
        log.info("GET /api/clientes - Obteniendo todos los clientes");
        List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }
    
    // GET /api/clientes/{id} - Obtener cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        log.info("GET /api/clientes/{} - Obteniendo cliente por ID", id);
        Optional<Cliente> cliente = clienteService.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/clientes/email/{email} - Obtener cliente por email
    @GetMapping("/email/{email}")
    public ResponseEntity<Cliente> obtenerClientePorEmail(@PathVariable String email) {
        log.info("GET /api/clientes/email/{} - Obteniendo cliente por email", email);
        Optional<Cliente> cliente = clienteService.buscarClientePorEmail(email);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // GET /api/clientes/dni/{dni} - Obtener cliente por DNI
    @GetMapping("/dni/{dni}")
    public ResponseEntity<Cliente> obtenerClientePorDni(@PathVariable String dni) {
        log.info("GET /api/clientes/dni/{} - Obteniendo cliente por DNI", dni);
        Optional<Cliente> cliente = clienteService.buscarClientePorDni(dni);
        return cliente.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/clientes - Crear nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        log.info("POST /api/clientes - Creando nuevo cliente: {}", cliente.getNombreCompleto());
        try {
            Cliente clienteCreado = clienteService.crearCliente(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(clienteCreado);
        } catch (RuntimeException e) {
            log.error("Error al crear cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // PUT /api/clientes/{id} - Actualizar cliente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, 
                                                       @Valid @RequestBody Cliente cliente) {
        log.info("PUT /api/clientes/{} - Actualizando cliente", id);
        try {
            Optional<Cliente> clienteActualizado = clienteService.actualizarCliente(id, cliente);
            return clienteActualizado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            log.error("Error al actualizar cliente: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    // DELETE /api/clientes/{id} - Eliminar cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        log.info("DELETE /api/clientes/{} - Eliminando cliente", id);
        boolean eliminado = clienteService.eliminarCliente(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    
    // GET /api/clientes/buscar - Buscar clientes por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Cliente>> buscarClientesPorNombre(@RequestParam String nombre) {
        log.info("GET /api/clientes/buscar?nombre={} - Buscando clientes por nombre", nombre);
        List<Cliente> clientes = clienteService.buscarClientesPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }
    

    
    // GET /api/clientes/estado/{estado} - Obtener clientes por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Cliente>> obtenerClientesPorEstado(@PathVariable Cliente.EstadoCliente estado) {
        log.info("GET /api/clientes/estado/{} - Obteniendo clientes por estado", estado);
        List<Cliente> clientes = clienteService.obtenerClientesPorEstado(estado);
        return ResponseEntity.ok(clientes);
    }
    
    // GET /api/clientes/activos - Obtener clientes activos
    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> obtenerClientesActivos() {
        log.info("GET /api/clientes/activos - Obteniendo clientes activos");
        List<Cliente> clientes = clienteService.obtenerClientesActivos();
        return ResponseEntity.ok(clientes);
    }
    

} 