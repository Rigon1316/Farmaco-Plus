# Sistema de Gestión de Farmacia

## Descripción
Sistema de gestión integral para farmacias desarrollado con Spring Boot, que permite administrar medicamentos, inventario, ventas, clientes y alertas del sistema.

## Tecnologías Utilizadas
- **Spring Boot 3.5.4-SNAPSHOT**
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Spring Validation**
- **Java 17**

## Estructura del Proyecto

### Modelos de Datos
- **Medicamento**: Gestión de medicamentos con información detallada
- **Categoria**: Categorización de medicamentos
- **Laboratorio**: Información de laboratorios farmacéuticos
- **Stock**: Control de inventario por lotes
- **Venta**: Registro de ventas
- **DetalleVenta**: Detalles de productos en ventas
- **Cliente**: Información de clientes
- **Usuario**: Gestión de usuarios del sistema
- **Proveedor**: Información de proveedores
- **Alerta**: Sistema de alertas para stock y vencimientos

### Enums
- **EstadoVenta**: Estados de las ventas (PENDIENTE, COMPLETADA, CANCELADA, DEVUELTA)
- **MetodoPago**: Métodos de pago disponibles
- **TipoAlerta**: Tipos de alertas del sistema
- **NivelPrioridad**: Niveles de prioridad para alertas
- **TipoDocumento**: Tipos de documentos de identidad
- **RolUsuario**: Roles de usuarios del sistema

## Configuración

### Base de Datos
1. Crear una base de datos PostgreSQL llamada `farmacia`
2. Configurar las credenciales en `application.properties`
3. La aplicación creará automáticamente las tablas al iniciar

### Configuración de la Aplicación
```properties
# Base de datos
spring.datasource.url=jdbc:postgresql://localhost:5432/farmacia
spring.datasource.username=postgres
spring.datasource.password=password

# Servidor
server.port=8080
server.servlet.context-path=/api
```

## Ejecución
1. Clonar el repositorio
2. Configurar la base de datos PostgreSQL
3. Ejecutar: `mvn spring-boot:run`
4. La aplicación estará disponible en: `http://localhost:8080/api`

## Características Principales
- ✅ Gestión completa de medicamentos
- ✅ Control de inventario por lotes
- ✅ Sistema de ventas
- ✅ Gestión de clientes y usuarios
- ✅ Sistema de alertas automáticas
- ✅ Validaciones de datos
- ✅ Auditoría de cambios

## Próximos Pasos
- [ ] Implementar controladores REST
- [ ] Agregar servicios de negocio
- [ ] Implementar autenticación y autorización
- [ ] Crear interfaz de usuario
- [ ] Agregar reportes y estadísticas 