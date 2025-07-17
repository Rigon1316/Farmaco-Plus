package com.example.farmacia.service;

import com.example.farmacia.model.Alerta;
import com.example.farmacia.model.Medicamento;
import com.example.farmacia.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AlertaService {
    
    private final AlertaRepository alertaRepository;
    
    @Value("${farmacia.alertas.dias-antes-caducidad:30}")
    private int diasAntesCaducidad;
    
    @Value("${farmacia.alertas.stock-minimo:10}")
    private int stockMinimo;
    

    
    // Obtener todas las alertas
    public List<Alerta> obtenerTodasLasAlertas() {
        log.info("Obteniendo todas las alertas");
        return alertaRepository.findAll();
    }
    
    // Obtener alerta por ID
    public Optional<Alerta> obtenerAlertaPorId(Long id) {
        log.info("Obteniendo alerta con ID: {}", id);
        return alertaRepository.findById(id);
    }
    
    // Eliminar alerta por ID
    public boolean eliminarAlerta(Long id) {
        log.info("Eliminando alerta con ID: {}", id);
        if (alertaRepository.existsById(id)) {
            alertaRepository.deleteById(id);
            log.info("Alerta con ID {} eliminada exitosamente", id);
            return true;
        } else {
            log.warn("No se encontró alerta con ID {} para eliminar", id);
            return false;
        }
    }
    
    // Crear nueva alerta
    public Alerta crearAlerta(Alerta alerta) {
        log.info("Creando nueva alerta: {}", alerta.getTitulo());
        Alerta alertaGuardada = alertaRepository.save(alerta);
        
        // Log de alerta crítica
        if (alerta.getNivel() == Alerta.NivelAlerta.CRITICA) {
            log.warn("ALERTA CRÍTICA CREADA: {} - {}", alerta.getTitulo(), alerta.getMensaje());
        }
        
        return alertaGuardada;
    }
    
    // Actualizar estado de alerta
    public Optional<Alerta> actualizarEstadoAlerta(Long id, Alerta.EstadoAlerta nuevoEstado) {
        log.info("Actualizando estado de alerta {} a {}", id, nuevoEstado);
        return alertaRepository.findById(id)
                .map(alerta -> {
                    alerta.setEstado(nuevoEstado);
                    if (nuevoEstado == Alerta.EstadoAlerta.RESUELTA) {
                        alerta.setFechaResolucion(LocalDate.now());
                    }
                    Alerta alertaGuardada = alertaRepository.save(alerta);
                    return alertaGuardada;
                });
    }
    
    // Obtener alertas por tipo
    public List<Alerta> obtenerAlertasPorTipo(Alerta.TipoAlerta tipo) {
        log.info("Obteniendo alertas por tipo: {}", tipo);
        return alertaRepository.findByTipo(tipo);
    }
    
    // Obtener alertas por nivel
    public List<Alerta> obtenerAlertasPorNivel(Alerta.NivelAlerta nivel) {
        log.info("Obteniendo alertas por nivel: {}", nivel);
        return alertaRepository.findByNivel(nivel);
    }
    
    // Obtener alertas por estado
    public List<Alerta> obtenerAlertasPorEstado(Alerta.EstadoAlerta estado) {
        log.info("Obteniendo alertas por estado: {}", estado);
        return alertaRepository.findByEstado(estado);
    }
    
    // Obtener alertas no resueltas
    public List<Alerta> obtenerAlertasNoResueltas() {
        log.info("Obteniendo alertas no resueltas");
        return alertaRepository.findByEstadoIn(List.of(Alerta.EstadoAlerta.PENDIENTE, Alerta.EstadoAlerta.EN_PROCESO));
    }
    
    // Obtener alertas del día
    public List<Alerta> obtenerAlertasDelDia() {
        log.info("Obteniendo alertas del día");
        LocalDate hoy = LocalDate.now();
        return alertaRepository.findByFechaCreacionBetween(hoy, hoy);
    }
    
    // Obtener alertas críticas
    public List<Alerta> obtenerAlertasCriticas() {
        log.info("Obteniendo alertas críticas");
        return alertaRepository.findByNivel(Alerta.NivelAlerta.CRITICA);
    }
    
    // Obtener alertas por medicamento
    public List<Alerta> obtenerAlertasPorMedicamento(Long medicamentoId) {
        log.info("Obteniendo alertas por medicamento: {}", medicamentoId);
        return alertaRepository.findByMedicamentoId(medicamentoId);
    }
    
    // Crear alerta de stock bajo
    public void crearAlertaStockBajo(Medicamento medicamento) {
        log.info("Creando alerta de stock bajo para: {}", medicamento.getNombre());
        
        // Verificar si ya existe una alerta pendiente para este medicamento
        List<Alerta> alertasExistentes = alertaRepository.findByMedicamentoId(medicamento.getId());
        boolean existeAlertaPendiente = alertasExistentes.stream()
                .anyMatch(a -> a.getTipo() == Alerta.TipoAlerta.STOCK_BAJO && 
                              a.getEstado() == Alerta.EstadoAlerta.PENDIENTE);
        
        if (!existeAlertaPendiente) {
            Alerta alerta = new Alerta();
            alerta.setTitulo("Stock Bajo - " + medicamento.getNombre());
            alerta.setMensaje("El medicamento " + medicamento.getNombre() + " tiene stock bajo. " +
                    "Stock actual: " + medicamento.getStock() + ", Stock mínimo: " + medicamento.getStockMinimo());
            alerta.setTipo(Alerta.TipoAlerta.STOCK_BAJO);
            alerta.setNivel(medicamento.getStock() == 0 ? Alerta.NivelAlerta.CRITICA : Alerta.NivelAlerta.ALTA);
            alerta.setMedicamento(medicamento);
            
            crearAlerta(alerta);
        }
    }
    
    // Crear alerta de stock agotado
    public void crearAlertaStockAgotado(Medicamento medicamento) {
        log.info("Creando alerta de stock agotado para: {}", medicamento.getNombre());
        
        Alerta alerta = new Alerta();
        alerta.setTitulo("Stock Agotado - " + medicamento.getNombre());
        alerta.setMensaje("El medicamento " + medicamento.getNombre() + " se ha agotado completamente.");
        alerta.setTipo(Alerta.TipoAlerta.STOCK_AGOTADO);
        alerta.setNivel(Alerta.NivelAlerta.CRITICA);
        alerta.setMedicamento(medicamento);
        
        crearAlerta(alerta);
    }
    
    // Crear alerta de fecha de caducidad
    public void crearAlertaFechaCaducidad(Medicamento medicamento) {
        log.info("Creando alerta de fecha de caducidad para: {}", medicamento.getNombre());
        
        Alerta alerta = new Alerta();
        alerta.setTitulo("Fecha de Caducidad Próxima - " + medicamento.getNombre());
        alerta.setMensaje("El medicamento " + medicamento.getNombre() + " caduca el " + 
                medicamento.getFechaCaducidad() + ". Stock actual: " + medicamento.getStock());
        alerta.setTipo(Alerta.TipoAlerta.FECHA_CADUCIDAD);
        alerta.setNivel(Alerta.NivelAlerta.MEDIA);
        alerta.setMedicamento(medicamento);
        
        crearAlerta(alerta);
    }
    
    // Crear alerta de medicamento caducado
    public void crearAlertaMedicamentoCaducado(Medicamento medicamento) {
        log.info("Creando alerta de medicamento caducado para: {}", medicamento.getNombre());
        
        Alerta alerta = new Alerta();
        alerta.setTitulo("Medicamento Caducado - " + medicamento.getNombre());
        alerta.setMensaje("El medicamento " + medicamento.getNombre() + " ha caducado el " + 
                medicamento.getFechaCaducidad() + ". Stock actual: " + medicamento.getStock());
        alerta.setTipo(Alerta.TipoAlerta.MEDICAMENTO_CADUCADO);
        alerta.setNivel(Alerta.NivelAlerta.CRITICA);
        alerta.setMedicamento(medicamento);
        
        crearAlerta(alerta);
    }
    
    // Tarea programada para verificar alertas diariamente
    @Scheduled(cron = "0 0 8 * * ?") // Todos los días a las 8:00 AM
    public void verificarAlertasDiarias() {
        log.info("Ejecutando verificación diaria de alertas");
        
        List<Alerta> alertasPendientes = alertaRepository.findByEstadoIn(List.of(Alerta.EstadoAlerta.PENDIENTE, Alerta.EstadoAlerta.EN_PROCESO));
        if (!alertasPendientes.isEmpty()) {
            log.info("Hay {} alertas pendientes", alertasPendientes.size());
            
            // Log de resumen de alertas pendientes
            for (Alerta alerta : alertasPendientes) {
                log.info("Alerta pendiente: {} - {}", alerta.getTitulo(), alerta.getNivel());
            }
        }
    }
    
} 