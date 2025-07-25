-- =====================================================
-- SISTEMA DE GESTIÓN DE FARMACIA - SCRIPTS SQL COMPLETOS
-- =====================================================
-- Base de datos: PostgreSQL
-- Aplicación: Spring Boot con JPA/Hibernate
-- =====================================================

-- =====================================================
-- 1. CREACIÓN DE LA BASE DE DATOS
-- =====================================================

-- Crear la base de datos (ejecutar como superusuario)
-- CREATE DATABASE farmacia_db;
-- CREATE USER farmacia_user WITH PASSWORD 'farmacia123';
-- GRANT ALL PRIVILEGES ON DATABASE farmacia_db TO farmacia_user;

-- =====================================================
-- 2. ESTRUCTURA DE TABLAS (DDL)
-- =====================================================

-- Tabla de Proveedores
CREATE TABLE IF NOT EXISTS proveedores (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    contacto VARCHAR(100) NOT NULL,
    email VARCHAR(150),
    ruc VARCHAR(11) UNIQUE,
    direccion VARCHAR(200),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_actualizacion DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Tabla de Clientes
CREATE TABLE IF NOT EXISTS clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(10) NOT NULL,
    direccion VARCHAR(200),
    dni VARCHAR(10) UNIQUE,
    fecha_nacimiento DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_actualizacion DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Tabla de Medicamentos
CREATE TABLE IF NOT EXISTS medicamentos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL UNIQUE,
    principio_activo VARCHAR(200) NOT NULL,
    presentacion VARCHAR(100) NOT NULL,
    concentracion VARCHAR(50) NOT NULL,
    laboratorio VARCHAR(150) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER NOT NULL DEFAULT 0,
    fecha_caducidad DATE NOT NULL,
    codigo_barras VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(500),
    categoria VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    requiere_receta BOOLEAN NOT NULL DEFAULT false,
    proveedor_id BIGINT REFERENCES proveedores(id),
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_actualizacion DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Tabla de Lotes
CREATE TABLE IF NOT EXISTS lotes (
    id BIGSERIAL PRIMARY KEY,
    numero_lote VARCHAR(50) NOT NULL,
    fecha_entrada DATE NOT NULL,
    fecha_caducidad DATE NOT NULL,
    cantidad INTEGER NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    medicamento_id BIGINT NOT NULL REFERENCES medicamentos(id),
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_actualizacion DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Tabla de Ventas
CREATE TABLE IF NOT EXISTS ventas (
    id BIGSERIAL PRIMARY KEY,
    numero_factura VARCHAR(20) NOT NULL UNIQUE,
    cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    subtotal DECIMAL(10,2) NOT NULL,
    igv DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    observaciones VARCHAR(500),
    fecha_venta DATE NOT NULL DEFAULT CURRENT_DATE,
    fecha_actualizacion DATE NOT NULL DEFAULT CURRENT_DATE
);

-- Tabla de Detalles de Venta
CREATE TABLE IF NOT EXISTS detalles_venta (
    id BIGSERIAL PRIMARY KEY,
    venta_id BIGINT NOT NULL REFERENCES ventas(id),
    medicamento_id BIGINT NOT NULL REFERENCES medicamentos(id),
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    igv DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    observaciones VARCHAR(500)
);

-- Tabla de Alertas
CREATE TABLE IF NOT EXISTS alertas (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    mensaje VARCHAR(1000) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    nivel VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE',
    medicamento_id BIGINT REFERENCES medicamentos(id),
    fecha_resolucion DATE,
    observaciones VARCHAR(500),
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE
);

-- =====================================================
-- 3. ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

-- Índices para búsquedas frecuentes
CREATE INDEX idx_medicamentos_nombre ON medicamentos(nombre);
CREATE INDEX idx_medicamentos_categoria ON medicamentos(categoria);
CREATE INDEX idx_medicamentos_stock ON medicamentos(stock);
CREATE INDEX idx_medicamentos_fecha_caducidad ON medicamentos(fecha_caducidad);
CREATE INDEX idx_medicamentos_codigo_barras ON medicamentos(codigo_barras);

CREATE INDEX idx_clientes_email ON clientes(email);
CREATE INDEX idx_clientes_dni ON clientes(dni);
CREATE INDEX idx_clientes_nombre_apellido ON clientes(nombre, apellido);

CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_ventas_numero_factura ON ventas(numero_factura);

CREATE INDEX idx_alertas_estado ON alertas(estado);
CREATE INDEX idx_alertas_tipo ON alertas(tipo);
CREATE INDEX idx_alertas_medicamento ON alertas(medicamento_id);

-- =====================================================
-- 4. DATOS DE PRUEBA - PROVEEDORES
-- =====================================================

INSERT INTO proveedores (nombre, contacto, email, ruc, direccion, estado) VALUES
('Farmindustria S.A.', 'Juan Pérez', 'contacto@farmindustria.com', '20123456789', 'Av. Industrial 123, Lima', 'ACTIVO'),
('Genérico Pharma', 'María López', 'ventas@generico.com', '20234567890', 'Jr. Comercial 456, Lima', 'ACTIVO'),
('Nutripharma', 'Carlos Rodríguez', 'info@nutripharma.com', '20345678901', 'Av. Salud 789, Lima', 'ACTIVO'),
('Bayer Perú', 'Ana Martínez', 'peru@bayer.com', '20456789012', 'Av. Corporativa 321, Lima', 'ACTIVO'),
('Medicamentos Unidos', 'Roberto Silva', 'contacto@medunidos.com', '20567890123', 'Jr. Farmacéutico 654, Lima', 'ACTIVO');

-- =====================================================
-- 5. DATOS DE PRUEBA - CLIENTES
-- =====================================================

INSERT INTO clientes (nombre, apellido, email, telefono, direccion, dni, fecha_nacimiento, estado) VALUES
('María', 'González', 'maria.gonzalez@email.com', '9876543210', 'Av. Arequipa 123, Lima', '12345678', '1985-03-15', 'ACTIVO'),
('Juan', 'Pérez', 'juan.perez@email.com', '9876543211', 'Jr. Tacna 456, Lima', '23456789', '1990-07-22', 'ACTIVO'),
('Ana', 'Rodríguez', 'ana.rodriguez@email.com', '9876543212', 'Av. Brasil 789, Lima', '34567890', '1988-11-08', 'ACTIVO'),
('Carlos', 'López', 'carlos.lopez@email.com', '9876543213', 'Jr. Huancavelica 321, Lima', '45678901', '1992-05-12', 'ACTIVO'),
('Lucía', 'Martínez', 'lucia.martinez@email.com', '9876543214', 'Av. Venezuela 654, Lima', '56789012', '1987-09-30', 'ACTIVO'),
('Roberto', 'Hernández', 'roberto.hernandez@email.com', '9876543215', 'Jr. Ayacucho 987, Lima', '67890123', '1995-01-18', 'ACTIVO'),
('Carmen', 'Díaz', 'carmen.diaz@email.com', '9876543216', 'Av. Alfonso Ugarte 147, Lima', '78901234', '1983-12-03', 'ACTIVO'),
('Fernando', 'Moreno', 'fernando.moreno@email.com', '9876543217', 'Jr. Cusco 258, Lima', '89012345', '1991-04-25', 'ACTIVO'),
('Patricia', 'Jiménez', 'patricia.jimenez@email.com', '9876543218', 'Av. Grau 369, Lima', '90123456', '1989-08-14', 'ACTIVO'),
('Miguel', 'Torres', 'miguel.torres@email.com', '9876543219', 'Jr. Puno 741, Lima', '01234567', '1993-06-07', 'ACTIVO');

-- =====================================================
-- 6. DATOS DE PRUEBA - MEDICAMENTOS
-- =====================================================

INSERT INTO medicamentos (nombre, principio_activo, presentacion, concentracion, laboratorio, precio, stock, stock_minimo, fecha_caducidad, codigo_barras, descripcion, categoria, estado, requiere_receta, proveedor_id) VALUES
('Paracetamol', 'Paracetamol', 'Tableta', '500mg', 'Genérico', 5.50, 150, 20, '2025-12-31', '1234567890123', 'Analgésico y antipirético', 'ANALGESICOS', 'ACTIVO', false, 2),
('Ibuprofeno', 'Ibuprofeno', 'Tableta', '400mg', 'Genérico', 6.80, 120, 15, '2025-10-15', '1234567890124', 'Antiinflamatorio no esteroideo', 'ANTIINFLAMATORIOS', 'ACTIVO', false, 2),
('Amoxicilina', 'Amoxicilina', 'Cápsula', '500mg', 'Farmindustria', 12.50, 80, 10, '2024-08-20', '1234567890125', 'Antibiótico de amplio espectro', 'ANTIBIOTICOS', 'ACTIVO', true, 1),
('Loratadina', 'Loratadina', 'Tableta', '10mg', 'Genérico', 8.90, 95, 12, '2025-06-30', '1234567890126', 'Antihistamínico no sedante', 'ANTIHISTAMINICOS', 'ACTIVO', false, 2),
('Omeprazol', 'Omeprazol', 'Cápsula', '20mg', 'Genérico', 15.75, 60, 8, '2025-09-15', '1234567890127', 'Protector gástrico', 'OTROS', 'ACTIVO', false, 2),
('Vitamina C', 'Ácido Ascórbico', 'Tableta', '500mg', 'Nutripharma', 4.20, 200, 25, '2026-03-10', '1234567890128', 'Suplemento vitamínico', 'VITAMINAS', 'ACTIVO', false, 3),
('Calcio + Vitamina D', 'Carbonato de Calcio + Colecalciferol', 'Tableta', '500mg + 200UI', 'Nutripharma', 18.90, 75, 10, '2025-11-20', '1234567890129', 'Suplemento de calcio', 'SUPLEMENTOS', 'ACTIVO', false, 3),
('Dexametasona', 'Dexametasona', 'Tableta', '4mg', 'Farmindustria', 22.50, 45, 5, '2024-12-05', '1234567890130', 'Corticoide antiinflamatorio', 'ANTIINFLAMATORIOS', 'ACTIVO', true, 1),
('Metformina', 'Metformina', 'Tableta', '500mg', 'Genérico', 9.80, 110, 15, '2025-07-25', '1234567890131', 'Antidiabético oral', 'OTROS', 'ACTIVO', true, 2),
('Aspirina', 'Ácido Acetilsalicílico', 'Tableta', '100mg', 'Bayer', 7.50, 180, 20, '2025-05-18', '1234567890132', 'Antiagregante plaquetario', 'ANALGESICOS', 'ACTIVO', false, 4),
('Cetirizina', 'Cetirizina', 'Tableta', '10mg', 'Genérico', 6.40, 130, 15, '2025-08-12', '1234567890133', 'Antihistamínico', 'ANTIHISTAMINICOS', 'ACTIVO', false, 2),
('Lansoprazol', 'Lansoprazol', 'Cápsula', '30mg', 'Genérico', 16.80, 55, 8, '2025-10-30', '1234567890134', 'Protector gástrico', 'OTROS', 'ACTIVO', false, 2),
('Vitamina B12', 'Cianocobalamina', 'Tableta', '1000mcg', 'Nutripharma', 12.30, 90, 12, '2026-01-15', '1234567890135', 'Suplemento de vitamina B12', 'VITAMINAS', 'ACTIVO', false, 3),
('Diclofenaco', 'Diclofenaco', 'Tableta', '50mg', 'Genérico', 8.90, 100, 12, '2025-04-22', '1234567890136', 'Antiinflamatorio', 'ANTIINFLAMATORIOS', 'ACTIVO', false, 2),
('Ciprofloxacino', 'Ciprofloxacino', 'Tableta', '500mg', 'Farmindustria', 14.20, 70, 8, '2024-09-30', '1234567890137', 'Antibiótico quinolona', 'ANTIBIOTICOS', 'ACTIVO', true, 1),
('Omega 3', 'Ácidos Grasos Omega 3', 'Cápsula', '1000mg', 'Nutripharma', 25.60, 65, 10, '2026-02-28', '1234567890138', 'Suplemento de ácidos grasos', 'SUPLEMENTOS', 'ACTIVO', false, 3),
('Ranitidina', 'Ranitidina', 'Tableta', '150mg', 'Genérico', 11.40, 85, 10, '2025-06-15', '1234567890139', 'Antiácido', 'OTROS', 'ACTIVO', false, 2),
('Acetaminofén', 'Paracetamol', 'Jarabe', '120mg/5ml', 'Genérico', 8.90, 45, 8, '2025-03-20', '1234567890140', 'Analgésico pediátrico', 'ANALGESICOS', 'ACTIVO', false, 2),
('Prednisona', 'Prednisona', 'Tableta', '5mg', 'Farmindustria', 19.80, 40, 5, '2024-11-10', '1234567890141', 'Corticoide', 'ANTIINFLAMATORIOS', 'ACTIVO', true, 1),
('Hierro', 'Sulfato Ferroso', 'Tableta', '325mg', 'Nutripharma', 7.20, 120, 15, '2025-12-25', '1234567890142', 'Suplemento de hierro', 'SUPLEMENTOS', 'ACTIVO', false, 3),
('Dextrometorfano', 'Dextrometorfano', 'Jarabe', '15mg/5ml', 'Genérico', 9.50, 60, 8, '2025-07-08', '1234567890143', 'Antitusivo', 'ANTITUSIVOS', 'ACTIVO', false, 2);

-- =====================================================
-- 7. DATOS DE PRUEBA - LOTES
-- =====================================================

INSERT INTO lotes (numero_lote, fecha_entrada, fecha_caducidad, cantidad, estado, medicamento_id) VALUES
('LOT001-2024', '2024-01-01', '2025-12-31', 150, 'ACTIVO', 1),
('LOT002-2024', '2024-01-05', '2025-10-15', 120, 'ACTIVO', 2),
('LOT003-2024', '2024-01-10', '2024-08-20', 80, 'ACTIVO', 3),
('LOT004-2024', '2024-01-15', '2025-06-30', 95, 'ACTIVO', 4),
('LOT005-2024', '2024-01-20', '2025-09-15', 60, 'ACTIVO', 5),
('LOT006-2024', '2024-02-01', '2026-03-10', 200, 'ACTIVO', 6),
('LOT007-2024', '2024-02-05', '2025-11-20', 75, 'ACTIVO', 7),
('LOT008-2024', '2024-02-10', '2024-12-05', 45, 'ACTIVO', 8),
('LOT009-2024', '2024-02-15', '2025-07-25', 110, 'ACTIVO', 9),
('LOT010-2024', '2024-02-20', '2025-05-18', 180, 'ACTIVO', 10);

-- =====================================================
-- 8. DATOS DE PRUEBA - VENTAS
-- =====================================================

INSERT INTO ventas (numero_factura, cliente_id, subtotal, igv, total, metodo_pago, estado, observaciones, fecha_venta) VALUES
('F001-2024-001', 1, 45.80, 8.24, 54.04, 'EFECTIVO', 'PAGADA', 'Venta normal', '2024-01-15'),
('F001-2024-002', 2, 67.50, 12.15, 79.65, 'TARJETA_CREDITO', 'PAGADA', 'Pago con tarjeta', '2024-01-15'),
('F001-2024-003', 3, 23.40, 4.21, 27.61, 'EFECTIVO', 'PAGADA', 'Venta pequeña', '2024-01-16'),
('F001-2024-004', 4, 89.20, 16.06, 105.26, 'YAPE', 'PAGADA', 'Pago digital', '2024-01-16'),
('F001-2024-005', 5, 156.80, 28.22, 185.02, 'TARJETA_DEBITO', 'PAGADA', 'Compra grande', '2024-01-17'),
('F001-2024-006', 6, 34.60, 6.23, 40.83, 'EFECTIVO', 'PAGADA', 'Venta regular', '2024-01-17'),
('F001-2024-007', 7, 78.90, 14.20, 93.10, 'PLIN', 'PAGADA', 'Pago móvil', '2024-01-18'),
('F001-2024-008', 8, 45.30, 8.15, 53.45, 'EFECTIVO', 'PAGADA', 'Venta normal', '2024-01-18'),
('F001-2024-009', 9, 123.70, 22.27, 145.97, 'TRANSFERENCIA', 'PAGADA', 'Pago bancario', '2024-01-19'),
('F001-2024-010', 10, 67.80, 12.20, 80.00, 'EFECTIVO', 'PAGADA', 'Venta completa', '2024-01-19');

-- =====================================================
-- 9. DATOS DE PRUEBA - DETALLES DE VENTA
-- =====================================================

INSERT INTO detalles_venta (venta_id, medicamento_id, cantidad, precio_unitario, subtotal, igv, total, observaciones) VALUES
-- Detalles para Venta 1
(1, 1, 3, 5.50, 16.50, 2.97, 19.47, 'Paracetamol 500mg'),
(1, 2, 2, 6.80, 13.60, 2.45, 16.05, 'Ibuprofeno 400mg'),
(1, 6, 2, 4.20, 8.40, 1.51, 9.91, 'Vitamina C'),
(1, 11, 1, 6.40, 6.40, 1.15, 7.55, 'Cetirizina'),
(1, 18, 1, 8.90, 8.90, 1.60, 10.50, 'Acetaminofén jarabe'),

-- Detalles para Venta 2
(2, 3, 2, 12.50, 25.00, 4.50, 29.50, 'Amoxicilina 500mg'),
(2, 5, 1, 15.75, 15.75, 2.84, 18.59, 'Omeprazol 20mg'),
(2, 7, 1, 18.90, 18.90, 3.40, 22.30, 'Calcio + Vitamina D'),
(2, 12, 1, 16.80, 16.80, 3.02, 19.82, 'Lansoprazol'),
(2, 15, 1, 14.20, 14.20, 2.56, 16.76, 'Ciprofloxacino'),

-- Detalles para Venta 3
(3, 1, 2, 5.50, 11.00, 1.98, 12.98, 'Paracetamol'),
(3, 6, 1, 4.20, 4.20, 0.76, 4.96, 'Vitamina C'),
(3, 11, 1, 6.40, 6.40, 1.15, 7.55, 'Cetirizina'),
(3, 20, 1, 7.20, 7.20, 1.30, 8.50, 'Hierro'),

-- Detalles para Venta 4
(4, 8, 1, 22.50, 22.50, 4.05, 26.55, 'Dexametasona'),
(4, 9, 2, 9.80, 19.60, 3.53, 23.13, 'Metformina'),
(4, 13, 1, 12.30, 12.30, 2.21, 14.51, 'Vitamina B12'),
(4, 16, 1, 25.60, 25.60, 4.61, 30.21, 'Omega 3'),
(4, 19, 1, 19.80, 19.80, 3.56, 23.36, 'Prednisona'),

-- Detalles para Venta 5
(5, 3, 3, 12.50, 37.50, 6.75, 44.25, 'Amoxicilina'),
(5, 5, 2, 15.75, 31.50, 5.67, 37.17, 'Omeprazol'),
(5, 7, 2, 18.90, 37.80, 6.80, 44.60, 'Calcio + Vitamina D'),
(5, 10, 1, 7.50, 7.50, 1.35, 8.85, 'Aspirina'),
(5, 14, 2, 8.90, 17.80, 3.20, 21.00, 'Diclofenaco'),
(5, 16, 1, 25.60, 25.60, 4.61, 30.21, 'Omega 3'),

-- Detalles para Venta 6
(6, 1, 1, 5.50, 5.50, 0.99, 6.49, 'Paracetamol'),
(6, 2, 1, 6.80, 6.80, 1.22, 8.02, 'Ibuprofeno'),
(6, 6, 2, 4.20, 8.40, 1.51, 9.91, 'Vitamina C'),
(6, 11, 1, 6.40, 6.40, 1.15, 7.55, 'Cetirizina'),
(6, 17, 1, 11.40, 11.40, 2.05, 13.45, 'Ranitidina'),

-- Detalles para Venta 7
(7, 8, 1, 22.50, 22.50, 4.05, 26.55, 'Dexametasona'),
(7, 9, 1, 9.80, 9.80, 1.76, 11.56, 'Metformina'),
(7, 13, 1, 12.30, 12.30, 2.21, 14.51, 'Vitamina B12'),
(7, 15, 1, 14.20, 14.20, 2.56, 16.76, 'Ciprofloxacino'),
(7, 19, 1, 19.80, 19.80, 3.56, 23.36, 'Prednisona'),

-- Detalles para Venta 8
(8, 1, 2, 5.50, 11.00, 1.98, 12.98, 'Paracetamol'),
(8, 2, 1, 6.80, 6.80, 1.22, 8.02, 'Ibuprofeno'),
(8, 6, 1, 4.20, 4.20, 0.76, 4.96, 'Vitamina C'),
(8, 10, 1, 7.50, 7.50, 1.35, 8.85, 'Aspirina'),
(8, 12, 1, 16.80, 16.80, 3.02, 19.82, 'Lansoprazol'),

-- Detalles para Venta 9
(9, 3, 2, 12.50, 25.00, 4.50, 29.50, 'Amoxicilina'),
(9, 5, 1, 15.75, 15.75, 2.84, 18.59, 'Omeprazol'),
(9, 7, 1, 18.90, 18.90, 3.40, 22.30, 'Calcio + Vitamina D'),
(9, 13, 1, 12.30, 12.30, 2.21, 14.51, 'Vitamina B12'),
(9, 16, 1, 25.60, 25.60, 4.61, 30.21, 'Omega 3'),
(9, 20, 1, 7.20, 7.20, 1.30, 8.50, 'Hierro'),
(9, 14, 1, 8.90, 8.90, 1.60, 10.50, 'Diclofenaco'),

-- Detalles para Venta 10
(10, 1, 3, 5.50, 16.50, 2.97, 19.47, 'Paracetamol'),
(10, 2, 2, 6.80, 13.60, 2.45, 16.05, 'Ibuprofeno'),
(10, 6, 2, 4.20, 8.40, 1.51, 9.91, 'Vitamina C'),
(10, 11, 1, 6.40, 6.40, 1.15, 7.55, 'Cetirizina'),
(10, 17, 1, 11.40, 11.40, 2.05, 13.45, 'Ranitidina'),
(10, 18, 1, 8.90, 8.90, 1.60, 10.50, 'Acetaminofén jarabe');

-- =====================================================
-- 10. DATOS DE PRUEBA - ALERTAS
-- =====================================================

INSERT INTO alertas (titulo, mensaje, tipo, nivel, estado, medicamento_id, observaciones) VALUES
('Stock Bajo - Paracetamol', 'El medicamento Paracetamol tiene stock bajo (15 unidades)', 'STOCK_BAJO', 'MEDIA', 'PENDIENTE', 1, 'Revisar inventario'),
('Stock Bajo - Amoxicilina', 'El medicamento Amoxicilina tiene stock bajo (8 unidades)', 'STOCK_BAJO', 'ALTA', 'PENDIENTE', 3, 'Urgente reabastecer'),
('Caducidad Próxima - Dexametasona', 'El medicamento Dexametasona caduca en 30 días', 'FECHA_CADUCIDAD', 'ALTA', 'PENDIENTE', 8, 'Verificar stock'),
('Stock Bajo - Omeprazol', 'El medicamento Omeprazol tiene stock bajo (6 unidades)', 'STOCK_BAJO', 'MEDIA', 'PENDIENTE', 5, 'Revisar inventario'),
('Caducidad Próxima - Ciprofloxacino', 'El medicamento Ciprofloxacino caduca en 45 días', 'FECHA_CADUCIDAD', 'MEDIA', 'PENDIENTE', 15, 'Monitorear'),
('Stock Bajo - Prednisona', 'El medicamento Prednisona tiene stock bajo (4 unidades)', 'STOCK_BAJO', 'CRITICA', 'PENDIENTE', 19, 'Reabastecer inmediatamente'),
('Demanda Alta - Vitamina C', 'La Vitamina C tiene alta demanda', 'DEMANDA_ALTA', 'MEDIA', 'PENDIENTE', 6, 'Considerar aumentar stock'),
('Precio Alto - Omega 3', 'El Omega 3 tiene un precio alto comparado con la competencia', 'PRECIO_ALTO', 'BAJA', 'PENDIENTE', 16, 'Revisar precios'),
('Stock Bajo - Lansoprazol', 'El medicamento Lansoprazol tiene stock bajo (5 unidades)', 'STOCK_BAJO', 'MEDIA', 'PENDIENTE', 12, 'Revisar inventario'),
('Caducidad Próxima - Metformina', 'El medicamento Metformina caduca en 60 días', 'FECHA_CADUCIDAD', 'BAJA', 'PENDIENTE', 9, 'Monitorear');

-- =====================================================
-- 11. CONSULTAS ÚTILES PARA ANÁLISIS
-- =====================================================

-- Consulta para ver medicamentos con stock bajo
SELECT 
    m.nombre,
    m.stock,
    m.stock_minimo,
    m.precio,
    m.fecha_caducidad,
    p.nombre as proveedor
FROM medicamentos m
LEFT JOIN proveedores p ON m.proveedor_id = p.id
WHERE m.stock <= m.stock_minimo
ORDER BY m.stock ASC;

-- Consulta para ver medicamentos próximos a caducar (30 días)
SELECT 
    m.nombre,
    m.fecha_caducidad,
    m.stock,
    m.precio,
    p.nombre as proveedor
FROM medicamentos m
LEFT JOIN proveedores p ON m.proveedor_id = p.id
WHERE m.fecha_caducidad <= CURRENT_DATE + INTERVAL '30 days'
ORDER BY m.fecha_caducidad ASC;

-- Consulta para ver ventas por período
SELECT 
    v.numero_factura,
    c.nombre || ' ' || c.apellido as cliente,
    v.subtotal,
    v.igv,
    v.total,
    v.metodo_pago,
    v.fecha_venta
FROM ventas v
JOIN clientes c ON v.cliente_id = c.id
WHERE v.fecha_venta BETWEEN '2024-01-01' AND '2024-12-31'
ORDER BY v.fecha_venta DESC;

-- Consulta para ver productos más vendidos
SELECT 
    m.nombre,
    SUM(dv.cantidad) as total_vendido,
    SUM(dv.total) as ingresos_totales
FROM detalles_venta dv
JOIN medicamentos m ON dv.medicamento_id = m.id
GROUP BY m.id, m.nombre
ORDER BY total_vendido DESC
LIMIT 10;

-- Consulta para ver alertas pendientes
SELECT 
    a.titulo,
    a.mensaje,
    a.tipo,
    a.nivel,
    a.fecha_creacion,
    m.nombre as medicamento
FROM alertas a
LEFT JOIN medicamentos m ON a.medicamento_id = m.id
WHERE a.estado = 'PENDIENTE'
ORDER BY 
    CASE a.nivel 
        WHEN 'CRITICA' THEN 1 
        WHEN 'ALTA' THEN 2 
        WHEN 'MEDIA' THEN 3 
        WHEN 'BAJA' THEN 4 
    END,
    a.fecha_creacion DESC;

-- =====================================================
-- 12. PROCEDIMIENTOS ALMACENADOS ÚTILES
-- =====================================================

-- Procedimiento para actualizar stock después de una venta
CREATE OR REPLACE FUNCTION actualizar_stock_venta(
    p_medicamento_id BIGINT,
    p_cantidad INTEGER
) RETURNS BOOLEAN AS $$
BEGIN
    UPDATE medicamentos 
    SET stock = stock - p_cantidad,
        fecha_actualizacion = CURRENT_DATE
    WHERE id = p_medicamento_id AND stock >= p_cantidad;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- Procedimiento para verificar y crear alertas de stock bajo
CREATE OR REPLACE FUNCTION verificar_alertas_stock() RETURNS VOID AS $$
BEGIN
    INSERT INTO alertas (titulo, mensaje, tipo, nivel, estado, medicamento_id, observaciones)
    SELECT 
        'Stock Bajo - ' || m.nombre,
        'El medicamento ' || m.nombre || ' tiene stock bajo (' || m.stock || ' unidades)',
        'STOCK_BAJO',
        CASE 
            WHEN m.stock = 0 THEN 'CRITICA'
            WHEN m.stock <= m.stock_minimo/2 THEN 'ALTA'
            ELSE 'MEDIA'
        END,
        'PENDIENTE',
        m.id,
        'Revisar inventario'
    FROM medicamentos m
    WHERE m.stock <= m.stock_minimo
    AND m.estado = 'ACTIVO'
    AND NOT EXISTS (
        SELECT 1 FROM alertas a 
        WHERE a.medicamento_id = m.id 
        AND a.tipo = 'STOCK_BAJO' 
        AND a.estado = 'PENDIENTE'
    );
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- 13. VISTAS ÚTILES
-- =====================================================

-- Vista para resumen de inventario
CREATE OR REPLACE VIEW v_inventario_resumen AS
SELECT 
    m.id,
    m.nombre,
    m.categoria,
    m.stock,
    m.stock_minimo,
    m.precio,
    m.fecha_caducidad,
    p.nombre as proveedor,
    CASE 
        WHEN m.stock = 0 THEN 'AGOTADO'
        WHEN m.stock <= m.stock_minimo THEN 'STOCK_BAJO'
        WHEN m.fecha_caducidad <= CURRENT_DATE + INTERVAL '30 days' THEN 'PRÓXIMO_CADUCAR'
        ELSE 'NORMAL'
    END as estado_inventario
FROM medicamentos m
LEFT JOIN proveedores p ON m.proveedor_id = p.id
WHERE m.estado = 'ACTIVO';

-- Vista para resumen de ventas
CREATE OR REPLACE VIEW v_ventas_resumen AS
SELECT 
    v.id,
    v.numero_factura,
    c.nombre || ' ' || c.apellido as cliente,
    v.subtotal,
    v.igv,
    v.total,
    v.metodo_pago,
    v.estado,
    v.fecha_venta,
    COUNT(dv.id) as cantidad_productos
FROM ventas v
JOIN clientes c ON v.cliente_id = c.id
LEFT JOIN detalles_venta dv ON v.id = dv.venta_id
GROUP BY v.id, v.numero_factura, c.nombre, c.apellido, v.subtotal, v.igv, v.total, v.metodo_pago, v.estado, v.fecha_venta;

-- =====================================================
-- 14. TRIGGERS ÚTILES
-- =====================================================

-- Trigger para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION actualizar_fecha_modificacion()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_DATE;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger a todas las tablas que tienen fecha_actualizacion
CREATE TRIGGER trigger_actualizar_fecha_medicamentos
    BEFORE UPDATE ON medicamentos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();

CREATE TRIGGER trigger_actualizar_fecha_clientes
    BEFORE UPDATE ON clientes
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();

CREATE TRIGGER trigger_actualizar_fecha_proveedores
    BEFORE UPDATE ON proveedores
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_modificacion();

-- =====================================================
-- 15. CONFIGURACIÓN DE PERMISOS
-- =====================================================

-- Crear usuario específico para la aplicación
-- CREATE USER farmacia_app WITH PASSWORD 'farmacia_app_123';

-- Otorgar permisos necesarios
-- GRANT CONNECT ON DATABASE farmacia_db TO farmacia_app;
-- GRANT USAGE ON SCHEMA public TO farmacia_app;
-- GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO farmacia_app;
-- GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO farmacia_app;

-- =====================================================
-- FIN DEL SCRIPT
-- =====================================================
-- 
-- INSTRUCCIONES DE USO:
-- 1. Ejecutar este script como superusuario de PostgreSQL
-- 2. Asegurarse de que la base de datos 'farmacia_db' existe
-- 3. Verificar que las credenciales en application.properties coincidan
-- 4. Ejecutar las consultas de verificación al final
-- 5. Configurar los permisos de usuario según sea necesario
--
-- NOTAS:
-- - Los datos de prueba incluyen 5 proveedores, 10 clientes, 20 medicamentos
-- - Se incluyen 10 ventas con sus respectivos detalles
-- - Se incluyen 10 alertas de ejemplo
-- - Se incluyen 10 lotes de medicamentos
-- - Todas las fechas están configuradas para 2024-2025
-- ===================================================== 