-- =====================================================
-- SISTEMA DE USUARIOS - FARMACIA
-- =====================================================
-- Script para crear y gestionar usuarios del sistema
-- =====================================================

-- =====================================================
-- 1. CREACIÓN DE TABLA DE USUARIOS
-- =====================================================

-- Tabla de Usuarios del Sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    ultimo_acceso TIMESTAMP,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Roles del Sistema
CREATE TABLE IF NOT EXISTS roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    permisos TEXT[],
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Sesiones de Usuario
CREATE TABLE IF NOT EXISTS sesiones_usuario (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    token VARCHAR(255) NOT NULL UNIQUE,
    fecha_inicio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_expiracion TIMESTAMP NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA'
);

-- =====================================================
-- 2. ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_rol ON usuarios(rol);
CREATE INDEX idx_usuarios_estado ON usuarios(estado);
CREATE INDEX idx_sesiones_token ON sesiones_usuario(token);
CREATE INDEX idx_sesiones_usuario ON sesiones_usuario(usuario_id);

-- =====================================================
-- 3. DATOS DE ROLES DEL SISTEMA
-- =====================================================

INSERT INTO roles (nombre, descripcion, permisos) VALUES
('ADMIN', 'Administrador del sistema con acceso completo', 
 ARRAY['USUARIOS_CREAR', 'USUARIOS_EDITAR', 'USUARIOS_ELIMINAR', 'USUARIOS_VER',
       'MEDICAMENTOS_CREAR', 'MEDICAMENTOS_EDITAR', 'MEDICAMENTOS_ELIMINAR', 'MEDICAMENTOS_VER',
       'VENTAS_CREAR', 'VENTAS_EDITAR', 'VENTAS_ELIMINAR', 'VENTAS_VER',
       'CLIENTES_CREAR', 'CLIENTES_EDITAR', 'CLIENTES_ELIMINAR', 'CLIENTES_VER',
       'PROVEEDORES_CREAR', 'PROVEEDORES_EDITAR', 'PROVEEDORES_ELIMINAR', 'PROVEEDORES_VER',
       'ALERTAS_CREAR', 'ALERTAS_EDITAR', 'ALERTAS_ELIMINAR', 'ALERTAS_VER',
       'REPORTES_VER', 'CONFIGURACION_EDITAR']),

('FARMACEUTICO', 'Farmacéutico con acceso a inventario y ventas', 
 ARRAY['MEDICAMENTOS_CREAR', 'MEDICAMENTOS_EDITAR', 'MEDICAMENTOS_VER',
       'VENTAS_CREAR', 'VENTAS_EDITAR', 'VENTAS_VER',
       'CLIENTES_CREAR', 'CLIENTES_EDITAR', 'CLIENTES_VER',
       'ALERTAS_VER', 'REPORTES_VER']),

('VENDEDOR', 'Vendedor con acceso limitado a ventas', 
 ARRAY['MEDICAMENTOS_VER',
       'VENTAS_CREAR', 'VENTAS_VER',
       'CLIENTES_CREAR', 'CLIENTES_VER']),

('INVENTARIO', 'Encargado de inventario', 
 ARRAY['MEDICAMENTOS_CREAR', 'MEDICAMENTOS_EDITAR', 'MEDICAMENTOS_VER',
       'PROVEEDORES_VER',
       'ALERTAS_VER', 'REPORTES_VER']),

('CONSULTA', 'Usuario de solo consulta', 
 ARRAY['MEDICAMENTOS_VER',
       'VENTAS_VER',
       'CLIENTES_VER',
       'REPORTES_VER']);

-- =====================================================
-- 4. DATOS DE USUARIOS DE PRUEBA
-- =====================================================

-- Nota: Las contraseñas están en texto plano para pruebas
-- En producción, usar BCrypt o similar para encriptar

INSERT INTO usuarios (username, password, email, nombre, apellido, rol, estado) VALUES
-- Administrador del sistema
('admin', 'admin123', 'admin@farmacia.com', 'Administrador', 'Sistema', 'ADMIN', 'ACTIVO'),

