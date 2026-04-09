package com.prueba.config;

import com.prueba.soap.EmpleadoService;
import com.prueba.soap.EmpleadoServiceImpl;
import jakarta.xml.ws.Endpoint;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CxfConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private EmpleadoServiceImpl empleadoServiceImpl;

    @Bean
    public Endpoint empleadoEndpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, empleadoServiceImpl);
        endpoint.publish("/empleados");
        return endpoint;
    }

    @Bean("empleadoServiceClient")
    public EmpleadoService empleadoServiceClient(
            @Value("${soap.service.url:http://localhost:8080/ws/empleados}") String url) {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(EmpleadoService.class);
        factory.setAddress(url);
        return (EmpleadoService) factory.create();
    }
}
