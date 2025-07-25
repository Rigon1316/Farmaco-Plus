-- =====================================================
-- EJEMPLOS DE INSERCIÓN DE USUARIOS - SISTEMA DE FARMACIA
-- =====================================================
-- Ejemplos prácticos para crear usuarios en el sistema
-- =====================================================

-- =====================================================
-- 1. EJEMPLOS BÁSICOS DE INSERCIÓN
-- =====================================================

-- Ejemplo 1: Crear un administrador
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('admin_principal', 'Admin2024!', 'admin.principal@farmacia.com', 'Juan Carlos', 'Administrador', 'ADMIN', 'ACTIVO');

-- Ejemplo 2: Crear un farmacéutico
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('farmaceutico_jefe', 'Farma2024!', 'farmaceutico.jefe@farmacia.com', 'Dra. María Elena', 'González', 'FARMACEUTICO', 'ACTIVO');

-- Ejemplo 3: Crear un vendedor
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('vendedor_turno1', 'Venta2024!', 'vendedor.turno1@farmacia.com', 'Carlos Alberto', 'Pérez', 'VENDEDOR', 'ACTIVO');

-- Ejemplo 4: Crear un encargado de inventario
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('inventario_jefe', 'Inventario2024!', 'inventario.jefe@farmacia.com', 'Roberto Daniel', 'Silva', 'INVENTARIO', 'ACTIVO');

-- Ejemplo 5: Crear un usuario de consulta
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('consulta_gerente', 'Consulta2024!', 'consulta.gerente@farmacia.com', 'Ana Patricia', 'Martínez', 'CONSULTA', 'ACTIVO');

-- =====================================================
-- 2. EJEMPLOS USANDO EL PROCEDIMIENTO ALMACENADO
-- =====================================================

-- Ejemplo 6: Crear usuario usando función
SELECT crear_usuario('nuevo_admin', 'NuevoAdmin2024!', 'nuevo.admin@farmacia.com', 'Nuevo', 'Administrador', 'ADMIN');

-- Ejemplo 7: Crear farmacéutico usando función
SELECT crear_usuario('farma_nuevo', 'FarmaNuevo2024!', 'farma.nuevo@farmacia.com', 'Nuevo', 'Farmacéutico', 'FARMACEUTICO');

-- Ejemplo 8: Crear vendedor usando función
SELECT crear_usuario('vendedor_nuevo', 'VendedorNuevo2024!', 'vendedor.nuevo@farmacia.com', 'Nuevo', 'Vendedor', 'VENDEDOR');

-- =====================================================
-- 3. EJEMPLOS DE MÚLTIPLES USUARIOS
-- =====================================================

-- Ejemplo 9: Insertar múltiples farmacéuticos
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('farma_001', 'Farma001!', 'farma.001@farmacia.com', 'Dr. Luis Miguel', 'Rodríguez', 'FARMACEUTICO', 'ACTIVO'),
('farma_002', 'Farma002!', 'farma.002@farmacia.com', 'Dra. Carmen Rosa', 'Hernández', 'FARMACEUTICO', 'ACTIVO'),
('farma_003', 'Farma003!', 'farma.003@farmacia.com', 'Dr. Fernando José', 'Moreno', 'FARMACEUTICO', 'ACTIVO'),
('farma_004', 'Farma004!', 'farma.004@farmacia.com', 'Dra. Patricia Ana', 'Jiménez', 'FARMACEUTICO', 'ACTIVO');

-- Ejemplo 10: Insertar múltiples vendedores
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('vendedor_001', 'Vendedor001!', 'vendedor.001@farmacia.com', 'Miguel Ángel', 'Torres', 'VENDEDOR', 'ACTIVO'),
('vendedor_002', 'Vendedor002!', 'vendedor.002@farmacia.com', 'Lucía María', 'Díaz', 'VENDEDOR', 'ACTIVO'),
('vendedor_003', 'Vendedor003!', 'vendedor.003@farmacia.com', 'Roberto Carlos', 'García', 'VENDEDOR', 'ACTIVO'),
('vendedor_004', 'Vendedor004!', 'vendedor.004@farmacia.com', 'Ana Sofía', 'López', 'VENDEDOR', 'ACTIVO'),
('vendedor_005', 'Vendedor005!', 'vendedor.005@farmacia.com', 'Carlos Eduardo', 'Martínez', 'VENDEDOR', 'ACTIVO');

