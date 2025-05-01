-- V1__create_appointments_table.sql
CREATE TABLE appointments (
                              id SERIAL PRIMARY KEY,
                              customer_name VARCHAR(255),
                              appointment_time TIMESTAMP,
                              chair_number INT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
