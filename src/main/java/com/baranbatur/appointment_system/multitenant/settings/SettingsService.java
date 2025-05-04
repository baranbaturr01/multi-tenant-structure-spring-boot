package com.baranbatur.appointment_system.multitenant.settings;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SettingsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getAppointmentSettings() {
        return jdbcTemplate.queryForMap(
            "SELECT reminder_before_minutes, sms_enabled, email_enabled " +
            "FROM appointment_settings WHERE tenant_id = ?",
            getCurrentTenantId()
        );
    }

    public void updateAppointmentSettings(int reminderBeforeMinutes, boolean smsEnabled, boolean emailEnabled) {
        jdbcTemplate.update(
            "UPDATE appointment_settings SET " +
            "reminder_before_minutes = ?, sms_enabled = ?, email_enabled = ? " +
            "WHERE tenant_id = ?",
            reminderBeforeMinutes, smsEnabled, emailEnabled, getCurrentTenantId()
        );
    }

    public void updateWorkingHours(String startTime, String endTime, int appointmentInterval) {
        jdbcTemplate.update(
            "UPDATE working_hours SET " +
            "start_time = ?, end_time = ?, appointment_interval = ? " +
            "WHERE tenant_id = ?",
            startTime, endTime, appointmentInterval, getCurrentTenantId()
        );
    }

    private String getCurrentTenantId() {
        return TenantContext.getCurrentTenant();
    }
} 