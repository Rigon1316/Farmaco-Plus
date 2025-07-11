package com.example.farmacia.controller;

import com.example.farmacia.service.NlqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nlq")
@RequiredArgsConstructor
@Slf4j
public class NlqController {
    
    private final NlqService nlqService;
    
    // POST /api/nlq/query - Consulta en lenguaje natural
    @PostMapping("/query")
    public ResponseEntity<String> consultaNatural(@RequestBody Map<String, String> request) {
        String pregunta = request.get("pregunta");
        log.info("POST /api/nlq/query - Consulta en lenguaje natural: {}", pregunta);
        
        if (pregunta == null || pregunta.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            String resultado = (String) nlqService.answer(pregunta);
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Error en consulta NLQ: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    

} 