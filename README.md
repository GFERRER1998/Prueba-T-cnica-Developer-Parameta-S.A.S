# Prueba Técnica — REST + SOAP + MySQL

Implementación Java de un servicio REST que valida datos de empleados, invoca un servicio SOAP interno que persiste la información en MySQL, y retorna un JSON enriquecido con edad y tiempo de vinculación calculados.

---

## Tecnologías

| Capa | Tecnología |
|---|---|
| REST | Spring Boot 3.2 + Spring Web |
| SOAP | Apache CXF 4.0 (JAX-WS) |
| Persistencia | Spring Data JPA + Hibernate 6 |
| Base de datos | MySQL 8 |
| Build | Maven 3 |
| Java | Java 17 |

---

## Estructura del proyecto

```
prueba-tecnica/
├── pom.xml
├── docker-compose.yml
└── src/main/java/com/prueba/
    ├── PruebaTecnicaApplication.java
    ├── config/
    │   └── CxfConfig.java              ← Publica endpoint SOAP + crea cliente JAX-WS
    ├── dto/
    │   ├── EmpleadoResponse.java       ← Respuesta REST enriquecida
    │   └── TiempoPeriodo.java          ← DTO años/meses/días
    ├── exception/
    │   └── GlobalExceptionHandler.java ← Manejo global de errores
    ├── model/
    │   └── Empleado.java               ← Entidad JPA (tabla: empleados)
    ├── repository/
    │   └── EmpleadoRepository.java     ← Spring Data JPA
    ├── rest/
    │   └── EmpleadoController.java     ← GET /api/empleado
    ├── soap/
    │   ├── EmpleadoService.java        ← SEI (interfaz SOAP)
    │   ├── EmpleadoServiceImpl.java    ← Implementación SOAP
    │   └── EmpleadoSoapDto.java        ← DTO JAXB para SOAP
    └── validation/
        └── EmpleadoValidator.java      ← Validaciones de negocio
```

---

## Cómo ejecutar

### 1. Levantar MySQL con Docker

```bash
docker-compose up -d
```

Esto inicia MySQL 8 en `localhost:3306`, base de datos `empleados`, usuario `root`, contraseña `root`.

### 2. Compilar y ejecutar la aplicación

```bash
cd prueba-tecnica
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8080`.

---

## Endpoints disponibles

### REST — `GET /api/empleado`

**Parámetros de query (todos obligatorios):**

| Parámetro | Tipo | Ejemplo |
|---|---|---|
| `nombres` | String | `Juan Carlos` |
| `apellidos` | String | `Pérez Gómez` |
| `tipoDocumento` | String | `CC` |
| `numeroDocumento` | String | `1023456789` |
| `fechaNacimiento` | Date `yyyy-MM-dd` | `1990-05-15` |
| `fechaVinculacion` | Date `yyyy-MM-dd` | `2015-03-01` |
| `cargo` | String | `Desarrollador Senior` |
| `salario` | Double | `5000000` |

**Ejemplo de petición:**

```
GET http://localhost:8080/api/empleado?nombres=Juan%20Carlos&apellidos=Perez&tipoDocumento=CC&numeroDocumento=1023456789&fechaNacimiento=1990-05-15&fechaVinculacion=2015-03-01&cargo=Desarrollador&salario=5000000
```

**Ejemplo de respuesta exitosa (HTTP 200):**

```json
{
  "nombres": "Juan Carlos",
  "apellidos": "Perez",
  "tipoDocumento": "CC",
  "numeroDocumento": "1023456789",
  "fechaNacimiento": "1990-05-15",
  "fechaVinculacion": "2015-03-01",
  "cargo": "Desarrollador",
  "salario": 5000000.0,
  "edadActual": {
    "años": 35,
    "meses": 10,
    "dias": 24
  },
  "tiempoVinculacion": {
    "años": 11,
    "meses": 1,
    "dias": 8
  }
}
```

**Errores de validación (HTTP 400):**

```json
{ "error": "El campo 'fechaNacimiento' tiene un formato de fecha inválido. Formato esperado: yyyy-MM-dd." }
{ "error": "El empleado debe ser mayor de edad. Edad actual: 15 años." }
{ "error": "El parámetro 'nombres' es obligatorio." }
```

---

### SOAP — `POST /ws/empleados`

- **WSDL:** `http://localhost:8080/ws/empleados?wsdl`
- **Operación:** `guardarEmpleado(empleado)`
- **Namespace:** `http://soap.prueba.com/`

Ejemplo de petición SOAP (puede enviarse con SoapUI o Postman):

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:soap="http://soap.prueba.com/">
   <soapenv:Body>
      <soap:guardarEmpleado>
         <empleado>
            <nombres>Juan Carlos</nombres>
            <apellidos>Perez</apellidos>
            <tipoDocumento>CC</tipoDocumento>
            <numeroDocumento>1023456789</numeroDocumento>
            <fechaNacimiento>1990-05-15</fechaNacimiento>
            <fechaVinculacion>2015-03-01</fechaVinculacion>
            <cargo>Desarrollador</cargo>
            <salario>5000000</salario>
         </empleado>
      </soap:guardarEmpleado>
   </soapenv:Body>
</soapenv:Envelope>
```

---

## Flujo completo

```
Cliente HTTP
    │
    ▼
GET /api/empleado (EmpleadoController)
    │  1. Valida campos (vacíos, formato fecha, mayor de edad)
    │  2. Construye EmpleadoSoapDto
    │
    ▼
JAX-WS Client Proxy (CXF)
    │  HTTP POST → /ws/empleados
    │
    ▼
EmpleadoServiceImpl (SOAP)
    │  3. Mapea DTO → entidad JPA Empleado
    │  4. Persiste en MySQL via EmpleadoRepository
    │  5. Retorna mensaje de éxito/error
    │
    ▼
EmpleadoController (continúa)
    │  6. Calcula edadActual: Period.between(fechaNacimiento, hoy)
    │  7. Calcula tiempoVinculacion: Period.between(fechaVinculacion, hoy)
    │  8. Construye EmpleadoResponse enriquecido
    │
    ▼
Cliente HTTP ← JSON (HTTP 200)
```

---

## Validaciones implementadas

| Validación | Descripción |
|---|---|
| Campos obligatorios | Todos los 8 atributos deben estar presentes y no vacíos |
| Formato fecha | `fechaNacimiento` y `fechaVinculacion` deben ser `yyyy-MM-dd` |
| Mayor de edad | El empleado debe tener ≥ 18 años |
| Salario numérico | `salario` debe ser un número decimal válido y no negativo |
| Parámetros HTTP | Parámetros de query faltantes generan HTTP 400 descriptivo |
