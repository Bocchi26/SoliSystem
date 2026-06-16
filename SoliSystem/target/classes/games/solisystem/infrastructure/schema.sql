
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    rol VARCHAR(50) NOT NULL CONSTRAINT chk_rol CHECK (rol IN ('SOLICITANTE', 'FUNCIONARIO'))
);

CREATE TABLE tipos_solicitud (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tiempo_estimado_dias INT NOT NULL
);

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

CREATE TABLE notificaciones (
    id BIGSERIAL PRIMARY KEY,
    solicitud_id BIGINT NOT NULL,
    mensaje TEXT NOT NULL,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    estado_solicitud VARCHAR(50) NOT NULL,
    FOREIGN KEY (solicitud_id) REFERENCES solicitudes(id) ON DELETE CASCADE
);
