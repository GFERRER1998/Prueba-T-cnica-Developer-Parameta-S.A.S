package com.prueba.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@Getter
@Setter
@NoArgsConstructor
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombres;

    @Column(nullable = false)
    private String apellidos;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(name = "numero_documento", nullable = false, length = 50)
    private String numeroDocumento;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_vinculacion", nullable = false)
    private LocalDate fechaVinculacion;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false)
    private Double salario;
}
