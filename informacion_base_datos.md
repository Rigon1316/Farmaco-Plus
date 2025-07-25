# Sistema de Gestión de Farmacia - Información de Base de Datos

## 📋 Resumen del Sistema

Este sistema de gestión de farmacia está desarrollado en **Spring Boot** con **PostgreSQL** como base de datos. El sistema maneja inventario, ventas, clientes, proveedores, alertas y reportes.

## 🗄️ Configuración de Base de Datos

### Base de Datos Principal

- **Motor**: PostgreSQL
- **Nombre**: `farmacia_db`
- **Puerto**: 5432
- **Usuario**: `postgres`
- **Contraseña**: `Incipio123`

### Configuración en application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/farmacia_db
spring.datasource.username=postgres
spring.datasource.password=Incipio123
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
```

## 📊 Estructura de Tablas

### 1. **proveedores**

- **Propósito**: Gestionar proveedores de medicamentos
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `nombre` (VARCHAR(150))
  - `contacto` (VARCHAR(100))
  - `email` (VARCHAR(150))
  - `ruc` (VARCHAR(11) UNIQUE)
  - `direccion` (VARCHAR(200))
  - `estado` (ENUM: ACTIVO, INACTIVO, SUSPENDIDO)

### 2. **clientes**

- **Propósito**: Gestionar información de clientes
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `nombre` (VARCHAR(100))
  - `apellido` (VARCHAR(100))
  - `email` (VARCHAR(150) UNIQUE)
  - `telefono` (VARCHAR(10))
  - `dni` (VARCHAR(10) UNIQUE)
  - `fecha_nacimiento` (DATE)
  - `estado` (ENUM: ACTIVO, INACTIVO, SUSPENDIDO)

### 3. **medicamentos**

- **Propósito**: Gestionar inventario de medicamentos
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `nombre` (VARCHAR(200) UNIQUE)
  - `principio_activo` (VARCHAR(200))
  - `presentacion` (VARCHAR(100))
  - `concentracion` (VARCHAR(50))
  - `laboratorio` (VARCHAR(150))
  - `precio` (DECIMAL(10,2))
  - `stock` (INTEGER)
  - `stock_minimo` (INTEGER)
  - `fecha_caducidad` (DATE)
  - `codigo_barras` (VARCHAR(50) UNIQUE)
  - `categoria` (ENUM: ANALGESICOS, ANTIBIOTICOS, etc.)
  - `requiere_receta` (BOOLEAN)
  - `proveedor_id` (FOREIGN KEY)

### 4. **lotes**

- **Propósito**: Control de lotes de medicamentos
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `numero_lote` (VARCHAR(50))
  - `fecha_entrada` (DATE)
  - `fecha_caducidad` (DATE)
  - `cantidad` (INTEGER)
  - `estado` (ENUM: ACTIVO, AGOTADO, CADUCADO)
  - `medicamento_id` (FOREIGN KEY)

### 5. **ventas**

- **Propósito**: Registrar transacciones de venta
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `numero_factura` (VARCHAR(20) UNIQUE)
  - `cliente_id` (FOREIGN KEY)
  - `subtotal` (DECIMAL(10,2))
  - `igv` (DECIMAL(10,2))
  - `total` (DECIMAL(10,2))
  - `metodo_pago` (ENUM: EFECTIVO, TARJETA_CREDITO, etc.)
  - `estado` (ENUM: PENDIENTE, PAGADA, CANCELADA, DEVUELTA)

### 6. **detalles_venta**

- **Propósito**: Detalles de productos en cada venta
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `venta_id` (FOREIGN KEY)
  - `medicamento_id` (FOREIGN KEY)
  - `cantidad` (INTEGER)
  - `precio_unitario` (DECIMAL(10,2))
  - `subtotal` (DECIMAL(10,2))
  - `igv` (DECIMAL(10,2))
  - `total` (DECIMAL(10,2))

### 7. **alertas**

- **Propósito**: Sistema de alertas automáticas
- **Campos principales**:
  - `id` (BIGSERIAL PRIMARY KEY)
  - `titulo` (VARCHAR(200))
  - `mensaje` (VARCHAR(1000))
  - `tipo` (ENUM: STOCK_BAJO, FECHA_CADUCIDAD, etc.)
  - `nivel` (ENUM: BAJA, MEDIA, ALTA, CRITICA)
  - `estado` (ENUM: PENDIENTE, EN_PROCESO, RESUELTA, DESCARTADA)
  - `medicamento_id` (FOREIGN KEY)

## 🔗 Relaciones entre Tablas

```
proveedores (1) ←→ (N) medicamentos
medicamentos (1) ←→ (N) lotes
medicamentos (1) ←→ (N) detalles_venta
medicamentos (1) ←→ (N) alertas
clientes (1) ←→ (N) ventas
ventas (1) ←→ (N) detalles_venta
```

## 📈 Datos de Prueba Incluidos

### Proveedores (5 registros)

- Farmindustria S.A.
- Genérico Pharma
- Nutripharma
- Bayer Perú
- Medicamentos Unidos

### Clientes (10 registros)

- Datos completos con nombres, emails, teléfonos y DNIs únicos
- Estados activos para todos los clientes

### Medicamentos (20 registros)

- **Categorías incluidas**:
  - Analgésicos (Paracetamol, Ibuprofeno, Aspirina)
  - Antibióticos (Amoxicilina, Ciprofloxacino)
  - Antiinflamatorios (Diclofenaco, Dexametasona, Prednisona)
  - Antihistamínicos (Loratadina, Cetirizina)
  - Vitaminas (Vitamina C, Vitamina B12)
  - Suplementos (Calcio + Vitamina D, Omega 3, Hierro)
  - Otros (Omeprazol, Lansoprazol, Metformina, Ranitidina)

### Ventas (10 registros)

- Facturas con números únicos
- Diferentes métodos de pago
- Estados pagados
- Fechas distribuidas en enero 2024

### Detalles de Venta (50+ registros)

- Múltiples productos por venta
- Cálculos correctos de IGV (18%)
- Observaciones descriptivas

### Alertas (10 registros)

- Diferentes tipos y niveles
- Estados pendientes
- Relacionadas con medicamentos específicos

### Lotes (10 registros)

- Números de lote únicos
- Fechas de entrada y caducidad
- Estados activos

## 🛠️ Funcionalidades del Sistema

### 1. **Gestión de Inventario**

- Control de stock en tiempo real
- Alertas automáticas de stock bajo
- Control de fechas de caducidad
- Gestión de lotes

### 2. **Gestión de Ventas**

- Registro de ventas con múltiples productos
- Cálculo automático de IGV
- Diferentes métodos de pago
- Estados de venta

### 3. **Gestión de Clientes**

- Registro completo de clientes
- Historial de compras
- Validación de datos únicos

### 4. **Gestión de Proveedores**

- Información completa de proveedores
- Relación con medicamentos
- Estados de proveedores

### 5. **Sistema de Alertas**

- Alertas automáticas por stock bajo
- Alertas por caducidad próxima
- Diferentes niveles de urgencia
- Seguimiento de resolución

### 6. **Reportes y Análisis**

- Ventas por período
- Productos más vendidos
- Análisis de inventario
- Reportes de rentabilidad

## 🔧 Configuración de Desarrollo

### Requisitos del Sistema

- **Java**: 17 o superior
- **PostgreSQL**: 12 o superior
- **Maven**: 3.6 o superior

### Pasos de Instalación

1. **Crear base de datos**:

   ```sql
   CREATE DATABASE farmacia_db;
   ```

2. **Configurar credenciales** en `application.properties`

3. **Ejecutar aplicación**:

   ```bash
   mvn spring-boot:run
   ```

4. **Verificar endpoints** en `http://localhost:8080/api`

