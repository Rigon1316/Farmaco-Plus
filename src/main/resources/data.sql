-- Datos de ejemplo para la base de datos de Farmacia
-- Este archivo se ejecutará automáticamente al iniciar la aplicación

-- =====================================================
-- DATOS DE CLIENTES
-- =====================================================

INSERT INTO clientes (nombre, apellido, email, telefono, direccion, dni, fecha_nacimiento, estado, fecha_creacion, fecha_actualizacion) VALUES
('María', 'González', 'maria.gonzalez@email.com', '9876543210', 'Av. Arequipa 123, Lima', '12345678', '1985-03-15 00:00:00', 'ACTIVO', NOW(), NOW()),
('Juan', 'Pérez', 'juan.perez@email.com', '9876543211', 'Jr. Tacna 456, Lima', '23456789', '1990-07-22 00:00:00', 'ACTIVO', NOW(), NOW()),
('Ana', 'Rodríguez', 'ana.rodriguez@email.com', '9876543212', 'Av. Brasil 789, Lima', '34567890', '1988-11-08 00:00:00', 'ACTIVO', NOW(), NOW()),
('Carlos', 'López', 'carlos.lopez@email.com', '9876543213', 'Jr. Huancavelica 321, Lima', '45678901', '1992-05-12 00:00:00', 'ACTIVO', NOW(), NOW()),
('Lucía', 'Martínez', 'lucia.martinez@email.com', '9876543214', 'Av. Venezuela 654, Lima', '56789012', '1987-09-30 00:00:00', 'ACTIVO', NOW(), NOW()),
('Roberto', 'Hernández', 'roberto.hernandez@email.com', '9876543215', 'Jr. Ayacucho 987, Lima', '67890123', '1995-01-18 00:00:00', 'ACTIVO', NOW(), NOW()),
('Carmen', 'Díaz', 'carmen.diaz@email.com', '9876543216', 'Av. Alfonso Ugarte 147, Lima', '78901234', '1983-12-03 00:00:00', 'ACTIVO', NOW(), NOW()),
('Fernando', 'Moreno', 'fernando.moreno@email.com', '9876543217', 'Jr. Cusco 258, Lima', '89012345', '1991-04-25 00:00:00', 'ACTIVO', NOW(), NOW()),
('Patricia', 'Jiménez', 'patricia.jimenez@email.com', '9876543218', 'Av. Grau 369, Lima', '90123456', '1989-08-14 00:00:00', 'ACTIVO', NOW(), NOW()),
('Miguel', 'Torres', 'miguel.torres@email.com', '9876543219', 'Jr. Puno 741, Lima', '01234567', '1993-06-07 00:00:00', 'ACTIVO', NOW(), NOW());

-- =====================================================
-- DATOS DE MEDICAMENTOS
-- =====================================================

