CREATE TABLE tenants
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) UNIQUE NOT NULL,
    db_url      VARCHAR(255)        NOT NULL,
    db_username VARCHAR(100)        NOT NULL,
    db_password VARCHAR(100)        NOT NULL
);
