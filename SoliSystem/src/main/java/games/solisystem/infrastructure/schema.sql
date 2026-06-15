CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    rol VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS tipos_solicitud (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    tiempo_estimado_dias INT NOT NULL
);

CREATE TABLE IF NOT EXISTS solicitudes (
    id SERIAL PRIMARY KEY,
    usuario_id INT REFERENCES usuarios(id),
    tipo_solicitud_id INT REFERENCES tipos_solicitud(id),
    descripcion TEXT NOT NULL,
    fecha_creacion DATE NOT NULL,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE IF NOT EXISTS notificaciones (
    id SERIAL PRIMARY KEY,
    solicitud_id INT REFERENCES solicitudes(id),
    mensaje TEXT NOT NULL,
    fecha DATE NOT NULL,
    estado_solicitud VARCHAR(30) NOT NULL
);