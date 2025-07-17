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
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "lotes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "medicamento")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "El número de lote es obligatorio")
    @Size(max = 50, message = "El número de lote no puede exceder 50 caracteres")
    @Column(nullable = false)
    private String numeroLote;

    @NotNull(message = "La fecha de entrada es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaEntrada;

    @NotNull(message = "La fecha de caducidad es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaCaducidad;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLote estado = EstadoLote.ACTIVO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private Medicamento medicamento;

    public enum EstadoLote {
        ACTIVO,
        AGOTADO,
        CADUCADO
    }
} 