package com.example.farmacia.repository;

import com.example.farmacia.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    
    // Métodos básicos de búsqueda
    List<Alerta> findByTipo(Alerta.TipoAlerta tipo);
    List<Alerta> findByNivel(Alerta.NivelAlerta nivel);
    List<Alerta> findByEstado(Alerta.EstadoAlerta estado);
    List<Alerta> findByEstadoIn(List<Alerta.EstadoAlerta> estados);
    List<Alerta> findByMedicamentoId(Long medicamentoId);
    
    // Búsqueda por rango de fechas (útil para la IA)
    List<Alerta> findByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    // Consulta flexible para múltiples criterios (útil para la IA)
    @Query("SELECT a FROM Alerta a WHERE " +
           "(:tipo IS NULL OR a.tipo = :tipo) AND " +
           "(:nivel IS NULL OR a.nivel = :nivel) AND " +
           "(:estado IS NULL OR a.estado = :estado) AND " +
           "(:medicamentoId IS NULL OR a.medicamento.id = :medicamentoId) AND " +
           "(:fechaInicio IS NULL OR a.fechaCreacion >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR a.fechaCreacion <= :fechaFin)")
    List<Alerta> findByCriteriosAvanzados(
            @Param("tipo") Alerta.TipoAlerta tipo,
            @Param("nivel") Alerta.NivelAlerta nivel,
            @Param("estado") Alerta.EstadoAlerta estado,
            @Param("medicamentoId") Long medicamentoId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
} 