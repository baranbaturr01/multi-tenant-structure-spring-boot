package com.baranbatur.appointment_system.multitenant.config;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Burada, TenantContext'ten geçerli tenant'ı alıyoruz
        return TenantContext.getCurrentTenant();
    }
}