-- Ejemplo 11: Insertar encargados de inventario
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('inventario_001', 'Inventario001!', 'inventario.001@farmacia.com', 'Fernando Luis', 'Silva', 'INVENTARIO', 'ACTIVO'),
('inventario_002', 'Inventario002!', 'inventario.002@farmacia.com', 'Patricia Elena', 'Rojas', 'INVENTARIO', 'ACTIVO'),
('inventario_003', 'Inventario003!', 'inventario.003@farmacia.com', 'Roberto José', 'Vargas', 'INVENTARIO', 'ACTIVO');

-- =====================================================
-- 4. EJEMPLOS DE USUARIOS CON DIFERENTES ESTADOS
-- =====================================================

-- Ejemplo 12: Usuario activo
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('usuario_activo', 'Activo2024!', 'usuario.activo@farmacia.com', 'Usuario', 'Activo', 'VENDEDOR', 'ACTIVO');

-- Ejemplo 13: Usuario inactivo
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('usuario_inactivo', 'Inactivo2024!', 'usuario.inactivo@farmacia.com', 'Usuario', 'Inactivo', 'CONSULTA', 'INACTIVO');

-- Ejemplo 14: Usuario suspendido
INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
('usuario_suspendido', 'Suspendido2024!', 'usuario.suspendido@farmacia.com', 'Usuario', 'Suspendido', 'VENDEDOR', 'SUSPENDIDO');

-- =====================================================
-- 5. EJEMPLOS DE CONSULTAS PARA VERIFICAR INSERCIONES
-- =====================================================

-- Consulta 1: Ver todos los usuarios creados
SELECT 
    id,
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    rol,
    estado,
    fecha_creacion
FROM usuarios
ORDER BY fecha_creacion DESC;

-- Consulta 2: Ver usuarios por rol
SELECT 
    rol,
    COUNT(*) as cantidad,
    STRING_AGG(nombre || ' ' || apellido, ', ') as usuarios
FROM usuarios
WHERE estado = 'ACTIVO'
GROUP BY rol
ORDER BY cantidad DESC;

-- Consulta 3: Ver usuarios activos
SELECT 
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    rol
FROM usuarios
WHERE estado = 'ACTIVO'
ORDER BY rol, nombre;

-- Consulta 4: Ver usuarios inactivos
SELECT 
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    rol,
    estado
FROM usuarios
WHERE estado != 'ACTIVO'
ORDER BY estado, nombre;

-- =====================================================
-- 6. EJEMPLOS DE ACTUALIZACIÓN DE USUARIOS
-- =====================================================

-- Ejemplo 15: Actualizar contraseña de un usuario
UPDATE usuarios 
SET password = 'NuevaPassword2024!'
WHERE username = 'admin_principal';

-- Ejemplo 16: Cambiar rol de un usuario
UPDATE usuarios 
SET rol = 'FARMACEUTICO'
WHERE username = 'vendedor_001';

-- Ejemplo 17: Activar un usuario inactivo
UPDATE usuarios 
SET estado = 'ACTIVO'
WHERE username = 'usuario_inactivo';

-- Ejemplo 18: Suspender un usuario
UPDATE usuarios 
SET estado = 'SUSPENDIDO'
WHERE username = 'vendedor_002';

-- =====================================================
-- 7. EJEMPLOS DE ELIMINACIÓN LÓGICA
-- =====================================================

-- Ejemplo 19: Desactivar usuario (eliminación lógica)
UPDATE usuarios 
SET estado = 'INACTIVO'
WHERE username = 'usuario_suspendido';

-- Ejemplo 20: Reactivar usuario
UPDATE usuarios 
SET estado = 'ACTIVO'
WHERE username = 'usuario_suspendido';

-- =====================================================
-- 8. EJEMPLOS DE CONSULTAS DE SEGURIDAD
-- =====================================================

-- Consulta 5: Verificar usuarios con contraseñas débiles
SELECT 
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    'Contraseña débil' as problema
FROM usuarios
WHERE password IN ('admin123', 'password', '123456', 'qwerty', 'admin')
AND estado = 'ACTIVO';

-- Consulta 6: Verificar usuarios sin acceso reciente
SELECT 
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    ultimo_acceso,
    'Sin acceso reciente' as problema
FROM usuarios
WHERE (ultimo_acceso IS NULL OR ultimo_acceso < CURRENT_TIMESTAMP - INTERVAL '30 days')
AND estado = 'ACTIVO';