INSERT INTO medicamentos (nombre, principio_activo, presentacion, concentracion, laboratorio, precio, stock, stock_minimo, fecha_caducidad, codigo_barras, descripcion, categoria, estado, requiere_receta, fecha_creacion, fecha_actualizacion) VALUES
('Paracetamol', 'Paracetamol', 'Tableta', '500mg', 'Genérico', 5.50, 150, 20, '2025-12-31 00:00:00', '1234567890123', 'Analgésico y antipirético', 'ANALGESICOS', 'ACTIVO', false, NOW(), NOW()),
('Ibuprofeno', 'Ibuprofeno', 'Tableta', '400mg', 'Genérico', 6.80, 120, 15, '2025-10-15 00:00:00', '1234567890124', 'Antiinflamatorio no esteroideo', 'ANTIINFLAMATORIOS', 'ACTIVO', false, NOW(), NOW()),
('Amoxicilina', 'Amoxicilina', 'Cápsula', '500mg', 'Farmindustria', 12.50, 80, 10, '2024-08-20 00:00:00', '1234567890125', 'Antibiótico de amplio espectro', 'ANTIBIOTICOS', 'ACTIVO', true, NOW(), NOW()),
('Loratadina', 'Loratadina', 'Tableta', '10mg', 'Genérico', 8.90, 95, 12, '2025-06-30 00:00:00', '1234567890126', 'Antihistamínico no sedante', 'ANTIHISTAMINICOS', 'ACTIVO', false, NOW(), NOW()),
('Omeprazol', 'Omeprazol', 'Cápsula', '20mg', 'Genérico', 15.75, 60, 8, '2025-09-15 00:00:00', '1234567890127', 'Protector gástrico', 'OTROS', 'ACTIVO', false, NOW(), NOW()),
('Vitamina C', 'Ácido Ascórbico', 'Tableta', '500mg', 'Nutripharma', 4.20, 200, 25, '2026-03-10 00:00:00', '1234567890128', 'Suplemento vitamínico', 'VITAMINAS', 'ACTIVO', false, NOW(), NOW()),
('Calcio + Vitamina D', 'Carbonato de Calcio + Colecalciferol', 'Tableta', '500mg + 200UI', 'Nutripharma', 18.90, 75, 10, '2025-11-20 00:00:00', '1234567890129', 'Suplemento de calcio', 'SUPLEMENTOS', 'ACTIVO', false, NOW(), NOW()),
('Dexametasona', 'Dexametasona', 'Tableta', '4mg', 'Farmindustria', 22.50, 45, 5, '2024-12-05 00:00:00', '1234567890130', 'Corticoide antiinflamatorio', 'ANTIINFLAMATORIOS', 'ACTIVO', true, NOW(), NOW()),
('Metformina', 'Metformina', 'Tableta', '500mg', 'Genérico', 9.80, 110, 15, '2025-07-25 00:00:00', '1234567890131', 'Antidiabético oral', 'OTROS', 'ACTIVO', true, NOW(), NOW()),
('Aspirina', 'Ácido Acetilsalicílico', 'Tableta', '100mg', 'Bayer', 7.50, 180, 20, '2025-05-18 00:00:00', '1234567890132', 'Antiagregante plaquetario', 'ANALGESICOS', 'ACTIVO', false, NOW(), NOW()),
('Cetirizina', 'Cetirizina', 'Tableta', '10mg', 'Genérico', 6.40, 130, 15, '2025-08-12 00:00:00', '1234567890133', 'Antihistamínico', 'ANTIHISTAMINICOS', 'ACTIVO', false, NOW(), NOW()),
('Lansoprazol', 'Lansoprazol', 'Cápsula', '30mg', 'Genérico', 16.80, 55, 8, '2025-10-30 00:00:00', '1234567890134', 'Protector gástrico', 'OTROS', 'ACTIVO', false, NOW(), NOW()),
('Vitamina B12', 'Cianocobalamina', 'Tableta', '1000mcg', 'Nutripharma', 12.30, 90, 12, '2026-01-15 00:00:00', '1234567890135', 'Suplemento de vitamina B12', 'VITAMINAS', 'ACTIVO', false, NOW(), NOW()),
('Diclofenaco', 'Diclofenaco', 'Tableta', '50mg', 'Genérico', 8.90, 100, 12, '2025-04-22 00:00:00', '1234567890136', 'Antiinflamatorio', 'ANTIINFLAMATORIOS', 'ACTIVO', false, NOW(), NOW()),
('Ciprofloxacino', 'Ciprofloxacino', 'Tableta', '500mg', 'Farmindustria', 14.20, 70, 8, '2024-09-30 00:00:00', '1234567890137', 'Antibiótico quinolona', 'ANTIBIOTICOS', 'ACTIVO', true, NOW(), NOW()),
('Omega 3', 'Ácidos Grasos Omega 3', 'Cápsula', '1000mg', 'Nutripharma', 25.60, 65, 10, '2026-02-28 00:00:00', '1234567890138', 'Suplemento de ácidos grasos', 'SUPLEMENTOS', 'ACTIVO', false, NOW(), NOW()),
('Ranitidina', 'Ranitidina', 'Tableta', '150mg', 'Genérico', 11.40, 85, 10, '2025-06-15 00:00:00', '1234567890139', 'Antiácido', 'OTROS', 'ACTIVO', false, NOW(), NOW()),
('Acetaminofén', 'Paracetamol', 'Jarabe', '120mg/5ml', 'Genérico', 8.90, 45, 8, '2025-03-20 00:00:00', '1234567890140', 'Analgésico pediátrico', 'ANALGESICOS', 'ACTIVO', false, NOW(), NOW()),
('Prednisona', 'Prednisona', 'Tableta', '5mg', 'Farmindustria', 19.80, 40, 5, '2024-11-10 00:00:00', '1234567890141', 'Corticoide', 'ANTIINFLAMATORIOS', 'ACTIVO', true, NOW(), NOW()),
('Hierro', 'Sulfato Ferroso', 'Tableta', '325mg', 'Nutripharma', 7.20, 120, 15, '2025-12-25 00:00:00', '1234567890142', 'Suplemento de hierro', 'SUPLEMENTOS', 'ACTIVO', false, NOW(), NOW()),
('Dextrometorfano', 'Dextrometorfano', 'Jarabe', '15mg/5ml', 'Genérico', 9.50, 60, 8, '2025-07-08 00:00:00', '1234567890143', 'Antitusivo', 'ANTITUSIVOS', 'ACTIVO', false, NOW(), NOW());

-- =====================================================
-- DATOS DE VENTAS
-- =====================================================

