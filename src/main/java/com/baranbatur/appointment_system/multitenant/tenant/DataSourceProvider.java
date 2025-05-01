package com.baranbatur.appointment_system.multitenant.tenant;

//bu  sınıf, tenant bilgisine göre doğru veritabanı bağlantısını sağlayacak.

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataSourceProvider {
    private final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();
    private final AbstractRoutingDataSource routingDataSource;

    @Autowired
    public DataSourceProvider(AbstractRoutingDataSource routingDataSource) {
        this.routingDataSource = routingDataSource;
    }

    public void addDataSource(String tenantId, String url, String username, String password) {
        DriverManagerDataSource ds = new DriverManagerDataSource(url, username, password);
        ds.setDriverClassName("org.postgresql.Driver");
        dataSources.put(tenantId, ds);

        // RoutingDataSource'un hedeflerine ekleyelim
        Map<Object, Object> targets = new HashMap<>(routingDataSource.getResolvedDataSources());
        targets.put(tenantId, ds);
        routingDataSource.setTargetDataSources(targets);
        routingDataSource.afterPropertiesSet();
    }

    public DataSource getDataSource(String tenantId) {
        return dataSources.get(tenantId);
    }
}}