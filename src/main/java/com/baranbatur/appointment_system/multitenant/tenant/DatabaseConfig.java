package com.baranbatur.appointment_system.multitenant.tenant;

//Spring konteynerine RoutingDataSource ve DataSource bean'lerini ekliyor

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseConfig {

    @Autowired
    @Lazy
    private DataSourceProvider dataSourceProvider;

    @Bean
    public AbstractRoutingDataSource abstractRoutingDataSource() {
        RoutingDataSource dataSource = new RoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        
        // Varsayılan veritabanı bağlantısı
        DriverManagerDataSource defaultDataSource = new DriverManagerDataSource();
        defaultDataSource.setUrl("jdbc:postgresql://localhost:5435/centraldb");
        defaultDataSource.setUsername("postgres");
        defaultDataSource.setPassword("password");
        defaultDataSource.setDriverClassName("org.postgresql.Driver");
        
        targetDataSources.put("default", defaultDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(defaultDataSource);
        
        return dataSource;
    }

    @Bean
    @Primary
    public DataSource routingDataSource(AbstractRoutingDataSource abstractRoutingDataSource) {
        return abstractRoutingDataSource;
    }
}