-- Farmacéuticos
('farmaceutico1', 'farma123', 'maria.garcia@farmacia.com', 'María', 'García', 'FARMACEUTICO', 'ACTIVO'),
('farmaceutico2', 'farma456', 'carlos.lopez@farmacia.com', 'Carlos', 'López', 'FARMACEUTICO', 'ACTIVO'),
('farmaceutico3', 'farma789', 'ana.rodriguez@farmacia.com', 'Ana', 'Rodríguez', 'FARMACEUTICO', 'ACTIVO'),

-- Vendedores
('vendedor1', 'venta123', 'juan.perez@farmacia.com', 'Juan', 'Pérez', 'VENDEDOR', 'ACTIVO'),
('vendedor2', 'venta456', 'lucia.martinez@farmacia.com', 'Lucía', 'Martínez', 'VENDEDOR', 'ACTIVO'),
('vendedor3', 'venta789', 'roberto.hernandez@farmacia.com', 'Roberto', 'Hernández', 'VENDEDOR', 'ACTIVO'),

-- Encargados de inventario
('inventario1', 'inv123', 'fernando.moreno@farmacia.com', 'Fernando', 'Moreno', 'INVENTARIO', 'ACTIVO'),
('inventario2', 'inv456', 'patricia.jimenez@farmacia.com', 'Patricia', 'Jiménez', 'INVENTARIO', 'ACTIVO'),

-- Usuarios de consulta
('consulta1', 'cons123', 'miguel.torres@farmacia.com', 'Miguel', 'Torres', 'CONSULTA', 'ACTIVO'),
('consulta2', 'cons456', 'carmen.diaz@farmacia.com', 'Carmen', 'Díaz', 'CONSULTA', 'ACTIVO'),

-- Usuario inactivo de ejemplo
('usuario_inactivo', 'inactivo123', 'inactivo@farmacia.com', 'Usuario', 'Inactivo', 'CONSULTA', 'INACTIVO');

-- =====================================================
-- 5. DATOS DE SESIONES DE EJEMPLO
-- =====================================================

