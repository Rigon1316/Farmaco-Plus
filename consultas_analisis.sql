-- =====================================================
-- CONSULTAS SQL PARA ANÁLISIS Y REPORTES - SISTEMA DE FARMACIA
-- =====================================================

-- =====================================================
-- 1. CONSULTAS DE INVENTARIO
-- =====================================================

-- 1.1 Medicamentos con stock bajo
SELECT 
    m.nombre,
    m.stock,
    m.stock_minimo,
    m.precio,
    m.fecha_caducidad,
    p.nombre as proveedor,
    CASE 
        WHEN m.stock = 0 THEN 'AGOTADO'
        WHEN m.stock <= m.stock_minimo/2 THEN 'CRÍTICO'
        ELSE 'BAJO'
    END as nivel_urgencia
FROM medicamentos m
LEFT JOIN proveedores p ON m.proveedor_id = p.id
WHERE m.stock <= m.stock_minimo
ORDER BY m.stock ASC;

-- 1.2 Medicamentos próximos a caducar (30 días)
SELECT 
    m.nombre,
    m.fecha_caducidad,
    m.stock,
    m.precio,
    p.nombre as proveedor,
    m.fecha_caducidad - CURRENT_DATE as dias_restantes
FROM medicamentos m
LEFT JOIN proveedores p ON m.proveedor_id = p.id
WHERE m.fecha_caducidad <= CURRENT_DATE + INTERVAL '30 days'
AND m.stock > 0
ORDER BY m.fecha_caducidad ASC;

-- 1.3 Valor total del inventario por categoría
SELECT 
    m.categoria,
    COUNT(*) as cantidad_productos,
    SUM(m.stock) as stock_total,
    SUM(m.stock * m.precio) as valor_inventario,
    AVG(m.precio) as precio_promedio
FROM medicamentos m
WHERE m.estado = 'ACTIVO'
GROUP BY m.categoria
ORDER BY valor_inventario DESC;

-- 1.4 Medicamentos que requieren receta
SELECT 
    m.nombre,
    m.principio_activo,
    m.precio,
    m.stock,
    p.nombre as proveedor
FROM medicamentos m
LEFT JOIN proveedores p ON m.proveedor_id = p.id
WHERE m.requiere_receta = true
ORDER BY m.nombre;

-- =====================================================
-- 2. CONSULTAS DE VENTAS
-- =====================================================

-- 2.1 Ventas por período (último mes)
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
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '1 month'
ORDER BY v.fecha_venta DESC;

-- 2.2 Productos más vendidos
SELECT 
    m.nombre,
    m.categoria,
    SUM(dv.cantidad) as total_vendido,
    SUM(dv.total) as ingresos_totales,
    AVG(dv.precio_unitario) as precio_promedio
FROM detalles_venta dv
JOIN medicamentos m ON dv.medicamento_id = m.id
GROUP BY m.id, m.nombre, m.categoria
ORDER BY total_vendido DESC
LIMIT 15;

-- 2.3 Ventas por método de pago
SELECT 
    v.metodo_pago,
    COUNT(*) as cantidad_ventas,
    SUM(v.total) as total_ingresos,
    AVG(v.total) as promedio_venta
FROM ventas v
WHERE v.estado = 'PAGADA'
GROUP BY v.metodo_pago
ORDER BY total_ingresos DESC;

-- 2.4 Clientes más frecuentes
SELECT 
    c.nombre || ' ' || c.apellido as cliente,
    c.email,
    COUNT(v.id) as cantidad_compras,
    SUM(v.total) as total_gastado,
    AVG(v.total) as promedio_compra
FROM clientes c
JOIN ventas v ON c.id = v.cliente_id
WHERE v.estado = 'PAGADA'
GROUP BY c.id, c.nombre, c.apellido, c.email
ORDER BY total_gastado DESC
LIMIT 10;

-- 2.5 Ventas diarias (últimos 30 días)
SELECT 
    v.fecha_venta,
    COUNT(*) as cantidad_ventas,
    SUM(v.total) as ingresos_diarios,
    AVG(v.total) as promedio_venta
FROM ventas v
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '30 days'
AND v.estado = 'PAGADA'
GROUP BY v.fecha_venta
ORDER BY v.fecha_venta DESC;

