package com.prueba.soap;

import com.prueba.model.Empleado;
import com.prueba.repository.EmpleadoRepository;
import jakarta.jws.WebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@WebService(
    endpointInterface = "com.prueba.soap.EmpleadoService",
    portName          = "EmpleadoServicePort",
    serviceName       = "EmpleadoServiceService",
    targetNamespace   = "http://soap.prueba.com/"
)
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public String guardarEmpleado(EmpleadoSoapDto dto) {
        log.info("SOAP - guardarEmpleado invocado para: {} {}", dto.getNombres(), dto.getApellidos());
        try {
            Empleado empleado = mapDtoToEntity(dto);
            empleadoRepository.save(empleado);
            String msg = "Empleado guardado exitosamente con ID: " + empleado.getId();
            log.info("SOAP - {}", msg);
            return msg;
        } catch (Exception e) {
            String msg = "Error al guardar empleado: " + e.getMessage();
            log.error("SOAP - {}", msg, e);
            return msg;
        }
    }

    private Empleado mapDtoToEntity(EmpleadoSoapDto dto) {
        Empleado e = new Empleado();
        e.setNombres(dto.getNombres());
        e.setApellidos(dto.getApellidos());
        e.setTipoDocumento(dto.getTipoDocumento());
        e.setNumeroDocumento(dto.getNumeroDocumento());
        e.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
        e.setFechaVinculacion(LocalDate.parse(dto.getFechaVinculacion()));
        e.setCargo(dto.getCargo());
        e.setSalario(dto.getSalario());
        return e;
    }
}