INSERT INTO sesiones_usuario (usuario_id, token, fecha_expiracion, ip_address, user_agent) VALUES
(1, 'token_admin_123456', CURRENT_TIMESTAMP + INTERVAL '24 hours', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'),
(2, 'token_farma_789012', CURRENT_TIMESTAMP + INTERVAL '8 hours', '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36'),
(4, 'token_vend_345678', CURRENT_TIMESTAMP + INTERVAL '12 hours', '192.168.1.102', 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36');

-- =====================================================
-- 6. CONSULTAS ÚTILES PARA GESTIÓN DE USUARIOS
-- =====================================================

-- Consulta para ver todos los usuarios activos
SELECT 
    u.id,
    u.username,
    u.email,
    u.nombre || ' ' || u.apellido as nombre_completo,
    u.rol,
    u.estado,
    u.ultimo_acceso,
    u.fecha_creacion
FROM usuarios u
WHERE u.estado = 'ACTIVO'
ORDER BY u.fecha_creacion DESC;

-- Consulta para ver usuarios por rol
SELECT 
    r.nombre as rol,
    COUNT(u.id) as cantidad_usuarios,
    STRING_AGG(u.nombre || ' ' || u.apellido, ', ') as usuarios
FROM roles r
LEFT JOIN usuarios u ON r.nombre = u.rol
WHERE u.estado = 'ACTIVO'
GROUP BY r.nombre, r.descripcion
ORDER BY cantidad_usuarios DESC;

-- Consulta para ver sesiones activas
SELECT 
    u.username,
    u.nombre || ' ' || u.apellido as nombre_completo,
    s.token,
    s.fecha_inicio,
    s.fecha_expiracion,
    s.ip_address,
    s.estado
FROM sesiones_usuario s
JOIN usuarios u ON s.usuario_id = u.id
WHERE s.estado = 'ACTIVA'
AND s.fecha_expiracion > CURRENT_TIMESTAMP
ORDER BY s.fecha_inicio DESC;

-- Consulta para ver permisos por rol
SELECT 
    r.nombre as rol,
    r.descripcion,
    array_to_string(r.permisos, ', ') as permisos
FROM roles r
WHERE r.estado = 'ACTIVO'
ORDER BY r.nombre;

-- =====================================================
-- 7. PROCEDIMIENTOS ALMACENADOS PARA GESTIÓN DE USUARIOS
-- =====================================================

-- Procedimiento para crear un nuevo usuario
CREATE OR REPLACE FUNCTION crear_usuario(
    p_username VARCHAR(50),
    p_password VARCHAR(255),
    p_email VARCHAR(150),
    p_nombre VARCHAR(100),
    p_apellido VARCHAR(100),
    p_rol VARCHAR(50)
) RETURNS BIGINT AS $$
DECLARE
    v_usuario_id BIGINT;
BEGIN
    INSERT INTO usuarios (username, password, email, nombre, apellido, rol)
    VALUES (p_username, p_password, p_email, p_nombre, p_apellido, p_rol)
    RETURNING id INTO v_usuario_id;
    
    RETURN v_usuario_id;
END;
$$ LANGUAGE plpgsql;

-- Procedimiento para autenticar usuario
CREATE OR REPLACE FUNCTION autenticar_usuario(
    p_username VARCHAR(50),
    p_password VARCHAR(255)
) RETURNS TABLE(
    usuario_id BIGINT,
    username VARCHAR(50),
    email VARCHAR(150),
    nombre_completo VARCHAR(200),
    rol VARCHAR(50),
    autenticado BOOLEAN
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        u.id,
        u.username,
        u.email,
        u.nombre || ' ' || u.apellido,
        u.rol,
        CASE WHEN u.password = p_password THEN true ELSE false END
    FROM usuarios u
    WHERE u.username = p_username
    AND u.estado = 'ACTIVO';
END;
$$ LANGUAGE plpgsql;

-- Procedimiento para crear sesión de usuario
CREATE OR REPLACE FUNCTION crear_sesion(
    p_usuario_id BIGINT,
    p_token VARCHAR(255),
    p_ip_address VARCHAR(45),
    p_user_agent TEXT
) RETURNS BOOLEAN AS $$
BEGIN
    INSERT INTO sesiones_usuario (usuario_id, token, fecha_expiracion, ip_address, user_agent)
    VALUES (p_usuario_id, p_token, CURRENT_TIMESTAMP + INTERVAL '24 hours', p_ip_address, p_user_agent);
    
    UPDATE usuarios 
    SET ultimo_acceso = CURRENT_TIMESTAMP
    WHERE id = p_usuario_id;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- Procedimiento para cerrar sesión
CREATE OR REPLACE FUNCTION cerrar_sesion(p_token VARCHAR(255)) RETURNS BOOLEAN AS $$
BEGIN
    UPDATE sesiones_usuario 
    SET estado = 'CERRADA'
    WHERE token = p_token;
    
    RETURN FOUND;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- 8. VISTAS ÚTILES
-- =====================================================

-- Vista para resumen de usuarios
CREATE OR REPLACE VIEW v_usuarios_resumen AS
SELECT 
    u.id,
    u.username,
    u.email,
    u.nombre || ' ' || u.apellido as nombre_completo,
    u.rol,
    u.estado,
    u.ultimo_acceso,
    u.fecha_creacion,
    COUNT(s.id) as sesiones_activas
FROM usuarios u
LEFT JOIN sesiones_usuario s ON u.id = s.usuario_id 
    AND s.estado = 'ACTIVA' 
    AND s.fecha_expiracion > CURRENT_TIMESTAMP
GROUP BY u.id, u.username, u.email, u.nombre, u.apellido, u.rol, u.estado, u.ultimo_acceso, u.fecha_creacion;

-- Vista para permisos de usuarios
CREATE OR REPLACE VIEW v_permisos_usuarios AS
SELECT 
    u.username,
    u.nombre || ' ' || u.apellido as nombre_completo,
    u.rol,
    r.descripcion as descripcion_rol,
    array_to_string(r.permisos, ', ') as permisos
FROM usuarios u
JOIN roles r ON u.rol = r.nombre
WHERE u.estado = 'ACTIVO'
ORDER BY u.rol, u.nombre;

-- =====================================================
-- 9. TRIGGERS PARA MANTENIMIENTO
-- =====================================================

-- Trigger para actualizar fecha_actualizacion
CREATE OR REPLACE FUNCTION actualizar_fecha_usuario()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_actualizar_fecha_usuarios
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_usuario();

-- Trigger para limpiar sesiones expiradas
CREATE OR REPLACE FUNCTION limpiar_sesiones_expiradas()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE sesiones_usuario 
    SET estado = 'EXPIRADA'
    WHERE fecha_expiracion < CURRENT_TIMESTAMP
    AND estado = 'ACTIVA';
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =====================================================
-- 10. CONSULTAS DE SEGURIDAD
-- =====================================================

-- Verificar usuarios con contraseñas débiles (ejemplo)
SELECT 
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    'Contraseña débil detectada' as problema
FROM usuarios
WHERE password IN ('admin123', 'password', '123456', 'qwerty')
AND estado = 'ACTIVO';

-- Verificar sesiones sospechosas (múltiples sesiones del mismo usuario)
SELECT 
    u.username,
    u.nombre || ' ' || u.apellido as nombre_completo,
    COUNT(s.id) as sesiones_activas,
    'Múltiples sesiones activas' as advertencia
FROM usuarios u
JOIN sesiones_usuario s ON u.id = s.usuario_id
WHERE s.estado = 'ACTIVA'
AND s.fecha_expiracion > CURRENT_TIMESTAMP
GROUP BY u.id, u.username, u.nombre, u.apellido
HAVING COUNT(s.id) > 1;

-- Verificar usuarios inactivos por mucho tiempo
SELECT 
    username,
    email,
    nombre || ' ' || apellido as nombre_completo,
    ultimo_acceso,
    'Usuario inactivo por más de 30 días' as problema
FROM usuarios
WHERE ultimo_acceso < CURRENT_TIMESTAMP - INTERVAL '30 days'
AND estado = 'ACTIVO';

-- =====================================================
-- 11. EJEMPLOS DE USO
-- =====================================================

-- Ejemplo: Crear un nuevo usuario
-- SELECT crear_usuario('nuevo_usuario', 'password123', 'nuevo@farmacia.com', 'Nuevo', 'Usuario', 'VENDEDOR');

-- Ejemplo: Autenticar usuario
-- SELECT * FROM autenticar_usuario('admin', 'admin123');

-- Ejemplo: Crear sesión
-- SELECT crear_sesion(1, 'nuevo_token_123', '192.168.1.100', 'Mozilla/5.0...');

-- Ejemplo: Cerrar sesión
-- SELECT cerrar_sesion('token_admin_123456');

-- =====================================================
-- FIN DEL SCRIPT DE USUARIOS
-- =====================================================
--
-- INSTRUCCIONES DE USO:
-- 1. Ejecutar este script después de crear las tablas principales
-- 2. Modificar las contraseñas en producción
-- 3. Configurar encriptación de contraseñas
-- 4. Revisar y ajustar permisos según necesidades
-- 5. Configurar políticas de seguridad
--
-- NOTAS:
-- - Las contraseñas están en texto plano para pruebas
-- - En producción usar BCrypt o similar
-- - Configurar políticas de contraseñas seguras
-- - Implementar autenticación de dos factores si es necesario
-- - Configurar logs de auditoría para acciones críticas
-- ===================================================== 