-- =====================================================
-- 3. CONSULTAS DE ALERTAS
-- =====================================================

-- 3.1 Alertas pendientes por nivel de urgencia
SELECT 
    a.titulo,
    a.mensaje,
    a.tipo,
    a.nivel,
    a.fecha_creacion,
    m.nombre as medicamento,
    a.observaciones
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

-- 3.2 Alertas por tipo
SELECT 
    a.tipo,
    COUNT(*) as cantidad_alertas,
    COUNT(CASE WHEN a.estado = 'PENDIENTE' THEN 1 END) as pendientes,
    COUNT(CASE WHEN a.estado = 'RESUELTA' THEN 1 END) as resueltas
FROM alertas a
GROUP BY a.tipo
ORDER BY cantidad_alertas DESC;

-- 3.3 Tiempo promedio de resolución de alertas
SELECT 
    a.tipo,
    AVG(a.fecha_resolucion - a.fecha_creacion) as dias_promedio_resolucion,
    COUNT(*) as total_alertas
FROM alertas a
WHERE a.estado = 'RESUELTA'
AND a.fecha_resolucion IS NOT NULL
GROUP BY a.tipo
ORDER BY dias_promedio_resolucion ASC;

-- =====================================================
-- 4. CONSULTAS DE PROVEEDORES
-- =====================================================

-- 4.1 Productos por proveedor
SELECT 
    p.nombre as proveedor,
    p.contacto,
    COUNT(m.id) as cantidad_productos,
    SUM(m.stock) as stock_total,
    SUM(m.stock * m.precio) as valor_inventario
FROM proveedores p
LEFT JOIN medicamentos m ON p.id = m.proveedor_id
WHERE p.estado = 'ACTIVO'
GROUP BY p.id, p.nombre, p.contacto
ORDER BY cantidad_productos DESC;

-- 4.2 Proveedores con productos próximos a caducar
SELECT 
    p.nombre as proveedor,
    p.contacto,
    COUNT(m.id) as productos_caducando,
    STRING_AGG(m.nombre, ', ') as medicamentos
FROM proveedores p
JOIN medicamentos m ON p.id = m.proveedor_id
WHERE m.fecha_caducidad <= CURRENT_DATE + INTERVAL '30 days'
AND m.stock > 0
GROUP BY p.id, p.nombre, p.contacto
ORDER BY productos_caducando DESC;

-- =====================================================
-- 5. CONSULTAS DE LOTES
-- =====================================================

-- 5.1 Lotes próximos a caducar
SELECT 
    l.numero_lote,
    m.nombre as medicamento,
    l.fecha_entrada,
    l.fecha_caducidad,
    l.cantidad,
    l.estado,
    l.fecha_caducidad - CURRENT_DATE as dias_restantes
FROM lotes l
JOIN medicamentos m ON l.medicamento_id = m.id
WHERE l.fecha_caducidad <= CURRENT_DATE + INTERVAL '60 days'
AND l.estado = 'ACTIVO'
ORDER BY l.fecha_caducidad ASC;

-- 5.2 Lotes por medicamento
SELECT 
    m.nombre as medicamento,
    COUNT(l.id) as cantidad_lotes,
    SUM(l.cantidad) as stock_total_lotes,
    MIN(l.fecha_caducidad) as fecha_caducidad_mas_proxima
FROM medicamentos m
LEFT JOIN lotes l ON m.id = l.medicamento_id
WHERE l.estado = 'ACTIVO'
GROUP BY m.id, m.nombre
ORDER BY cantidad_lotes DESC;

-- =====================================================
-- 6. CONSULTAS DE RENTABILIDAD
-- =====================================================

-- 6.1 Margen de ganancia por categoría (estimado)
SELECT 
    m.categoria,
    COUNT(*) as productos,
    AVG(m.precio) as precio_promedio,
    SUM(m.stock * m.precio) as valor_inventario,
    SUM(m.stock) as stock_total
FROM medicamentos m
WHERE m.estado = 'ACTIVO'
GROUP BY m.categoria
ORDER BY valor_inventario DESC;