### Endpoints Principales

- `GET /api/medicamentos` - Listar medicamentos
- `GET /api/clientes` - Listar clientes
- `GET /api/ventas` - Listar ventas
- `GET /api/alertas` - Listar alertas
- `POST /api/ventas` - Crear nueva venta
- `PUT /api/medicamentos/{id}` - Actualizar medicamento

## 📊 Consultas SQL Importantes

### Consultas de Inventario

```sql
-- Medicamentos con stock bajo
SELECT nombre, stock, stock_minimo FROM medicamentos
WHERE stock <= stock_minimo;

-- Productos próximos a caducar
SELECT nombre, fecha_caducidad FROM medicamentos
WHERE fecha_caducidad <= CURRENT_DATE + INTERVAL '30 days';
```

### Consultas de Ventas

```sql
-- Ventas del último mes
SELECT * FROM ventas
WHERE fecha_venta >= CURRENT_DATE - INTERVAL '1 month';

-- Productos más vendidos
SELECT m.nombre, SUM(dv.cantidad) as total_vendido
FROM detalles_venta dv
JOIN medicamentos m ON dv.medicamento_id = m.id
GROUP BY m.id, m.nombre
ORDER BY total_vendido DESC;
```

### Consultas de Alertas

```sql
-- Alertas pendientes críticas
SELECT * FROM alertas
WHERE estado = 'PENDIENTE' AND nivel = 'CRITICA'
ORDER BY fecha_creacion DESC;
```

## 🔒 Consideraciones de Seguridad

### Validaciones Implementadas

- **Emails únicos** para clientes
- **DNIs únicos** para clientes
- **Códigos de barras únicos** para medicamentos
- **Números de factura únicos**
- **RUCs únicos** para proveedores

### Restricciones de Datos

- **Stock no negativo**
- **Precios positivos**
- **Fechas válidas**
- **Longitudes de campos respetadas**

## 📈 Métricas y KPIs

### Métricas de Inventario

- **Rotación de inventario**
- **Stock promedio**
- **Productos con exceso de stock**
- **Productos con stock bajo**

### Métricas de Ventas

- **Ventas diarias/mensuales**
- **Ticket promedio**
- **Productos más vendidos**
- **Clientes más frecuentes**

### Métricas de Alertas

- **Tiempo de resolución**
- **Alertas por tipo**
- **Alertas críticas pendientes**

## 🚀 Optimizaciones Implementadas

### Índices de Base de Datos

- Índices en campos de búsqueda frecuente
- Índices en claves foráneas
- Índices compuestos para consultas complejas

### Triggers Automáticos

- Actualización automática de fechas
- Validaciones de integridad
- Cálculos automáticos

### Vistas Materializadas

- Resumen de inventario
- Resumen de ventas
- Métricas de rendimiento

## 📝 Notas Importantes

1. **Backup Regular**: Realizar backups diarios de la base de datos
2. **Monitoreo**: Configurar alertas de espacio en disco y rendimiento
3. **Actualizaciones**: Mantener PostgreSQL actualizado
4. **Seguridad**: Cambiar contraseñas por defecto
5. **Documentación**: Mantener documentación actualizada

## 🔄 Mantenimiento

### Tareas Diarias

- Revisar alertas pendientes
- Verificar stock bajo
- Monitorear productos próximos a caducar

### Tareas Semanales

- Generar reportes de ventas
- Analizar tendencias de productos
- Revisar métricas de rendimiento

### Tareas Mensuales

- Limpieza de datos antiguos
- Optimización de índices
- Actualización de estadísticas

---

**Desarrollado con Spring Boot y PostgreSQL**
**Versión**: 1.0.0
**Última actualización**: Enero 2024
