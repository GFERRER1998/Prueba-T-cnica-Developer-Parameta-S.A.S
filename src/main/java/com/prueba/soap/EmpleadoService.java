package com.prueba.soap;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

@WebService(
    name            = "EmpleadoService",
    targetNamespace = "http://soap.prueba.com/"
)
public interface EmpleadoService {

    @WebMethod(operationName = "guardarEmpleado")
    @WebResult(name = "resultado")
    String guardarEmpleado(
        @WebParam(name = "empleado") EmpleadoSoapDto empleado
    );
}
