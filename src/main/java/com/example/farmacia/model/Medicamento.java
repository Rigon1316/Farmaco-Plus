package com.example.farmacia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicamentos")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "detallesVenta")
public class Medicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank(message = "El nombre del medicamento es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @NotBlank(message = "El principio activo es obligatorio")
    @Size(max = 200, message = "El principio activo no puede exceder 200 caracteres")
    @Column(nullable = false)
    private String principioActivo;
    
    @NotBlank(message = "La presentación es obligatoria")
    @Size(max = 100, message = "La presentación no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String presentacion;
    
    @NotBlank(message = "La concentración es obligatoria")
    @Size(max = 50, message = "La concentración no puede exceder 50 caracteres")
    @Column(nullable = false)
    private String concentracion;
    
    @NotBlank(message = "El laboratorio es obligatorio")
    @Size(max = 150, message = "El laboratorio no puede exceder 150 caracteres")
    @Column(nullable = false)
    private String laboratorio;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;
    
    @NotNull(message = "El stock mínimo es obligatorio")
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Column(nullable = false)
    private Integer stockMinimo;
    
    @NotNull(message = "La fecha de caducidad es obligatoria")
    @Column(nullable = false)
    private LocalDate fechaCaducidad;
    
    @NotBlank(message = "El código de barras es obligatorio")
    @Size(max = 50, message = "El código de barras no puede exceder 50 caracteres")
    @Column(nullable = false, unique = true)
    private String codigoBarras;
    
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(length = 500)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaMedicamento categoria;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMedicamento estado = EstadoMedicamento.ACTIVO;
    
    @Column(nullable = false)
    private Boolean requiereReceta = false;
    
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    @OneToMany(mappedBy = "medicamento")
    private List<Lote> lotes;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate fechaActualizacion;
    
    @OneToMany(mappedBy = "medicamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("medicamento-detalles")
    private List<DetalleVenta> detallesVenta = new ArrayList<>();
    
    public enum CategoriaMedicamento {
        ANALGESICOS,
        ANTIBIOTICOS,
        ANTIINFLAMATORIOS,
        ANTIHISTAMINICOS,
        ANTIPIRETICOS,
        ANTITUSIVOS,
        LAXANTES,
        VITAMINAS,
        SUPLEMENTOS,
        DERMATOLOGICOS,
        OFTALMOLOGICOS,
        OTROS
    }
    
    public enum EstadoMedicamento {
        ACTIVO,
        INACTIVO,
        DESCONTINUADO
    }
    
    // Métodos para gestionar la relación bidireccional
    public void addDetalleVenta(DetalleVenta detalleVenta) {
        detallesVenta.add(detalleVenta);
        detalleVenta.setMedicamento(this);
    }
    
    public void removeDetalleVenta(DetalleVenta detalleVenta) {
        detallesVenta.remove(detalleVenta);
        detalleVenta.setMedicamento(null);
    }
    
    // Método para verificar si el stock está bajo
    @JsonIgnore
    public boolean isStockBajo() {
        return stock != null && stockMinimo != null && stock <= stockMinimo;
    }
    
    // Método para verificar si está agotado
    @JsonIgnore
    public boolean isAgotado() {
        return stock != null && stock <= 0;
    }
    
    // Método para verificar si está próximo a caducar (30 días)
    @JsonIgnore
    public boolean isProximoACaducar() {
        return fechaCaducidad != null && LocalDate.now().plusDays(30).isAfter(fechaCaducidad);
    }
    
    // Método para verificar si ya caducó
    @JsonIgnore
    public boolean isCaducado() {
        return fechaCaducidad != null && LocalDate.now().isAfter(fechaCaducidad);
    }
} 