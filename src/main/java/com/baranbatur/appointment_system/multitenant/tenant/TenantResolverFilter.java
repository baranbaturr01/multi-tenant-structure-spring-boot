package com.baranbatur.appointment_system.multitenant.tenant;


import com.baranbatur.appointment_system.multitenant.central.repository.TenantRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

//Subdomain'e göre tenant’ı belirlemek için bir filter kullanıyorum
@WebFilter(urlPatterns = "/*")
public class TenantResolverFilter implements Filter {

    @Autowired
    private TenantRepository tenantRepository;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String host = httpRequest.getServerName(); // örneğin: mekanA.batur.com
        String subdomain = host.split("\\.")[0]; // mekanA

        tenantRepository.findByName(subdomain)
                .ifPresent(tenant -> TenantContext.setCurrentTenant(tenant.getName()));

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup logic if needed
    }
}