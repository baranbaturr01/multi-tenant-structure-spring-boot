package com.baranbatur.appointment_system.multitenant.management;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StaffService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getAllStaff() {
        return jdbcTemplate.queryForList(
            "SELECT id, name, email, phone, chair_id, is_active " +
            "FROM staff WHERE tenant_id = ?",
            getCurrentTenantId()
        );
    }

    public void createStaff(String name, String email, String phone, Integer chairId) {
        jdbcTemplate.update(
            "INSERT INTO staff (tenant_id, name, email, phone, chair_id, is_active) " +
            "VALUES (?, ?, ?, ?, ?, true)",
            getCurrentTenantId(), name, email, phone, chairId
        );
    }

    public void updateStaff(int id, String name, String email, String phone, Integer chairId, boolean isActive) {
        jdbcTemplate.update(
            "UPDATE staff SET " +
            "name = ?, email = ?, phone = ?, chair_id = ?, is_active = ? " +
            "WHERE id = ? AND tenant_id = ?",
            name, email, phone, chairId, isActive, id, getCurrentTenantId()
        );
    }

    public void deleteStaff(int id) {
        jdbcTemplate.update(
            "DELETE FROM staff WHERE id = ? AND tenant_id = ?",
            id, getCurrentTenantId()
        );
    }

    private String getCurrentTenantId() {
        return TenantContext.getCurrentTenant();
    }
} 