package com.baranbatur.appointment_system.multitenant.tenant;

//Spring konteynerine RoutingDataSource ve DataSource bean'lerini ekliyor

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Autowired
    private DataSourceProvider dataSourceProvider;

    @Bean
    public DataSource dataSource() {
        RoutingDataSource dataSource = new RoutingDataSource();
        dataSource.setTargetDataSources(null);  // İlk başta boş, runtime’da set edilecek
        dataSource.setDefaultTargetDataSource(dataSourceProvider.getDataSource("defaultTenant"));
        return dataSource;
    }
}