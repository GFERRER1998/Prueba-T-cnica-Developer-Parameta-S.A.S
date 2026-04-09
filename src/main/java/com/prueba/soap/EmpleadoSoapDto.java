package com.prueba.soap;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "empleado", namespace = "http://soap.prueba.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
    name      = "EmpleadoSoapDto",
    namespace = "http://soap.prueba.com/",
    propOrder = {
        "nombres", "apellidos", "tipoDocumento", "numeroDocumento",
        "fechaNacimiento", "fechaVinculacion", "cargo", "salario"
    }
)
@Getter
@Setter
@NoArgsConstructor
public class EmpleadoSoapDto {

    private String nombres;
    private String apellidos;
    private String tipoDocumento;
    private String numeroDocumento;

    private String fechaNacimiento;

    private String fechaVinculacion;

    private String cargo;
    private Double salario;
}
