package com.baranbatur.appointment_system.multitenant.tenant;

//Bu sınıf, tenant'ı belirledikten sonra, doğru veritabanına yönlendirecek.

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContext.getCurrentTenant();
    }
}
