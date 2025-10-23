CREATE TABLE IF NOT EXISTS franchises (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS branches (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    franchise_id BIGINT REFERENCES franchises(id)
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    product_id VARCHAR(50),
    name VARCHAR(255) NOT NULL,
    stock INT,
    branch_id VARCHAR(50) REFERENCES branches(id)
);
