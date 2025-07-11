package com.example.farmacia.repository;

import com.example.farmacia.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Buscar por nombre o apellido
    List<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
    
    // Buscar por email
    Optional<Cliente> findByEmail(String email);
    
    // Buscar por DNI
    Optional<Cliente> findByDni(String dni);
    
    // Buscar por teléfono
    Optional<Cliente> findByTelefono(String telefono);
    
    // Buscar por estado
    List<Cliente> findByEstado(Cliente.EstadoCliente estado);
    
    // Buscar clientes por nombre completo
    @Query("SELECT c FROM Cliente c WHERE LOWER(CONCAT(c.nombre, ' ', c.apellido)) LIKE LOWER(CONCAT('%', :nombreCompleto, '%'))")
    List<Cliente> findByNombreCompletoContainingIgnoreCase(@Param("nombreCompleto") String nombreCompleto);
    
    // Obtener clientes más frecuentes (top 10)
    @Query("SELECT c, COUNT(v) as compras FROM Cliente c " +
           "LEFT JOIN c.ventas v " +
           "WHERE c.estado = 'ACTIVO' " +
           "GROUP BY c " +
           "ORDER BY compras DESC")
    List<Object[]> findClientesMasFrecuentes();
    
    // Obtener clientes con mayor gasto (top 10)
    @Query("SELECT c, SUM(v.total) as totalGastado FROM Cliente c " +
           "LEFT JOIN c.ventas v " +
           "WHERE c.estado = 'ACTIVO' " +
           "GROUP BY c " +
           "ORDER BY totalGastado DESC")
    List<Object[]> findClientesConMayorGasto();
    
    // Buscar clientes por múltiples criterios
    @Query("SELECT c FROM Cliente c WHERE " +
           "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:apellido IS NULL OR LOWER(c.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:estado IS NULL OR c.estado = :estado)")
    List<Cliente> findByCriteriosAvanzados(
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("email") String email,
            @Param("estado") Cliente.EstadoCliente estado
    );
} 