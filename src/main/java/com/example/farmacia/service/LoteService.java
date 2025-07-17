package com.example.farmacia.service;

import com.example.farmacia.model.Lote;
import com.example.farmacia.repository.LoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoteService {
    private final LoteRepository loteRepository;

    public List<Lote> obtenerTodos() {
        return loteRepository.findAll();
    }

    public Optional<Lote> obtenerPorId(Long id) {
        return loteRepository.findById(id);
    }

    public Lote crear(Lote lote) {
        return loteRepository.save(lote);
    }

    public Optional<Lote> actualizar(Long id, Lote lote) {
        return loteRepository.findById(id).map(l -> {
            l.setNumeroLote(lote.getNumeroLote());
            l.setFechaEntrada(lote.getFechaEntrada());
            l.setFechaCaducidad(lote.getFechaCaducidad());
            l.setCantidad(lote.getCantidad());
            l.setMedicamento(lote.getMedicamento());
            return loteRepository.save(l);
        });
    }

    public boolean eliminar(Long id) {
        if (loteRepository.existsById(id)) {
            loteRepository.deleteById(id);
            return true;
        }
        return false;
    }
} 