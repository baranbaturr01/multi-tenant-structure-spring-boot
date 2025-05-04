package com.baranbatur.appointment_system.multitenant.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.flywaydb.core.Flyway;

import java.util.List;
import java.util.Map;

@Service
public class TenantService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSourceProvider dataSourceProvider;

    @Transactional
    public void createTenant(String name, String subdomain, String email, String phone, String address,
                           String workingHoursStart, String workingHoursEnd, int appointmentInterval) {
        // Yeni veritabanı adı oluştur
        String dbName = "tenant_" + subdomain.toLowerCase().replaceAll("[^a-z0-9]", "_");
        
        // Merkezi veritabanına tenant bilgilerini kaydet
        jdbcTemplate.update(
            "INSERT INTO tenants (name, subdomain, db_name, email, phone, address, " +
            "working_hours_start, working_hours_end, appointment_interval) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
            name, subdomain, dbName, email, phone, address,
            workingHoursStart, workingHoursEnd, appointmentInterval
        );

        // Yeni veritabanı oluştur
        jdbcTemplate.execute("CREATE DATABASE " + dbName);

        // Yeni veritabanı için bağlantı bilgilerini kaydet
        String dbUrl = "jdbc:postgresql://localhost:5435/" + dbName;
        dataSourceProvider.addDataSource(subdomain, dbUrl, "postgres", "password");

        // Flyway ile migration'ları uygula
        Flyway.configure()
            .dataSource(dbUrl, "postgres", "password")
            .locations("classpath:db/migration/tenants")
            .baselineOnMigrate(true)
            .load()
            .migrate();

        // Yeni veritabanında tabloları oluştur
        JdbcTemplate tenantJdbcTemplate = new JdbcTemplate(dataSourceProvider.getDataSource(subdomain));
        createTenantTables(tenantJdbcTemplate);
    }

    private void createTenantTables(JdbcTemplate jdbcTemplate) {
        // V2__create_tenant_specific_tables.sql içindeki SQL komutlarını çalıştır
        jdbcTemplate.execute("CREATE TABLE chairs (" +
            "id SERIAL PRIMARY KEY, " +
            "number INT NOT NULL, " +
            "name VARCHAR(100) NOT NULL, " +
            "description TEXT, " +
            "is_active BOOLEAN DEFAULT TRUE, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")");

        jdbcTemplate.execute("CREATE TABLE staff (" +
            "id SERIAL PRIMARY KEY, " +
            "name VARCHAR(100) NOT NULL, " +
            "email VARCHAR(255), " +
            "phone VARCHAR(20), " +
            "chair_id INTEGER REFERENCES chairs(id), " +
            "is_active BOOLEAN DEFAULT TRUE, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")");

        jdbcTemplate.execute("CREATE TABLE appointments (" +
            "id SERIAL PRIMARY KEY, " +
            "customer_name VARCHAR(100) NOT NULL, " +
            "customer_email VARCHAR(255), " +
            "customer_phone VARCHAR(20) NOT NULL, " +
            "chair_id INTEGER REFERENCES chairs(id), " +
            "staff_id INTEGER REFERENCES staff(id), " +
            "start_time TIMESTAMP NOT NULL, " +
            "end_time TIMESTAMP NOT NULL, " +
            "status VARCHAR(50) NOT NULL, " +
            "reminder_sent BOOLEAN DEFAULT FALSE, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")");

        jdbcTemplate.execute("CREATE TABLE appointment_settings (" +
            "id SERIAL PRIMARY KEY, " +
            "reminder_before_minutes INT DEFAULT 30, " +
            "sms_enabled BOOLEAN DEFAULT FALSE, " +
            "email_enabled BOOLEAN DEFAULT TRUE, " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")");
    }

    public List<Map<String, Object>> getAllTenants() {
        return jdbcTemplate.queryForList("SELECT * FROM tenants");
    }

    public Map<String, Object> getTenantBySubdomain(String subdomain) {
        return jdbcTemplate.queryForMap(
            "SELECT * FROM tenants WHERE subdomain = ?",
            subdomain
        );
    }
} 