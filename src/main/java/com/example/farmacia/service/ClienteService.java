package com.example.farmacia.service;

import com.example.farmacia.model.Cliente;
import com.example.farmacia.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        log.info("Obteniendo todos los clientes");
        return clienteRepository.findAll();
    }
    
    // Obtener cliente por ID
    public Optional<Cliente> obtenerClientePorId(Long id) {
        log.info("Obteniendo cliente con ID: {}", id);
        return clienteRepository.findById(id);
    }
    
    // Crear nuevo cliente
    public Cliente crearCliente(Cliente cliente) {
        log.info("Creando nuevo cliente: {}", cliente.getNombreCompleto());
        
        // Validar que el email no exista
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con el email: " + cliente.getEmail());
        }
        
        // Validar que el DNI no exista si se proporciona
        if (cliente.getDni() != null && !cliente.getDni().isEmpty() && 
            clienteRepository.findByDni(cliente.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con el DNI: " + cliente.getDni());
        }
        
        return clienteRepository.save(cliente);
    }
    
    // Actualizar cliente
    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        log.info("Actualizando cliente con ID: {}", id);
        
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setApellido(clienteActualizado.getApellido());
                    cliente.setEmail(clienteActualizado.getEmail());
                    cliente.setTelefono(clienteActualizado.getTelefono());
                    cliente.setDireccion(clienteActualizado.getDireccion());
                    cliente.setDni(clienteActualizado.getDni());
                    cliente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
                    cliente.setEstado(clienteActualizado.getEstado());
                    
                    return clienteRepository.save(cliente);
                });
    }
    
    // Eliminar cliente (eliminación física)
    public boolean eliminarCliente(Long id) {
        log.info("Eliminando cliente con ID: {}", id);
        return clienteRepository.findById(id)
                .map(cliente -> {
                    clienteRepository.delete(cliente);
                    return true;
                })
                .orElse(false);
    }
    
    // Buscar clientes por nombre o apellido
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        log.info("Buscando clientes por nombre: {}", nombre);
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(nombre, nombre);
    }
    
    // Buscar cliente por email
    public Optional<Cliente> buscarClientePorEmail(String email) {
        log.info("Buscando cliente por email: {}", email);
        return clienteRepository.findByEmail(email);
    }
    
    // Buscar cliente por DNI
    public Optional<Cliente> buscarClientePorDni(String dni) {
        log.info("Buscando cliente por DNI: {}", dni);
        return clienteRepository.findByDni(dni);
    }
    
    // Obtener clientes por estado
    public List<Cliente> obtenerClientesPorEstado(Cliente.EstadoCliente estado) {
        log.info("Obteniendo clientes por estado: {}", estado);
        return clienteRepository.findByEstado(estado);
    }
    
    // Obtener clientes activos
    public List<Cliente> obtenerClientesActivos() {
        log.info("Obteniendo clientes activos");
        return clienteRepository.findByEstado(Cliente.EstadoCliente.ACTIVO);
    }
    
    // Obtener clientes más frecuentes
    public List<Cliente> obtenerClientesMasFrecuentes() {
        log.info("Obteniendo clientes más frecuentes");
        return clienteRepository.findClientesMasFrecuentes().stream()
                .limit(10)
                .map(resultado -> (Cliente) resultado[0])
                .collect(Collectors.toList());
    }
    
    // Obtener clientes con mayor gasto
    public List<Cliente> obtenerClientesConMayorGasto() {
        log.info("Obteniendo clientes con mayor gasto");
        return clienteRepository.findClientesConMayorGasto().stream()
                .limit(10)
                .map(resultado -> (Cliente) resultado[0])
                .collect(Collectors.toList());
    }
    
    // Buscar clientes por múltiples criterios
    public List<Cliente> buscarClientesPorCriterios(String nombre, String apellido, String email, Cliente.EstadoCliente estado) {
        log.info("Buscando clientes por criterios - nombre: {}, apellido: {}, email: {}, estado: {}", 
                nombre, apellido, email, estado);
        return clienteRepository.findByCriteriosAvanzados(nombre, apellido, email, estado);
    }
    
    // Activar cliente
    public boolean activarCliente(Long id) {
        log.info("Activando cliente con ID: {}", id);
        
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setEstado(Cliente.EstadoCliente.ACTIVO);
                    clienteRepository.save(cliente);
                    return true;
                })
                .orElse(false);
    }
    
    // Suspender cliente
    public boolean suspenderCliente(Long id) {
        log.info("Suspendiendo cliente con ID: {}", id);
        
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setEstado(Cliente.EstadoCliente.SUSPENDIDO);
                    clienteRepository.save(cliente);
                    return true;
                })
                .orElse(false);
    }
} 