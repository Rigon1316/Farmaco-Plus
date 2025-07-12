package com.example.farmacia.repository;

import com.example.farmacia.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    
    // Buscar por nombre (búsqueda parcial)
    List<Medicamento> findByNombreContainingIgnoreCase(String nombre);
    
    // Buscar por principio activo
    List<Medicamento> findByPrincipioActivoContainingIgnoreCase(String principioActivo);
    
    // Buscar por laboratorio
    List<Medicamento> findByLaboratorioContainingIgnoreCase(String laboratorio);
    
    // Buscar por categoría
    List<Medicamento> findByCategoria(Medicamento.CategoriaMedicamento categoria);
    
    // Buscar por estado
    List<Medicamento> findByEstado(Medicamento.EstadoMedicamento estado);
    
    // Buscar por código de barras
    Optional<Medicamento> findByCodigoBarras(String codigoBarras);
    
    // Buscar medicamentos con stock bajo
    @Query("SELECT m FROM Medicamento m WHERE m.stock <= m.stockMinimo AND m.estado = 'ACTIVO'")
    List<Medicamento> findMedicamentosConStockBajo();
    
    // Buscar medicamentos agotados
    @Query("SELECT m FROM Medicamento m WHERE m.stock = 0 AND m.estado = 'ACTIVO'")
    List<Medicamento> findMedicamentosAgotados();
    
    // Buscar medicamentos próximos a caducar
    @Query("SELECT m FROM Medicamento m WHERE m.fechaCaducidad <= :fechaLimite AND m.estado = 'ACTIVO'")
    List<Medicamento> findMedicamentosProximosACaducar(@Param("fechaLimite") LocalDate fechaLimite);
    
    // Buscar medicamentos caducados
    @Query("SELECT m FROM Medicamento m WHERE m.fechaCaducidad < :fechaActual AND m.estado = 'ACTIVO'")
    List<Medicamento> findMedicamentosCaducados(@Param("fechaActual") LocalDate fechaActual);
    
    // Buscar medicamentos que requieren receta
    List<Medicamento> findByRequiereRecetaTrue();
    
    // Buscar medicamentos por rango de precios
    @Query("SELECT m FROM Medicamento m WHERE m.precio BETWEEN :precioMin AND :precioMax AND m.estado = 'ACTIVO'")
    List<Medicamento> findByPrecioBetween(@Param("precioMin") Double precioMin, @Param("precioMax") Double precioMax);
    
    // Contar medicamentos por categoría
    @Query("SELECT m.categoria, COUNT(m) FROM Medicamento m WHERE m.estado = 'ACTIVO' GROUP BY m.categoria")
    List<Object[]> countByCategoria();
    
    // Obtener medicamentos más vendidos (top 10)
    @Query("SELECT m, COUNT(dv) as ventas FROM Medicamento m " +
           "LEFT JOIN m.detallesVenta dv " +
           "WHERE m.estado = 'ACTIVO' " +
           "GROUP BY m " +
           "ORDER BY ventas DESC")
    List<Object[]> findMedicamentosMasVendidos();
    
    // Buscar medicamentos por múltiples criterios
    @Query("SELECT m FROM Medicamento m WHERE " +
           "(:nombre IS NULL OR LOWER(m.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:categoria IS NULL OR m.categoria = :categoria) AND " +
           "(:laboratorio IS NULL OR LOWER(m.laboratorio) LIKE LOWER(CONCAT('%', :laboratorio, '%'))) AND " +
           "(:requiereReceta IS NULL OR m.requiereReceta = :requiereReceta) AND " +
           "m.estado = 'ACTIVO'")
    List<Medicamento> findByCriteriosAvanzados(
            @Param("nombre") String nombre,
            @Param("categoria") Medicamento.CategoriaMedicamento categoria,
            @Param("laboratorio") String laboratorio,
            @Param("requiereReceta") Boolean requiereReceta
    );
} 