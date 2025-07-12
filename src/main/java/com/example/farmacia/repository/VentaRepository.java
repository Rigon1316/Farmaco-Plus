package com.example.farmacia.repository;

import com.example.farmacia.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    // Métodos básicos de búsqueda
    Optional<Venta> findByNumeroFactura(String numeroFactura);
    List<Venta> findByClienteId(Long clienteId);
    List<Venta> findByEstado(Venta.EstadoVenta estado);
    List<Venta> findByMetodoPago(Venta.MetodoPago metodoPago);
    
    // Búsqueda por rango de fechas (útil para la IA)
    List<Venta> findByFechaVentaBetween(LocalDate fechaInicio, LocalDate fechaFin);
    
    // Consulta flexible para múltiples criterios (útil para la IA)
    @Query("SELECT v FROM Venta v WHERE " +
           "(:clienteId IS NULL OR v.cliente.id = :clienteId) AND " +
           "(:estado IS NULL OR v.estado = :estado) AND " +
           "(:metodoPago IS NULL OR v.metodoPago = :metodoPago) AND " +
           "(:fechaInicio IS NULL OR v.fechaVenta >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR v.fechaVenta <= :fechaFin)")
    List<Venta> findByCriteriosAvanzados(
            @Param("clienteId") Long clienteId,
            @Param("estado") Venta.EstadoVenta estado,
            @Param("metodoPago") Venta.MetodoPago metodoPago,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin
    );
} 