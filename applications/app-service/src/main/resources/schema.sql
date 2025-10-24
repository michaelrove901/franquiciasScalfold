-- Borrar tablas si existen (solo para pruebas)
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS franchises;

-- Tabla de franquicias
CREATE TABLE franchises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tabla de sucursales
CREATE TABLE branches (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id BIGINT NOT NULL,
    CONSTRAINT fk_franchise
        FOREIGN KEY(franchise_id) REFERENCES franchises(id)
        ON DELETE CASCADE
);

-- Tabla de productos
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL,
    branch_id BIGINT NOT NULL,
    CONSTRAINT fk_branch
        FOREIGN KEY(branch_id) REFERENCES branches(id)
        ON DELETE CASCADE
);
