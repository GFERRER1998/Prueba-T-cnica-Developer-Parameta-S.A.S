package com.prueba.rest;

import com.prueba.dto.EmpleadoResponse;
import com.prueba.dto.TiempoPeriodo;
import com.prueba.soap.EmpleadoService;
import com.prueba.soap.EmpleadoSoapDto;
import com.prueba.validation.EmpleadoValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class EmpleadoController {

    @Autowired
    private EmpleadoValidator validator;

    @Autowired
    @Qualifier("empleadoServiceClient")
    private EmpleadoService empleadoServiceClient;

    @GetMapping("/empleado")
    public ResponseEntity<?> registrarEmpleado(
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String tipoDocumento,
            @RequestParam String numeroDocumento,
            @RequestParam String fechaNacimiento,
            @RequestParam String fechaVinculacion,
            @RequestParam String cargo,
            @RequestParam String salario) {

        log.info("REST GET /api/empleado — nombres={} apellidos={}", nombres, apellidos);

        try {
            validator.validate(nombres, apellidos, tipoDocumento, numeroDocumento,
                    fechaNacimiento, fechaVinculacion, cargo, salario);
        } catch (IllegalArgumentException ex) {
            log.warn("Validación fallida: {}", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(errorBody(ex.getMessage()));
        }

        EmpleadoSoapDto soapDto = buildSoapDto(nombres, apellidos, tipoDocumento,
                numeroDocumento, fechaNacimiento, fechaVinculacion, cargo, salario);

        String soapResult;
        try {
            soapResult = empleadoServiceClient.guardarEmpleado(soapDto);
        } catch (Exception ex) {
            log.error("Error al invocar el servicio SOAP", ex);
            return ResponseEntity.internalServerError()
                    .body(errorBody("Error al invocar el servicio SOAP: " + ex.getMessage()));
        }

        if (soapResult != null && soapResult.startsWith("Error")) {
            return ResponseEntity.internalServerError()
                    .body(errorBody(soapResult));
        }

        log.info("SOAP respondió: {}", soapResult);

        LocalDate nacimiento  = LocalDate.parse(fechaNacimiento);
        LocalDate vinculacion = LocalDate.parse(fechaVinculacion);
        LocalDate hoy         = LocalDate.now();

        Period edadPeriodo       = Period.between(nacimiento,  hoy);
        Period vinculacionPeriodo = Period.between(vinculacion, hoy);

        EmpleadoResponse response = new EmpleadoResponse();
        response.setNombres(nombres);
        response.setApellidos(apellidos);
        response.setTipoDocumento(tipoDocumento);
        response.setNumeroDocumento(numeroDocumento);
        response.setFechaNacimiento(nacimiento);
        response.setFechaVinculacion(vinculacion);
        response.setCargo(cargo);
        response.setSalario(Double.parseDouble(salario));

        response.setEdadActual(new TiempoPeriodo(
                edadPeriodo.getYears(),
                edadPeriodo.getMonths(),
                edadPeriodo.getDays()));

        response.setTiempoVinculacion(new TiempoPeriodo(
                vinculacionPeriodo.getYears(),
                vinculacionPeriodo.getMonths(),
                vinculacionPeriodo.getDays()));

        return ResponseEntity.ok(response);
    }

    private EmpleadoSoapDto buildSoapDto(
            String nombres, String apellidos, String tipoDocumento,
            String numeroDocumento, String fechaNacimiento,
            String fechaVinculacion, String cargo, String salario) {

        EmpleadoSoapDto dto = new EmpleadoSoapDto();
        dto.setNombres(nombres);
        dto.setApellidos(apellidos);
        dto.setTipoDocumento(tipoDocumento);
        dto.setNumeroDocumento(numeroDocumento);
        dto.setFechaNacimiento(fechaNacimiento);
        dto.setFechaVinculacion(fechaVinculacion);
        dto.setCargo(cargo);
        dto.setSalario(Double.parseDouble(salario));
        return dto;
    }

    private Map<String, String> errorBody(String message) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("error", message);
        return body;
    }
}
