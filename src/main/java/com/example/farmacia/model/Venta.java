package com.example.farmacia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"cliente", "detalles"})
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 20, message = "El número de factura no puede exceder 20 caracteres")
    @Column(nullable = false, unique = true)
    private String numeroFactura;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference
    private Cliente cliente;
    
    @NotNull(message = "El subtotal es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El subtotal debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    @NotNull(message = "El IGV es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El IGV no puede ser negativo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal igv;
    
    @NotNull(message = "El total es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado = EstadoVenta.PENDIENTE;
    
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String observaciones;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaVenta;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<DetalleVenta> detalles = new ArrayList<>();
    
    public enum MetodoPago {
        EFECTIVO,
        TARJETA_CREDITO,
        TARJETA_DEBITO,
        TRANSFERENCIA,
        YAPE,
        PLIN
    }
    
    public enum EstadoVenta {
        PENDIENTE,
        PAGADA,
        CANCELADA,
        DEVUELTA
    }
    
    // Métodos para gestionar la relación bidireccional
    public void addDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        detalle.setVenta(this);
    }
    
    public void removeDetalle(DetalleVenta detalle) {
        detalles.remove(detalle);
        detalle.setVenta(null);
    }
    
    // Método para calcular el total de la venta
    public void calcularTotales() {
        this.subtotal = detalles.stream()
                .map(DetalleVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        this.igv = subtotal.multiply(new BigDecimal("0.18")); // 18% IGV
        this.total = subtotal.add(igv);
    }
    
    // Método para verificar si la venta está pagada
    public boolean isPagada() {
        return EstadoVenta.PAGADA.equals(estado);
    }
    
    // Método para verificar si la venta está cancelada
    public boolean isCancelada() {
        return EstadoVenta.CANCELADA.equals(estado);
    }
} 