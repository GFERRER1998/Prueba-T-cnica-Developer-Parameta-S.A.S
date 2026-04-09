package com.prueba.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EmpleadoResponse {

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVinculacion;

    private String cargo;
    private Double salario;

    private TiempoPeriodo edadActual;

    private TiempoPeriodo tiempoVinculacion;
}
