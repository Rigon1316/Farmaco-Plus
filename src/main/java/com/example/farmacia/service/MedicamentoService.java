package com.example.farmacia.service;

import com.example.farmacia.model.Medicamento;
import com.example.farmacia.repository.MedicamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicamentoService {
    
    private final MedicamentoRepository medicamentoRepository;
    private final AlertaService alertaService;
    
    // Obtener todos los medicamentos
    public List<Medicamento> obtenerTodosLosMedicamentos() {
        log.info("Obteniendo todos los medicamentos");
        return medicamentoRepository.findAll();
    }
    
    // Obtener medicamento por ID
    public Optional<Medicamento> obtenerMedicamentoPorId(Long id) {
        log.info("Obteniendo medicamento con ID: {}", id);
        return medicamentoRepository.findById(id);
    }
    
    // Obtener medicamento por código de barras
    public Optional<Medicamento> obtenerMedicamentoPorCodigoBarras(String codigoBarras) {
        log.info("Obteniendo medicamento con código de barras: {}", codigoBarras);
        return medicamentoRepository.findByCodigoBarras(codigoBarras);
    }
    
    // Crear nuevo medicamento
    public Medicamento crearMedicamento(Medicamento medicamento) {
        log.info("Creando nuevo medicamento: {}", medicamento.getNombre());
        
        // Validar que el código de barras no exista
        if (medicamentoRepository.findByCodigoBarras(medicamento.getCodigoBarras()).isPresent()) {
            throw new RuntimeException("Ya existe un medicamento con el código de barras: " + medicamento.getCodigoBarras());
        }
        
        // Validar que el nombre no exista
        if (!medicamentoRepository.findByNombreContainingIgnoreCase(medicamento.getNombre()).isEmpty()) {
            throw new RuntimeException("Ya existe un medicamento con el nombre: " + medicamento.getNombre());
        }
        
        Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);
        
        // Verificar si necesita alertas
        verificarAlertasMedicamento(medicamentoGuardado);
        
        return medicamentoGuardado;
    }
    
    // Actualizar medicamento
    public Optional<Medicamento> actualizarMedicamento(Long id, Medicamento medicamentoActualizado) {
        log.info("Actualizando medicamento con ID: {}", id);
        
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    // Actualizar campos
                    medicamento.setNombre(medicamentoActualizado.getNombre());
                    medicamento.setPrincipioActivo(medicamentoActualizado.getPrincipioActivo());
                    medicamento.setPresentacion(medicamentoActualizado.getPresentacion());
                    medicamento.setConcentracion(medicamentoActualizado.getConcentracion());
                    medicamento.setLaboratorio(medicamentoActualizado.getLaboratorio());
                    medicamento.setPrecio(medicamentoActualizado.getPrecio());
                    medicamento.setStock(medicamentoActualizado.getStock());
                    medicamento.setStockMinimo(medicamentoActualizado.getStockMinimo());
                    medicamento.setFechaCaducidad(medicamentoActualizado.getFechaCaducidad());
                    medicamento.setDescripcion(medicamentoActualizado.getDescripcion());
                    medicamento.setCategoria(medicamentoActualizado.getCategoria());
                    medicamento.setEstado(medicamentoActualizado.getEstado());
                    medicamento.setRequiereReceta(medicamentoActualizado.getRequiereReceta());
                    
                    Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);
                    
                    // Verificar si necesita alertas
                    verificarAlertasMedicamento(medicamentoGuardado);
                    
                    return medicamentoGuardado;
                });
    }
    
    // Eliminar medicamento (cambiar estado a DESCONTINUADO)
    public boolean eliminarMedicamento(Long id) {
        log.info("Eliminando medicamento con ID: {}", id);
        
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    medicamento.setEstado(Medicamento.EstadoMedicamento.DESCONTINUADO);
                    medicamentoRepository.save(medicamento);
                    return true;
                })
                .orElse(false);
    }
    
    // Buscar medicamentos por nombre
    public List<Medicamento> buscarMedicamentosPorNombre(String nombre) {
        log.info("Buscando medicamentos por nombre: {}", nombre);
        return medicamentoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    // Buscar medicamentos por categoría
    public List<Medicamento> buscarMedicamentosPorCategoria(Medicamento.CategoriaMedicamento categoria) {
        log.info("Buscando medicamentos por categoría: {}", categoria);
        return medicamentoRepository.findByCategoria(categoria);
    }
    
    // Obtener medicamentos con stock bajo
    public List<Medicamento> obtenerMedicamentosConStockBajo() {
        log.info("Obteniendo medicamentos con stock bajo");
        return medicamentoRepository.findMedicamentosConStockBajo();
    }
    
    // Obtener medicamentos agotados
    public List<Medicamento> obtenerMedicamentosAgotados() {
        log.info("Obteniendo medicamentos agotados");
        return medicamentoRepository.findMedicamentosAgotados();
    }
    
    // Obtener medicamentos próximos a caducar
    public List<Medicamento> obtenerMedicamentosProximosACaducar(int diasAntes) {
        log.info("Obteniendo medicamentos próximos a caducar en {} días", diasAntes);
        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(diasAntes);
        return medicamentoRepository.findMedicamentosProximosACaducar(fechaLimite);
    }
    
    // Obtener medicamentos caducados
    public List<Medicamento> obtenerMedicamentosCaducados() {
        log.info("Obteniendo medicamentos caducados");
        return medicamentoRepository.findMedicamentosCaducados(LocalDateTime.now());
    }
    
    // Actualizar stock de medicamento
    public Optional<Medicamento> actualizarStock(Long id, Integer nuevaCantidad) {
        log.info("Actualizando stock del medicamento {} a {}", id, nuevaCantidad);
        
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    medicamento.setStock(nuevaCantidad);
                    Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);
                    
                    // Verificar si necesita alertas
                    verificarAlertasMedicamento(medicamentoGuardado);
                    
                    return medicamentoGuardado;
                });
    }
    
    // Reducir stock (para ventas)
    public boolean reducirStock(Long id, Integer cantidad) {
        log.info("Reduciendo stock del medicamento {} en {}", id, cantidad);
        
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    if (medicamento.getStock() >= cantidad) {
                        medicamento.setStock(medicamento.getStock() - cantidad);
                        Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);
                        
                        // Verificar si necesita alertas
                        verificarAlertasMedicamento(medicamentoGuardado);
                        
                        return true;
                    } else {
                        throw new RuntimeException("Stock insuficiente para el medicamento: " + medicamento.getNombre());
                    }
                })
                .orElse(false);
    }
    
    // Aumentar stock (para compras/ingresos)
    public Optional<Medicamento> aumentarStock(Long id, Integer cantidad) {
        log.info("Aumentando stock del medicamento {} en {}", id, cantidad);
        
        return medicamentoRepository.findById(id)
                .map(medicamento -> {
                    medicamento.setStock(medicamento.getStock() + cantidad);
                    Medicamento medicamentoGuardado = medicamentoRepository.save(medicamento);
                    
                    // Verificar si necesita alertas
                    verificarAlertasMedicamento(medicamentoGuardado);
                    
                    return medicamentoGuardado;
                });
    }
    
    // Obtener medicamentos más vendidos
    public List<Medicamento> obtenerMedicamentosMasVendidos() {
        log.info("Obteniendo medicamentos más vendidos");
        return medicamentoRepository.findMedicamentosMasVendidos().stream()
                .limit(10)
                .map(resultado -> (Medicamento) resultado[0])
                .collect(Collectors.toList());
    }
    
    // Verificar alertas para un medicamento
    private void verificarAlertasMedicamento(Medicamento medicamento) {
        // Verificar stock bajo
        if (medicamento.getStock() <= medicamento.getStockMinimo() && medicamento.getStock() > 0) {
            alertaService.crearAlertaStockBajo(medicamento);
        }
        
        // Verificar stock agotado
        if (medicamento.getStock() == 0) {
            alertaService.crearAlertaStockAgotado(medicamento);
        }
        
        // Verificar fecha de caducidad
        if (medicamento.getFechaCaducidad().isBefore(LocalDateTime.now())) {
            alertaService.crearAlertaMedicamentoCaducado(medicamento);
        } else if (medicamento.getFechaCaducidad().isBefore(LocalDateTime.now().plusDays(30))) {
            alertaService.crearAlertaFechaCaducidad(medicamento);
        }
    }
} 