-- Consulta 7: Verificar emails duplicados (si los hay)
SELECT 
    email,
    COUNT(*) as cantidad
FROM usuarios
GROUP BY email
HAVING COUNT(*) > 1;

-- =====================================================
-- 9. EJEMPLOS DE CREACIÓN DE SESIONES
-- =====================================================

-- Ejemplo 21: Crear sesión para un usuario
SELECT crear_sesion(1, 'token_ejemplo_123', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)');

-- Ejemplo 22: Crear múltiples sesiones
INSERT INTO sesiones_usuario (usuario_id, token, fecha_expiracion, ip_address, user_agent) VALUES
(2, 'token_farma_001', CURRENT_TIMESTAMP + INTERVAL '8 hours', '192.168.1.101', 'Mozilla/5.0 (Macintosh)'),
(3, 'token_vend_001', CURRENT_TIMESTAMP + INTERVAL '12 hours', '192.168.1.102', 'Mozilla/5.0 (Linux)'),
(4, 'token_inv_001', CURRENT_TIMESTAMP + INTERVAL '24 hours', '192.168.1.103', 'Mozilla/5.0 (Windows)');

-- =====================================================
-- 10. EJEMPLOS DE AUTENTICACIÓN
-- =====================================================

-- Ejemplo 23: Autenticar usuario
SELECT * FROM autenticar_usuario('admin_principal', 'Admin2024!');

-- Ejemplo 24: Autenticar usuario con contraseña incorrecta
SELECT * FROM autenticar_usuario('admin_principal', 'PasswordIncorrecto');

-- =====================================================
-- 11. EJEMPLOS DE CONSULTAS AVANZADAS
-- =====================================================

-- Consulta 8: Resumen completo de usuarios
SELECT 
    u.rol,
    COUNT(*) as total_usuarios,
    COUNT(CASE WHEN u.estado = 'ACTIVO' THEN 1 END) as usuarios_activos,
    COUNT(CASE WHEN u.estado = 'INACTIVO' THEN 1 END) as usuarios_inactivos,
    COUNT(CASE WHEN u.estado = 'SUSPENDIDO' THEN 1 END) as usuarios_suspendidos,
    COUNT(s.id) as sesiones_activas
FROM usuarios u
LEFT JOIN sesiones_usuario s ON u.id = s.usuario_id 
    AND s.estado = 'ACTIVA' 
    AND s.fecha_expiracion > CURRENT_TIMESTAMP
GROUP BY u.rol
ORDER BY total_usuarios DESC;

-- Consulta 9: Usuarios con más actividad
SELECT 
    u.username,
    u.nombre || ' ' || u.apellido as nombre_completo,
    u.rol,
    COUNT(s.id) as sesiones_totales,
    MAX(s.fecha_inicio) as ultima_sesion
FROM usuarios u
LEFT JOIN sesiones_usuario s ON u.id = s.usuario_id
WHERE u.estado = 'ACTIVO'
GROUP BY u.id, u.username, u.nombre, u.apellido, u.rol
ORDER BY sesiones_totales DESC;

-- =====================================================
-- 12. EJEMPLOS DE LIMPIEZA Y MANTENIMIENTO
-- =====================================================

-- Ejemplo 25: Limpiar sesiones expiradas
UPDATE sesiones_usuario 
SET estado = 'EXPIRADA'
WHERE fecha_expiracion < CURRENT_TIMESTAMP
AND estado = 'ACTIVA';

-- Ejemplo 26: Cerrar todas las sesiones de un usuario
UPDATE sesiones_usuario 
SET estado = 'CERRADA'
WHERE usuario_id = (SELECT id FROM usuarios WHERE username = 'usuario_suspendido')
AND estado = 'ACTIVA';

-- =====================================================
-- FIN DE EJEMPLOS
-- =====================================================
--
-- INSTRUCCIONES DE USO:
-- 1. Ejecutar los ejemplos según necesidades
-- 2. Modificar datos según requerimientos específicos
-- 3. Usar contraseñas seguras en producción
-- 4. Verificar permisos antes de ejecutar
-- 5. Hacer backup antes de modificaciones masivas
--
-- NOTAS:
-- - Los ejemplos incluyen diferentes tipos de usuarios
-- - Se muestran consultas de verificación
-- - Se incluyen ejemplos de mantenimiento
-- - Las contraseñas son ejemplos, cambiar en producción
-- ===================================================== 