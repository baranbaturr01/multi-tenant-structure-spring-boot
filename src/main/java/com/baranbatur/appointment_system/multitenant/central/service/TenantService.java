package com.baranbatur.appointment_system.multitenant.central.service;


import com.baranbatur.appointment_system.multitenant.central.entity.Tenant;
import com.baranbatur.appointment_system.multitenant.central.repository.TenantRepository;
import com.baranbatur.appointment_system.multitenant.dto.TenantRegistrationRequest;
import com.baranbatur.appointment_system.multitenant.tenant.DataSourceProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final JdbcTemplate centralJdbc;        // central-db için
    private final DataSourceProvider dataSourceProvider;

    @Transactional
    public void registerTenant(TenantRegistrationRequest req) {
        // 1. CentralDB'ye kaydet
        Tenant tenant = Tenant.builder()
                .name(req.getName())
                .dbUrl("jdbc:postgresql://localhost:5432/" + req.getDbName())
                .dbUsername(req.getDbUsername())
                .dbPassword(req.getDbPassword())
                .build();
        tenantRepository.save(tenant);

        // 2. Yeni DB oluştur
        centralJdbc.execute("CREATE DATABASE " + req.getDbName());

        // 3. Flyway ile migration'ları uygula
        Flyway.configure()
                .dataSource(tenant.getDbUrl(), tenant.getDbUsername(), tenant.getDbPassword())
                .locations("classpath:db/migration/tenants") // tenant-db şemaları
                .baselineOnMigrate(true)
                .load()
                .migrate();

        // 4. DataSourceProvider cache'ine ekleyelim
        dataSourceProvider.addDataSource(tenant.getName(), tenant.getDbUrl(),
                tenant.getDbUsername(), tenant.getDbPassword());
    }
}