INSERT INTO ventas (numero_factura, cliente_id, subtotal, igv, total, metodo_pago, estado, observaciones, fecha_venta, fecha_actualizacion) VALUES
('F001-2024-001', 1, 45.80, 8.24, 54.04, 'EFECTIVO', 'PAGADA', 'Venta normal', '2024-01-15 10:30:00', NOW()),
('F001-2024-002', 2, 67.50, 12.15, 79.65, 'TARJETA_CREDITO', 'PAGADA', 'Pago con tarjeta', '2024-01-15 14:20:00', NOW()),
('F001-2024-003', 3, 23.40, 4.21, 27.61, 'EFECTIVO', 'PAGADA', 'Venta pequeña', '2024-01-16 09:15:00', NOW()),
('F001-2024-004', 4, 89.20, 16.06, 105.26, 'YAPE', 'PAGADA', 'Pago digital', '2024-01-16 16:45:00', NOW()),
('F001-2024-005', 5, 156.80, 28.22, 185.02, 'TARJETA_DEBITO', 'PAGADA', 'Compra grande', '2024-01-17 11:30:00', NOW()),
('F001-2024-006', 6, 34.60, 6.23, 40.83, 'EFECTIVO', 'PAGADA', 'Venta regular', '2024-01-17 15:20:00', NOW()),
('F001-2024-007', 7, 78.90, 14.20, 93.10, 'PLIN', 'PAGADA', 'Pago móvil', '2024-01-18 10:45:00', NOW()),
('F001-2024-008', 8, 45.30, 8.15, 53.45, 'EFECTIVO', 'PAGADA', 'Venta normal', '2024-01-18 13:30:00', NOW()),
('F001-2024-009', 9, 123.70, 22.27, 145.97, 'TRANSFERENCIA', 'PAGADA', 'Pago bancario', '2024-01-19 09:00:00', NOW()),
('F001-2024-010', 10, 67.80, 12.20, 80.00, 'EFECTIVO', 'PAGADA', 'Venta completa', '2024-01-19 17:15:00', NOW());

-- =====================================================
-- DATOS DE DETALLES DE VENTA
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
-- DATOS DE ALERTAS
-- =====================================================

INSERT INTO alertas (titulo, mensaje, tipo, nivel, estado, medicamento_id, fecha_resolucion, observaciones, fecha_creacion) VALUES
('Stock Bajo - Paracetamol', 'El medicamento Paracetamol tiene stock bajo (15 unidades)', 'STOCK_BAJO', 'MEDIA', 'PENDIENTE', 1, NULL, 'Revisar inventario', NOW()),
('Stock Bajo - Amoxicilina', 'El medicamento Amoxicilina tiene stock bajo (8 unidades)', 'STOCK_BAJO', 'ALTA', 'PENDIENTE', 3, NULL, 'Urgente reabastecer', NOW()),
('Caducidad Próxima - Dexametasona', 'El medicamento Dexametasona caduca en 30 días', 'FECHA_CADUCIDAD', 'ALTA', 'PENDIENTE', 8, NULL, 'Verificar stock', NOW()),
('Stock Bajo - Omeprazol', 'El medicamento Omeprazol tiene stock bajo (6 unidades)', 'STOCK_BAJO', 'MEDIA', 'PENDIENTE', 5, NULL, 'Revisar inventario', NOW()),
('Caducidad Próxima - Ciprofloxacino', 'El medicamento Ciprofloxacino caduca en 45 días', 'FECHA_CADUCIDAD', 'MEDIA', 'PENDIENTE', 15, NULL, 'Monitorear', NOW()),
('Stock Bajo - Prednisona', 'El medicamento Prednisona tiene stock bajo (4 unidades)', 'STOCK_BAJO', 'CRITICA', 'PENDIENTE', 19, NULL, 'Reabastecer inmediatamente', NOW()),
('Demanda Alta - Vitamina C', 'La Vitamina C tiene alta demanda', 'DEMANDA_ALTA', 'MEDIA', 'PENDIENTE', 6, NULL, 'Considerar aumentar stock', NOW()),
('Precio Alto - Omega 3', 'El Omega 3 tiene un precio alto comparado con la competencia', 'PRECIO_ALTO', 'BAJA', 'PENDIENTE', 16, NULL, 'Revisar precios', NOW()),
('Stock Bajo - Lansoprazol', 'El medicamento Lansoprazol tiene stock bajo (5 unidades)', 'STOCK_BAJO', 'MEDIA', 'PENDIENTE', 12, NULL, 'Revisar inventario', NOW()),
('Caducidad Próxima - Metformina', 'El medicamento Metformina caduca en 60 días', 'FECHA_CADUCIDAD', 'BAJA', 'PENDIENTE', 9, NULL, 'Monitorear', NOW()); 