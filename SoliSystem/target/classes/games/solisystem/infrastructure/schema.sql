-- Eliminar tablas si existen (para poder reiniciar la base de datos limpiamente)
DROP TABLE IF EXISTS notificaciones CASCADE;
DROP TABLE IF EXISTS solicitudes CASCADE;
DROP TABLE IF EXISTS tipos_solicitud CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;

-- Crear Tabla USUARIOS
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    rol VARCHAR(50) NOT NULL CONSTRAINT chk_rol CHECK (rol IN ('SOLICITANTE', 'FUNCIONARIO'))
);

-- Crear Tabla TIPOS_SOLICITUD
CREATE TABLE tipos_solicitud (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tiempo_estimado_dias INT NOT NULL
);

-- Crear Tabla SOLICITUDES
CREATE TABLE solicitudes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    tipo_solicitud_id BIGINT NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_creacion DATE NOT NULL DEFAULT CURRENT_DATE,
    estado VARCHAR(50) NOT NULL CONSTRAINT chk_estado CHECK (estado IN ('CREADA', 'EN_REVISION', 'APROBADA', 'RECHAZADA', 'CERRADA')),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (tipo_solicitud_id) REFERENCES tipos_solicitud(id) ON DELETE CASCADE
);

-- Crear Tabla NOTIFICACIONES
CREATE TABLE notificaciones (
    id BIGSERIAL PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    mensaje TEXT NOT NULL,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_solicitud VARCHAR(50) NOT NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes(id) ON DELETE CASCADE
);

-- Datos de prueba 
INSERT INTO usuarios (nombre, correo, rol) VALUES
('Carlos Mendoza', 'carlos.mendoza@email.com', 'SOLICITANTE'),
('Ana Gómez', 'ana.gomez@solisystem.com', 'FUNCIONARIO'),
('María Rodríguez', 'maria.rod@email.com', 'SOLICITANTE');

INSERT INTO tipos_solicitud (nombre, descripcion, tiempo_estimado_dias) VALUES
('Petición General', 'Solicitudes de información general o trámites sencillos', 5),
('Reclamo de Facturación', 'Discrepancias en los cobros de servicios', 10),
('Soporte Técnico', 'Reportes de fallos en el sistema o hardware', 3);

INSERT INTO solicitudes (usuario_id, tipo_solicitud_id, descripcion, fecha_creacion, estado) VALUES
(1, 3, 'Mi usuario no puede acceder al panel principal, muestra error 500', '2026-06-14', 'CREADA'),
(3, 2, 'Cobro doble en la factura del mes de mayo', '2026-06-13', 'EN_REVISION');

INSERT INTO notificaciones (solicitud_id, mensaje, fecha, estado_solicitud) VALUES
(1, 'Tu solicitud de Soporte Técnico ha sido creada correctamente.', '2026-06-14', 'CREADA'),
(2, 'Tu reclamo ha pasado a estado de revisión por parte de un funcionario.', '2026-06-14', 'EN_REVISION');
