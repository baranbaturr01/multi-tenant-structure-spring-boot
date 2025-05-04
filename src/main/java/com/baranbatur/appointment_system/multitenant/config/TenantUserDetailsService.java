package com.baranbatur.appointment_system.multitenant.config;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TenantUserDetailsService implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String tenant = TenantContext.getCurrentTenant();
        Map<String, Object> user = jdbcTemplate.queryForMap(
            "SELECT * FROM tenant_users WHERE username = ?",
            username
        );
        
        return org.springframework.security.core.userdetails.User
            .withUsername((String) user.get("username"))
            .password((String) user.get("password"))
            .roles((String) user.get("role"))
            .build();
    }
}