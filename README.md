# API Clientes - SRJ

API REST construida con **Spring Boot**, **Spring Data JPA** y **SQL Server** para la gestión de clientes (`POST` y `DELETE`), con manejo centralizado de excepciones mediante un contrato JSON uniforme (`ApiErrorResponse`) y documentación interactiva con **Swagger / OpenAPI**.

---

## Stack Tecnológico

* **Java** + **Spring Boot** (Spring Web, Spring Data JPA, Bean Validation)
* **HikariCP** como pool de conexiones JDBC
* **SQL Server** (ejecutado en contenedor Docker, puerto por defecto `1433`)
* **Springdoc OpenAPI / Swagger UI** para documentación y pruebas de endpoints
* **Maven** para gestión de dependencias y compilación

---

## Estructura del Proyecto

```text
src/main/java/com/project/test/srj
├── SrjApplication.java              # Punto de entrada de la aplicación
├── config/                          # Configuración (OpenAPI / Swagger, etc.)
├── controller/
│   └── ClientController.java        # Endpoints REST (/api/clientes)
├── dto/
│   ├── ClientRequest.java           # Payload de entrada y validaciones
│   ├── ApiErrorResponse.java        # Respuesta estándar de error (400, 404, 503)
│   └── GenericResponse.java         # Respuesta estándar para operaciones exitosas (ej. DELETE)
├── exception/
│   ├── GlobalExceptionHandler.java  # Manejo centralizado (@RestControllerAdvice)
│   ├── ErrorDbException.java        # Excepción para fallos de base de datos
│   └── ClienteNoEncontradoException.java # Excepción cuando no existe el ID (404)
├── model/
│   └── Client.java                  # Entidad JPA mapeada a la tabla de clientes
├── repository/
│   └── ClientRepository.java        # Repositorio Spring Data JPA
└── service/
    ├── ClientService.java
    └── ClientServiceImpl.java       # Lógica de negocio y control de transacciones/errores
```

---

## Configuración (`application.properties`)

Ajusta los valores según tu entorno local o variables de entorno:

```properties
server.port=8080

# Conexión a SQL Server (Docker)
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=prueba_crud;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=TuPasswordSeguro

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Swagger UI
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
```

---

## Levantar SQL Server con Docker

Si aún no tienes el contenedor corriendo, puedes iniciarlo en el puerto `1433`:

```bash
docker run -e "ACCEPT_EULA=Y" \
  -e "MSSQL_SA_PASSWORD=TuPasswordSeguro" \
  -p 1433:1433 \
  --name sqlserver-express \
  -d [mcr.microsoft.com/mssql/server:2022-latest](https://mcr.microsoft.com/mssql/server:2022-latest)
```

### Notas útiles sobre `IDENTITY` en SQL Server
* Al eliminar filas con `DELETE`, SQL Server **no reinicia** automáticamente el contador `IDENTITY` (por lo que el siguiente `id` puede ser alto si hubo inserciones o reinicios previos).
* Para reiniciar el contador si la tabla está vacía:
  ```sql
  DBCC CHECKIDENT ('clientes', RESEED, 0);
  ```

---

## Ejecutar la Aplicación

1. Asegúrate de que el contenedor de SQL Server esté activo en `localhost:1433`.
2. Compila y ejecuta el proyecto con Maven:

```bash
mvn spring-boot:run
```

O con un puerto específico por línea de comandos:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

---

## Documentación Swagger / OpenAPI

Una vez iniciada la aplicación:

* **Swagger UI:** `http://localhost:8080/swagger-ui.html`
* **Especificación OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## Endpoints Disponibles

### 1. Crear un cliente
* **Método:** `POST`
* **Ruta:** `/api/clientes`
* **Body de ejemplo:**
  ```json
  {
    "nombre": "Celeste Rua",
    "apellido": "Rua Jaramillo",
    "direccion": "San Vicente"
  }
  ```
* **Respuesta (`201 Created`):**
  ```json
  {
    "id": 1,
    "nombre": "Celeste Rua",
    "apellido": "Rua Jaramillo",
    "direccion": "San Vicente"
  }
  ```

### 2. Eliminar un cliente por ID
* **Método:** `DELETE`
* **Ruta:** `/api/clientes/{id}`
* **Ejemplo:** `DELETE http://localhost:8080/api/clientes/1`
* **Respuesta (`200 OK`):**
  ```json
  {
    "codigo": "DELETE_SUCCESS",
    "mensaje": "Registro eliminado",
    "fechaHora": "2026-11-26T04:00:00Z"
  }
  ```

---

## Manejo Centralizado de Errores (`ApiErrorResponse`)

Todos los errores controlados devuelven una estructura JSON consistente:

```json
{
  "codigoError": "DB_001",
  "mensaje": "No fue posible conectar con la base de datos. Verifica que SQL Server esté activo.",
  "origen": "TransactionManager / HikariCP",
  "fechaHora": "2026-11-26T04:00:00Z"
}
```

| Código HTTP | Escenario | Excepción / Causa |
| :--- | :--- | :--- |
| `400 Bad Request` | Campos inválidos o faltantes en el payload | `MethodArgumentNotValidException` |
| `404 Not Found` | El `id` indicado en `DELETE /api/clientes/{id}` no existe | `ClienteNoEncontradoException` |
| `503 Service Unavailable` | Fallo de conexión con SQL Server, pool HikariCP sin conexiones o error de acceso a datos | `CannotCreateTransactionException` / `DataAccessException` / `ErrorDbException` |