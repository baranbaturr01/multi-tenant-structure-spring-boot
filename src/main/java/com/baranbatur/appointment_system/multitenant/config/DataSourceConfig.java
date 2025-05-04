package com.baranbatur.appointment_system.multitenant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

    @Bean("centralDataSource")
    public DataSource centralDataSource() {
        RoutingDataSource dataSource = new RoutingDataSource();

        // Burada veritabanı bağlantılarının yönlendirilmesini sağlıyoruz
        Map<Object, Object> targetDataSources = new HashMap<>();

        // Default DB
        targetDataSources.put("default", createDataSource("jdbc:postgresql://localhost:5435/default_db"));

   

        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(createDataSource("jdbc:postgresql://localhost:5435/default_db"));

        return dataSource;
    }

    private DataSource createDataSource(String url) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername("postgres");
        dataSource.setPassword("password");
        return dataSource;
    }
}