package com.baranbatur.appointment_system.multitenant.management;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChairService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAllChairs() {
        return jdbcTemplate.queryForList(
            "SELECT id, number, name, description, is_active " +
            "FROM chairs WHERE tenant_id = ?",
            getCurrentTenantId()
        );
    }

    public void createChair(int number, String name, String description) {
        jdbcTemplate.update(
            "INSERT INTO chairs (tenant_id, number, name, description, is_active) " +
            "VALUES (?, ?, ?, ?, true)",
            getCurrentTenantId(), number, name, description
        );
    }

    public void updateChair(int id, int number, String name, String description, boolean isActive) {
        jdbcTemplate.update(
            "UPDATE chairs SET " +
            "number = ?, name = ?, description = ?, is_active = ? " +
            "WHERE id = ? AND tenant_id = ?",
            number, name, description, isActive, id, getCurrentTenantId()
        );
    }

    public void deleteChair(int id) {
        jdbcTemplate.update(
            "DELETE FROM chairs WHERE id = ? AND tenant_id = ?",
            id, getCurrentTenantId()
        );
    }

    private String getCurrentTenantId() {
        return TenantContext.getCurrentTenant();
    }
} 