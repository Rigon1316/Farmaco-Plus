package com.example.farmacia.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "ventas")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String nombre;
    
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String apellido;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    @Column(nullable = false, unique = true)
    private String email;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener 10 dígitos")
    @Column(nullable = false)
    private String telefono;
    
    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(length = 200)
    private String direccion;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "El DNI debe tener 10 dígitos")
    @Column(unique = true)
    private String dni;
    
    @Column
    private LocalDate fechaNacimiento;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCliente estado = EstadoCliente.ACTIVO;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate fechaActualizacion;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference("cliente-ventas")
    private List<Venta> ventas = new ArrayList<>();
    
    public enum EstadoCliente {
        ACTIVO,
        INACTIVO,
        SUSPENDIDO
    }
    
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
    
    // Métodos para gestionar la relación bidireccional
    public void addVenta(Venta venta) {
        ventas.add(venta);
        venta.setCliente(this);
    }
    
    public void removeVenta(Venta venta) {
        ventas.remove(venta);
        venta.setCliente(null);
    }
} 