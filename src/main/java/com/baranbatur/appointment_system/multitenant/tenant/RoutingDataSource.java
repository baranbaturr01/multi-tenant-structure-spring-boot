package com.baranbatur.appointment_system.multitenant.tenant;

//Bu sınıf, tenant'ı belirledikten sonra, doğru veritabanına yönlendirecek.

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Burada, TenantContext'ten geçerli tenant'ı alıyoruz
        return TenantContext.getCurrentTenant();
    }
}
