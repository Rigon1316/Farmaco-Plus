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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "proveedores")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "medicamentos")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede exceder 150 caracteres")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El contacto es obligatorio")
    @Size(max = 100, message = "El contacto no puede exceder 100 caracteres")
    @Column(nullable = false)
    private String contacto;

    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    @Column(nullable = true)
    private String email;

    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe tener 11 dígitos")
    @Column(unique = true)
    private String ruc;

    @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
    @Column(length = 200)
    private String direccion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoProveedor estado = EstadoProveedor.ACTIVO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate fechaCreacion;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate fechaActualizacion;

    @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Medicamento> medicamentos = new ArrayList<>();

    public enum EstadoProveedor {
        ACTIVO,
        INACTIVO,
        SUSPENDIDO
    }
} 