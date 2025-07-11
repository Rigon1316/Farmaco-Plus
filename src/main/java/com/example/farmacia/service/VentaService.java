package com.example.farmacia.service;

import com.example.farmacia.model.Venta;
import com.example.farmacia.model.DetalleVenta;
import com.example.farmacia.model.Medicamento;
import com.example.farmacia.model.Cliente;
import com.example.farmacia.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class VentaService {
    
    private final VentaRepository ventaRepository;
    private final MedicamentoService medicamentoService;
    private final ClienteService clienteService;
    
    private static final BigDecimal IVA_RATE = new BigDecimal("0.15"); // 15% IVA
    
    // Obtener todas las ventas
    public List<Venta> obtenerTodasLasVentas() {
        log.info("Obteniendo todas las ventas");
        return ventaRepository.findAll();
    }
    
    // Obtener venta por ID
    public Optional<Venta> obtenerVentaPorId(Long id) {
        log.info("Obteniendo venta con ID: {}", id);
        return ventaRepository.findById(id);
    }
    
    // Obtener venta por número de factura
    public Optional<Venta> obtenerVentaPorNumeroFactura(String numeroFactura) {
        log.info("Obteniendo venta con número de factura: {}", numeroFactura);
        return ventaRepository.findByNumeroFactura(numeroFactura);
    }
    
    // Crear nueva venta (método original mantenido para compatibilidad)
    public Venta crearVenta(Venta venta) {
        log.info("Creando nueva venta: {}", venta.getNumeroFactura());
        
        // Validar que el número de factura no exista
        if (ventaRepository.findByNumeroFactura(venta.getNumeroFactura()).isPresent()) {
            throw new RuntimeException("Ya existe una venta con el número de factura: " + venta.getNumeroFactura());
        }
        
        // Validar que el cliente exista
        if (venta.getCliente() == null || venta.getCliente().getId() == null) {
            throw new RuntimeException("El cliente es obligatorio");
        }
        
        // Verificar que el cliente realmente existe en la base de datos
        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(venta.getCliente().getId());
        if (clienteExistente.isEmpty()) {
            throw new RuntimeException("El cliente con ID " + venta.getCliente().getId() + " no existe en la base de datos");
        }
        
        // Verificar que el cliente esté activo
        if (clienteExistente.get().getEstado() != Cliente.EstadoCliente.ACTIVO) {
            throw new RuntimeException("El cliente con ID " + venta.getCliente().getId() + " no está activo. Estado actual: " + clienteExistente.get().getEstado());
        }
        
        // Asignar el cliente completo desde la base de datos
        venta.setCliente(clienteExistente.get());
        
        // Validar stock de medicamentos
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getMedicamento() == null || detalle.getMedicamento().getId() == null) {
                throw new RuntimeException("El medicamento es obligatorio en todos los detalles");
            }
            
            Optional<Medicamento> medicamento = medicamentoService.obtenerMedicamentoPorId(detalle.getMedicamento().getId());
            if (medicamento.isEmpty()) {
                throw new RuntimeException("Medicamento no encontrado: " + detalle.getMedicamento().getId());
            }
            
            if (medicamento.get().getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el medicamento: " + medicamento.get().getNombre());
            }
        }
        
        // Reducir stock de medicamentos
        for (DetalleVenta detalle : venta.getDetalles()) {
            medicamentoService.reducirStock(detalle.getMedicamento().getId(), detalle.getCantidad());
        }
        
        // Calcular totales
        calcularTotalesVenta(venta);
        
        // Generar alertas si es necesario
        generarAlertasVenta(venta);
        
        return ventaRepository.save(venta);
    }
    
    // Actualizar estado de venta
    public Optional<Venta> actualizarEstadoVenta(Long id, Venta.EstadoVenta nuevoEstado) {
        log.info("Actualizando estado de venta {} a {}", id, nuevoEstado);
        
        return ventaRepository.findById(id)
                .map(venta -> {
                    venta.setEstado(nuevoEstado);
                    
                    // Si se cancela o devuelve la venta, restaurar stock
                    if ((nuevoEstado == Venta.EstadoVenta.CANCELADA || nuevoEstado == Venta.EstadoVenta.DEVUELTA) 
                        && venta.getEstado() == Venta.EstadoVenta.PAGADA) {
                        restaurarStockMedicamentos(venta);
                    }
                    
                    return ventaRepository.save(venta);
                });
    }
    
    // Obtener ventas por cliente
    public List<Venta> obtenerVentasPorCliente(Long clienteId) {
        log.info("Obteniendo ventas del cliente: {}", clienteId);
        return ventaRepository.findByClienteId(clienteId);
    }
    
    // Obtener ventas por estado
    public List<Venta> obtenerVentasPorEstado(Venta.EstadoVenta estado) {
        log.info("Obteniendo ventas por estado: {}", estado);
        return ventaRepository.findByEstado(estado);
    }
    
    // Obtener ventas por método de pago
    public List<Venta> obtenerVentasPorMetodoPago(Venta.MetodoPago metodoPago) {
        log.info("Obteniendo ventas por método de pago: {}", metodoPago);
        return ventaRepository.findByMetodoPago(metodoPago);
    }
    
    // Obtener ventas por rango de fechas
    public List<Venta> obtenerVentasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Obteniendo ventas entre {} y {}", fechaInicio, fechaFin);
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin);
    }
    
    // Obtener ventas del día
    public List<Venta> obtenerVentasDelDia() {
        log.info("Obteniendo ventas del día");
        LocalDateTime inicioDia = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finDia = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return ventaRepository.findByFechaVentaBetween(inicioDia, finDia);
    }
    
    // Obtener ventas del mes
    public List<Venta> obtenerVentasDelMes() {
        log.info("Obteniendo ventas del mes");
        LocalDateTime inicioMes = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime finMes = LocalDateTime.now().withDayOfMonth(LocalDateTime.now().toLocalDate().lengthOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        return ventaRepository.findByFechaVentaBetween(inicioMes, finMes);
    }
    
    // Calcular total de ventas por período
    public BigDecimal calcularTotalVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Calculando total de ventas del {} al {}", fechaInicio, fechaFin);
        return ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin)
                .stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.PAGADA)
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Calcular totales de la venta
    private void calcularTotalesVenta(Venta venta) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        // Calcular subtotal sumando todos los detalles
        for (DetalleVenta detalle : venta.getDetalles()) {
            // Calcular subtotal del detalle si no está calculado
            if (detalle.getSubtotal() == null) {
                detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            }
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        
        // Calcular IVA usando la tasa definida
        BigDecimal iva = subtotal.multiply(IVA_RATE).setScale(2, RoundingMode.HALF_UP);
        
        // Calcular total
        BigDecimal total = subtotal.add(iva);
        
        // Asignar valores a la venta
        venta.setSubtotal(subtotal);
        venta.setIgv(iva);
        venta.setTotal(total);
        
        log.info("Totales calculados - Subtotal: {}, IVA: {}, Total: {}", subtotal, iva, total);
    }
    
    // Generar alertas para la venta
    private void generarAlertasVenta(Venta venta) {
        // Aquí se pueden generar alertas específicas de ventas
        // Por ejemplo, alertas de demanda alta para ciertos medicamentos
        log.info("Generando alertas para la venta: {}", venta.getNumeroFactura());
    }
    
    // Restaurar stock de medicamentos (para cancelaciones/devoluciones)
    private void restaurarStockMedicamentos(Venta venta) {
        for (DetalleVenta detalle : venta.getDetalles()) {
            medicamentoService.aumentarStock(detalle.getMedicamento().getId(), detalle.getCantidad());
        }
    }
    
    // Obtener promedio de ventas por día
    public BigDecimal calcularPromedioVentasPorDia(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Calculando promedio de ventas por día entre {} y {}", fechaInicio, fechaFin);
        List<Venta> ventas = ventaRepository.findByFechaVentaBetween(fechaInicio, fechaFin)
                .stream()
                .filter(v -> v.getEstado() == Venta.EstadoVenta.PAGADA)
                .collect(Collectors.toList());
        
        if (ventas.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = ventas.stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.divide(BigDecimal.valueOf(ventas.size()), 2, RoundingMode.HALF_UP);
    }
    
    // Obtener top ventas
    public List<Venta> obtenerTopVentas() {
        log.info("Obteniendo top ventas");
        return ventaRepository.findByEstado(Venta.EstadoVenta.PAGADA)
                .stream()
                .sorted((v1, v2) -> v2.getTotal().compareTo(v1.getTotal()))
                .limit(10)
                .collect(Collectors.toList());
    }
    

} 