-- 6.2 Productos con mayor rotación
SELECT 
    m.nombre,
    m.categoria,
    m.stock,
    COALESCE(SUM(dv.cantidad), 0) as vendido_ultimo_mes,
    CASE 
        WHEN m.stock > 0 THEN 
            ROUND((COALESCE(SUM(dv.cantidad), 0)::DECIMAL / m.stock) * 100, 2)
        ELSE 0 
    END as porcentaje_rotacion
FROM medicamentos m
LEFT JOIN detalles_venta dv ON m.id = dv.medicamento_id
LEFT JOIN ventas v ON dv.venta_id = v.id
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '1 month'
OR v.fecha_venta IS NULL
GROUP BY m.id, m.nombre, m.categoria, m.stock
ORDER BY porcentaje_rotacion DESC
LIMIT 20;

-- =====================================================
-- 7. CONSULTAS DE TENDENCIAS
-- =====================================================

-- 7.1 Ventas por día de la semana
SELECT 
    EXTRACT(DOW FROM v.fecha_venta) as dia_semana,
    CASE EXTRACT(DOW FROM v.fecha_venta)
        WHEN 0 THEN 'Domingo'
        WHEN 1 THEN 'Lunes'
        WHEN 2 THEN 'Martes'
        WHEN 3 THEN 'Miércoles'
        WHEN 4 THEN 'Jueves'
        WHEN 5 THEN 'Viernes'
        WHEN 6 THEN 'Sábado'
    END as nombre_dia,
    COUNT(*) as cantidad_ventas,
    SUM(v.total) as ingresos
FROM ventas v
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '3 months'
AND v.estado = 'PAGADA'
GROUP BY EXTRACT(DOW FROM v.fecha_venta)
ORDER BY dia_semana;

-- 7.2 Productos más vendidos por mes
SELECT 
    EXTRACT(MONTH FROM v.fecha_venta) as mes,
    EXTRACT(YEAR FROM v.fecha_venta) as año,
    m.nombre,
    SUM(dv.cantidad) as cantidad_vendida
FROM detalles_venta dv
JOIN medicamentos m ON dv.medicamento_id = m.id
JOIN ventas v ON dv.venta_id = v.id
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '6 months'
GROUP BY EXTRACT(MONTH FROM v.fecha_venta), EXTRACT(YEAR FROM v.fecha_venta), m.id, m.nombre
ORDER BY año DESC, mes DESC, cantidad_vendida DESC;

-- =====================================================
-- 8. CONSULTAS DE MANTENIMIENTO
-- =====================================================

-- 8.1 Verificar integridad referencial
SELECT 
    'Ventas sin cliente' as problema,
    COUNT(*) as cantidad
FROM ventas v
LEFT JOIN clientes c ON v.cliente_id = c.id
WHERE c.id IS NULL

UNION ALL

SELECT 
    'Detalles sin venta' as problema,
    COUNT(*) as cantidad
FROM detalles_venta dv
LEFT JOIN ventas v ON dv.venta_id = v.id
WHERE v.id IS NULL

UNION ALL

SELECT 
    'Detalles sin medicamento' as problema,
    COUNT(*) as cantidad
FROM detalles_venta dv
LEFT JOIN medicamentos m ON dv.medicamento_id = m.id
WHERE m.id IS NULL;

-- 8.2 Productos sin proveedor
SELECT 
    m.nombre,
    m.laboratorio,
    m.precio,
    m.stock
FROM medicamentos m
WHERE m.proveedor_id IS NULL
AND m.estado = 'ACTIVO';

-- 8.3 Clientes sin ventas
SELECT 
    c.nombre || ' ' || c.apellido as cliente,
    c.email,
    c.telefono,
    c.fecha_creacion
FROM clientes c
LEFT JOIN ventas v ON c.id = v.cliente_id
WHERE v.id IS NULL
AND c.estado = 'ACTIVO';

-- =====================================================
-- 9. CONSULTAS DE REPORTES EJECUTIVOS
-- =====================================================

-- 9.1 Resumen ejecutivo mensual
SELECT 
    'RESUMEN MENSUAL' as tipo_reporte,
    COUNT(DISTINCT v.id) as total_ventas,
    SUM(v.total) as ingresos_totales,
    AVG(v.total) as promedio_venta,
    COUNT(DISTINCT v.cliente_id) as clientes_unicos,
    COUNT(DISTINCT dv.medicamento_id) as productos_vendidos
