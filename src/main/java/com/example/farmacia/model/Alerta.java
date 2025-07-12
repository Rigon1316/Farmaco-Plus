package com.example.farmacia.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "alertas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "medicamento")
public class Alerta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank(message = "El título de la alerta es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    @Column(nullable = false)
    private String titulo;
    
    @NotBlank(message = "El mensaje de la alerta es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede exceder 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String mensaje;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipo;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelAlerta nivel = NivelAlerta.MEDIA;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlerta estado = EstadoAlerta.PENDIENTE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id")
    private Medicamento medicamento;
    
    @Column
    private LocalDate fechaResolucion;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String observaciones;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;
    
    public enum TipoAlerta {
        STOCK_BAJO,
        FECHA_CADUCIDAD,
        STOCK_AGOTADO,
        MEDICAMENTO_CADUCADO,
        PRECIO_ALTO,
        DEMANDA_ALTA
    }
    
    public enum NivelAlerta {
        BAJA,
        MEDIA,
        ALTA,
        CRITICA
    }
    
    public enum EstadoAlerta {
        PENDIENTE,
        EN_PROCESO,
        RESUELTA,
        DESCARTADA
    }
    
    // Método para marcar la alerta como resuelta
    public void resolver(String observaciones) {
        this.estado = EstadoAlerta.RESUELTA;
        this.fechaResolucion = LocalDate.now();
        this.observaciones = observaciones;
    }
    
    // Método para marcar la alerta como descartada
    public void descartar(String observaciones) {
        this.estado = EstadoAlerta.DESCARTADA;
        this.fechaResolucion = LocalDate.now();
        this.observaciones = observaciones;
    }
    
    // Método para marcar la alerta como en proceso
    public void marcarEnProceso() {
        this.estado = EstadoAlerta.EN_PROCESO;
    }
    
    // Método para verificar si la alerta está pendiente
    public boolean isPendiente() {
        return EstadoAlerta.PENDIENTE.equals(estado);
    }
    
    // Método para verificar si la alerta está resuelta
    public boolean isResuelta() {
        return EstadoAlerta.RESUELTA.equals(estado);
    }
    
    // Método para verificar si la alerta es crítica
    public boolean isCritica() {
        return NivelAlerta.CRITICA.equals(nivel);
    }
    
    // Método para obtener el tiempo transcurrido desde la creación
    public long getTiempoTranscurrido() {
        return java.time.temporal.ChronoUnit.DAYS.between(fechaCreacion, LocalDate.now());
    }
} 