package com.example.farmacia.repository;

import com.example.farmacia.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    java.util.Optional<Proveedor> findByRuc(String ruc);
} 