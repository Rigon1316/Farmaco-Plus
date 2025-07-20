package com.example.farmacia.service;

import com.example.farmacia.model.Proveedor;
import com.example.farmacia.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProveedorService {
    private final ProveedorRepository proveedorRepository;

    public List<Proveedor> obtenerTodos() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Optional<Proveedor> obtenerPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    public Proveedor crear(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Optional<Proveedor> actualizar(Long id, Proveedor proveedor) {
        return proveedorRepository.findById(id).map(p -> {
            p.setNombre(proveedor.getNombre());
            p.setContacto(proveedor.getContacto());
            p.setDireccion(proveedor.getDireccion());
            return proveedorRepository.save(p);
        });
    }

    public boolean eliminar(Long id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 