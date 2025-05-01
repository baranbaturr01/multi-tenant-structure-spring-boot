package com.baranbatur.appointment_system.multitenant.config;

import com.baranbatur.appointment_system.multitenant.filter.TenantResolverFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TenantResolverFilter> tenantFilter() {
        FilterRegistrationBean<TenantResolverFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantResolverFilter());
        registrationBean.addUrlPatterns("/appointments/*");  // Endpoint'lere uygulanacak
        return registrationBean;
    }
}