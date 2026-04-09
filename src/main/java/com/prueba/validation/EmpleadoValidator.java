package com.prueba.validation;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

@Component
public class EmpleadoValidator {

    private static final int EDAD_MINIMA = 18;

    public void validate(
            String nombres,
            String apellidos,
            String tipoDocumento,
            String numeroDocumento,
            String fechaNacimiento,
            String fechaVinculacion,
            String cargo,
            String salario) {

        validarNoVacio("nombres",          nombres);
        validarNoVacio("apellidos",        apellidos);
        validarNoVacio("tipoDocumento",    tipoDocumento);
        validarNoVacio("numeroDocumento",  numeroDocumento);
        validarNoVacio("fechaNacimiento",  fechaNacimiento);
        validarNoVacio("fechaVinculacion", fechaVinculacion);
        validarNoVacio("cargo",            cargo);
        validarNoVacio("salario",          salario);

        LocalDate nacimiento  = parsearFecha("fechaNacimiento",  fechaNacimiento);
        parsearFecha("fechaVinculacion", fechaVinculacion);

        validarSalario(salario);

        Period edad = Period.between(nacimiento, LocalDate.now());
        if (edad.getYears() < EDAD_MINIMA) {
            throw new IllegalArgumentException(
                "El empleado debe ser mayor de edad. Edad actual: " + edad.getYears() + " años."
            );
        }
    }

    private void validarNoVacio(String campo, String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(
                "El campo '" + campo + "' es obligatorio y no puede estar vacío."
            );
        }
    }

    private LocalDate parsearFecha(String campo, String valor) {
        try {
            return LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "El campo '" + campo + "' tiene un formato de fecha inválido. " +
                "Formato esperado: yyyy-MM-dd. Valor recibido: '" + valor + "'."
            );
        }
    }

    private void validarSalario(String valor) {
        try {
            double salario = Double.parseDouble(valor);
            if (salario < 0) {
                throw new IllegalArgumentException("El campo 'salario' no puede ser negativo.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "El campo 'salario' debe ser un valor numérico válido. Valor recibido: '" + valor + "'."
            );
        }
    }
}
