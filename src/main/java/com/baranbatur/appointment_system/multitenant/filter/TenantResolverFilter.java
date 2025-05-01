package com.baranbatur.appointment_system.multitenant.filter;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantResolverFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String tenant = resolveTenantFromRequest(request);
        TenantContext.setCurrentTenant(tenant);

        filterChain.doFilter(request, response);

        TenantContext.clear();  // Cleanup after request
    }

    private String resolveTenantFromRequest(HttpServletRequest request) {
        String host = request.getHeader("Host");
        // Burada, subdomain'den tenant adı alıyoruz
        String tenant = host != null && host.contains(".localhost") ? host.split("\\.")[0] : "default";
        return tenant;
    }
}