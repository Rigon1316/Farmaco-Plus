package com.example.farmacia.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NlqService {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    private static final Logger log = LoggerFactory.getLogger(NlqService.class);

    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    public NlqService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.objectMapper = new ObjectMapper();
    }

    public Object answer(String pregunta) {
        log.info("Pregunta recibida: {}", pregunta);
        System.out.println("ENTRANDO EN LA AI prompt");

        // FILTRO: Si la pregunta es sobre todos los medicamentos, devolver solo los nombres directamente
        // Eliminado para que toda consulta pase por la IA
        
        // Prompt dirigido a generar JPQL para el sistema de farmacia
        String prompt = """
Eres un asistente experto en transformar preguntas en lenguaje natural a JPQL (Java Persistence Query Language) para un sistema de farmacia. Devuelve SOLO la consulta JPQL, sin explicaciones ni formato adicional. Usa siempre LOWER y LIKE para búsquedas de texto, nunca uses '=' para comparar nombres o textos. Si la pregunta es ambigua, haz la consulta más general posible.

Entidades principales:
- Medicamento (id, nombre, principioActivo, presentacion, concentracion, laboratorio, precio, stock, stockMinimo, fechaCaducidad, descripcion, categoria, estado, requiereReceta, codigoBarras)
- Cliente (id, nombre, apellido, email, telefono, direccion, dni, fechaNacimiento, estado)
- Venta (id, numeroFactura, fechaVenta, subtotal, igv, total, metodoPago, estado, cliente, detalles)
- DetalleVenta (id, cantidad, precioUnitario, subtotal, medicamento, venta)
- Alerta (id, titulo, mensaje, tipo, nivel, estado, fechaCreacion, fechaResolucion, medicamento)

Ejemplos:
Pregunta: ¿Cuáles son los nombres de todos los clientes?
JPQL: select c.nombre from Cliente c

Pregunta: Dame el stock mínimo de paracetamol
JPQL: select m.stockMinimo from Medicamento m where LOWER(m.nombre) LIKE '%%paracetamol%%'

Pregunta: ¿Qué clientes han comprado antibióticos?
JPQL: select distinct v.cliente from Venta v join v.detalles d join d.medicamento m where m.categoria = 'ANTIBIOTICOS'

Pregunta: ¿Cuántas ventas se hicieron hoy?
JPQL: select count(v) from Venta v where v.fechaVenta >= CURRENT_DATE

Pregunta: %s
""".formatted(pregunta);
        log.debug("Prompt enviado a la IA:\n{}", prompt);
        System.out.println("SALIENDO EN LA AI prompt");
        String jpql = null;
        String responseJson = null;
        try {
            System.out.println("ENTRANDO EN LA AI TRY");

            WebClient client = WebClient.builder()
                    .baseUrl("https://openrouter.ai")
                    .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            System.out.println("client");

            responseJson = client.post()
                    .uri("/api/v1/chat/completions")
                    .bodyValue("""
        {
          "model": "openai/gpt-4.1",
          "messages": [
            {"role": "user", "content": "%s"}
          ],
          "max_tokens": 1000
        }
        """.formatted(prompt))
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            response -> {
                                return response.bodyToMono(String.class)
                                        .flatMap(errorBody -> {
                                            log.error("Error del servidor OpenRouter: {}", errorBody);
                                            return Mono.error(new RuntimeException("Error al llamar a OpenRouter: " + errorBody));
                                        });
                            })
                    .bodyToMono(String.class)
                    .block();

            System.out.println("responseJson");

            // Parsear manualmente la respuesta
            @SuppressWarnings("unchecked")
            Map<String, Object> response = objectMapper.readValue(responseJson, Map.class);
            @SuppressWarnings("unchecked")
            Map<String, Object> choice = (Map<String, Object>) ((List<?>) response.get("choices")).get(0);
            @SuppressWarnings("unchecked")
            Map<String, Object> message = (Map<String, Object>) choice.get("message");
            jpql = message.get("content").toString().trim();
            log.info("JPQL generado por la IA: {}", jpql);

            // Validar que la respuesta parezca una consulta JPQL
            if (!jpql.toLowerCase().startsWith("select")) {
                log.warn("La IA no devolvió una consulta JPQL válida: {}", jpql);
                return "No se pudo interpretar la pregunta. Intenta reformularla.";
            }

            // Ejecutar JPQL
            try {
                log.debug("Ejecutando JPQL: {}", jpql);
                Query query = entityManager.createQuery(jpql);
                List<?> resultList = query.getResultList();
                log.info("Resultados obtenidos: {}", resultList.size());
                return aLenguajeNatural(resultList, jpql, pregunta);
            } catch (Exception e) {
                log.error("Error al ejecutar JPQL: {}", e.getMessage(), e);
                return "Ocurrió un error al procesar la consulta: " + e.getMessage();
            }

        } catch (Exception e) {
            log.error("Error general en la consulta IA: {}", e.getMessage(), e);
            return "Ocurrió un error al procesar la consulta: " + e.getMessage();
        }
    }

    private String aLenguajeNatural(List<?> resultList, String jpql, String pregunta) {
        if (resultList == null || resultList.isEmpty()) {
            return "No se encontraron resultados para la consulta.";
        }
        // Si es un solo valor simple
        if (resultList.size() == 1 && (resultList.get(0) instanceof String || resultList.get(0) instanceof Number || resultList.get(0) instanceof Boolean)) {
            Object valor = resultList.get(0);
            if (pregunta.toLowerCase().contains("stock minimo")) {
                return "El stock mínimo es " + valor + ".";
            }
            return "El resultado es: " + valor + ".";
        }
        // Si es una lista de arreglos (por ejemplo, nombre y apellido)
        if (resultList.get(0) instanceof Object[]) {
            List<String> combinados = new java.util.ArrayList<>();
            for (Object obj : resultList) {
                Object[] arr = (Object[]) obj;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i] != null) {
                        if (sb.length() > 0) sb.append(" ");
                        sb.append(arr[i].toString());
                    }
                }
                if (sb.length() > 0) combinados.add(sb.toString());
            }
            if (!combinados.isEmpty()) {
                return String.join(", ", combinados);
            }
        }
        // Si es una lista de strings
        if (resultList.get(0) instanceof String) {
            return String.join(", ", resultList.stream().map(Object::toString).toList());
        }
        // Si es una lista de mapas con nombre y apellido
        if (resultList.get(0) instanceof Map) {
            List<String> nombres = new java.util.ArrayList<>();
            for (Object obj : resultList) {
                Map<?,?> map = (Map<?,?>) obj;
                Object nombre = map.get("nombre");
                Object apellido = map.get("apellido");
                if (nombre != null && apellido != null) {
                    nombres.add(nombre + " " + apellido);
                } else if (nombre != null) {
                    nombres.add(nombre.toString());
                }
            }
            if (!nombres.isEmpty()) {
                return String.join(", ", nombres);
            }
        }
        // Lógica para devolver solo un resultado si la pregunta lo sugiere
        String preguntaLower = pregunta.toLowerCase();
        boolean pideSoloUno = preguntaLower.contains("el más caro") || preguntaLower.contains("el mas caro") || preguntaLower.contains("el más barato") || preguntaLower.contains("el mas barato") || preguntaLower.contains("el primero") || preguntaLower.contains("el último") || preguntaLower.contains("el ultimo") || preguntaLower.contains("el mayor") || preguntaLower.contains("el menor") || preguntaLower.contains("mayor precio") || preguntaLower.contains("menor precio") || preguntaLower.contains("más costoso") || preguntaLower.contains("mas costoso") || preguntaLower.contains("más barato") || preguntaLower.contains("mas barato");
        boolean pideTodos = preguntaLower.contains("todos") || preguntaLower.contains("todas") || preguntaLower.contains("lista") || preguntaLower.contains("muestrame todos") || preguntaLower.contains("muéstrame todos");

        // Si es una lista de entidades Medicamento, mostrar solo el nombre (y si pide solo uno, solo el primero)
        Object first = resultList.get(0);
        if (first != null && first.getClass().getSimpleName().equals("Medicamento")) {
            List<String> nombres = new java.util.ArrayList<>();
            for (Object obj : resultList) {
                try {
                    java.lang.reflect.Method getNombre = obj.getClass().getMethod("getNombre");
                    String nombre = (String) getNombre.invoke(obj);
                    nombres.add(nombre);
                } catch (Exception e) {
                    nombres.add(obj.toString());
                }
            }
            if (pideSoloUno && !nombres.isEmpty()) {
                return nombres.get(0);
            }
            return String.join(", ", nombres);
        }
        // Si es una lista de entidades Cliente, mostrar nombre y apellido (y si pide solo uno, solo el primero)
        if (first != null && first.getClass().getSimpleName().equals("Cliente")) {
            List<String> nombres = new java.util.ArrayList<>();
            for (Object obj : resultList) {
                try {
                    java.lang.reflect.Method getNombre = obj.getClass().getMethod("getNombre");
                    java.lang.reflect.Method getApellido = obj.getClass().getMethod("getApellido");
                    String nombre = (String) getNombre.invoke(obj);
                    String apellido = (String) getApellido.invoke(obj);
                    nombres.add(nombre + " " + apellido);
                } catch (Exception e) {
                    nombres.add(obj.toString());
                }
            }
            if (pideSoloUno && !nombres.isEmpty()) {
                return nombres.get(0);
            }
            return String.join(", ", nombres);
        }
        // Por defecto, mostrar los datos como texto plano separados por coma
        return resultList.stream().map(Object::toString).collect(java.util.stream.Collectors.joining(", "));
    }

    private Map<String, Object> entityToMap(Object entity) {
        Map<String, Object> map = new HashMap<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(entity));
            } catch (IllegalAccessException ignored) {}
        }
        return map;
    }
} 