FROM ventas v
LEFT JOIN detalles_venta dv ON v.id = dv.venta_id
WHERE v.fecha_venta >= DATE_TRUNC('month', CURRENT_DATE)
AND v.estado = 'PAGADA';

-- 9.2 Top 5 categorías más vendidas
SELECT 
    m.categoria,
    SUM(dv.cantidad) as unidades_vendidas,
    SUM(dv.total) as ingresos_categoria,
    ROUND((SUM(dv.total) / (SELECT SUM(total) FROM ventas WHERE fecha_venta >= CURRENT_DATE - INTERVAL '1 month')) * 100, 2) as porcentaje_ingresos
FROM detalles_venta dv
JOIN medicamentos m ON dv.medicamento_id = m.id
JOIN ventas v ON dv.venta_id = v.id
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '1 month'
AND v.estado = 'PAGADA'
GROUP BY m.categoria
ORDER BY ingresos_categoria DESC
LIMIT 5;

-- 9.3 Estado del inventario
SELECT 
    'INVENTARIO' as seccion,
    COUNT(*) as total_productos,
    SUM(stock) as stock_total,
    SUM(stock * precio) as valor_inventario,
    COUNT(CASE WHEN stock <= stock_minimo THEN 1 END) as productos_stock_bajo,
    COUNT(CASE WHEN fecha_caducidad <= CURRENT_DATE + INTERVAL '30 days' THEN 1 END) as productos_caducando
FROM medicamentos
WHERE estado = 'ACTIVO';

-- =====================================================
-- 10. CONSULTAS DE OPTIMIZACIÓN
-- =====================================================

-- 10.1 Productos con exceso de stock
SELECT 
    m.nombre,
    m.stock,
    m.stock_minimo,
    COALESCE(SUM(dv.cantidad), 0) as vendido_ultimo_mes,
    ROUND((m.stock::DECIMAL / NULLIF(SUM(dv.cantidad), 0)), 1) as meses_inventario
FROM medicamentos m
LEFT JOIN detalles_venta dv ON m.id = dv.medicamento_id
LEFT JOIN ventas v ON dv.venta_id = v.id
WHERE v.fecha_venta >= CURRENT_DATE - INTERVAL '1 month'
OR v.fecha_venta IS NULL
GROUP BY m.id, m.nombre, m.stock, m.stock_minimo
HAVING m.stock > m.stock_minimo * 3
ORDER BY meses_inventario DESC;

-- 10.2 Productos con baja rotación
SELECT 
    m.nombre,
    m.categoria,
    m.stock,
    COALESCE(SUM(dv.cantidad), 0) as vendido_ultimo_mes,
    m.fecha_caducidad
FROM medicamentos m
LEFT JOIN detalles_venta dv ON m.id = dv.medicamento_id
LEFT JOIN ventas v ON dv.venta_id = v.id
WHERE (v.fecha_venta >= CURRENT_DATE - INTERVAL '1 month' OR v.fecha_venta IS NULL)
AND m.stock > 0
GROUP BY m.id, m.nombre, m.categoria, m.stock, m.fecha_caducidad
HAVING COALESCE(SUM(dv.cantidad), 0) < 5
ORDER BY vendido_ultimo_mes ASC;

-- =====================================================
-- FIN DE LAS CONSULTAS
-- =====================================================
--
-- INSTRUCCIONES DE USO:
-- 1. Ejecutar las consultas según el análisis requerido
-- 2. Modificar fechas y parámetros según necesidades
-- 3. Usar LIMIT para consultas que pueden devolver muchos resultados
-- 4. Crear vistas para consultas frecuentemente utilizadas
-- 5. Programar ejecución automática de consultas críticas
--
-- NOTAS:
-- - Todas las consultas están optimizadas para PostgreSQL
-- - Se incluyen consultas para inventario, ventas, alertas y análisis
-- - Las consultas pueden ser modificadas según necesidades específicas
-- - Se recomienda crear índices adicionales según patrones de uso
-- ===================================================== 