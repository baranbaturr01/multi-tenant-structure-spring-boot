package com.baranbatur.appointment_system.multitenant.appointment;

import com.baranbatur.appointment_system.multitenant.appointment.model.Appointment;
import com.baranbatur.appointment_system.multitenant.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import com.baranbatur.appointment_system.multitenant.notification.NotificationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<Map<String, Object>> getAvailableTimeSlots(LocalDateTime date, int chairId) {
        // Çalışma saatlerini al
        Map<String, Object> tenant = jdbcTemplate.queryForMap(
            "SELECT working_hours_start, working_hours_end, appointment_interval " +
            "FROM tenants WHERE subdomain = ?",
            TenantContext.getCurrentTenant()
        );

        // Randevu aralıklarını hesapla ve müsait zamanları döndür
        return jdbcTemplate.queryForList(
            "WITH RECURSIVE time_slots AS (" +
            "  SELECT ?::timestamp as start_time, " +
            "         ?::timestamp + (? || ' minutes')::interval as end_time " +
            "  UNION ALL " +
            "  SELECT end_time, end_time + (? || ' minutes')::interval " +
            "  FROM time_slots " +
            "  WHERE end_time < ?::timestamp + '1 day'::interval" +
            ") " +
            "SELECT ts.start_time, ts.end_time " +
            "FROM time_slots ts " +
            "WHERE NOT EXISTS (" +
            "  SELECT 1 FROM appointments a " +
            "  WHERE a.chair_id = ? " +
            "  AND a.start_time = ts.start_time" +
            ") " +
            "ORDER BY ts.start_time",
            date, date, tenant.get("appointment_interval"),
            tenant.get("appointment_interval"), date,
            chairId
        );
    }

    public void createAppointment(String customerName, String customerEmail, String customerPhone,
                                int chairId, int staffId, LocalDateTime startTime, LocalDateTime endTime) {
        Appointment appointment = new Appointment();
        appointment.setCustomerName(customerName);
        appointment.setCustomerEmail(customerEmail);
        appointment.setCustomerPhone(customerPhone);
        appointment.setChairId(chairId);
        appointment.setStaffId(staffId);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus("ACTIVE");
        appointmentRepository.save(appointment);
    }

    @Scheduled(fixedRate = 60000) // Her dakika kontrol et
    public void checkAndSendReminders() {
        // Hatırlatma ayarlarını al
        Map<String, Object> settings = jdbcTemplate.queryForMap(
            "SELECT reminder_before_minutes, sms_enabled, email_enabled " +
            "FROM appointment_settings"
        );

        int reminderMinutes = (int) settings.get("reminder_before_minutes");
        boolean smsEnabled = (boolean) settings.get("sms_enabled");
        boolean emailEnabled = (boolean) settings.get("email_enabled");

        // Hatırlatma gönderilecek randevuları bul
        List<Map<String, Object>> appointments = jdbcTemplate.queryForList(
            "SELECT a.*, c.name as chair_name, s.name as staff_name " +
            "FROM appointments a " +
            "JOIN chairs c ON a.chair_id = c.id " +
            "JOIN staff s ON a.staff_id = s.id " +
            "WHERE a.reminder_sent = false " +
            "AND a.start_time BETWEEN ? AND ?",
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(reminderMinutes)
        );

        // Her randevu için hatırlatma gönder
        for (Map<String, Object> appointment : appointments) {
            if (emailEnabled && appointment.get("customer_email") != null) {
                notificationService.sendEmailReminder(appointment);
            }
            if (smsEnabled && appointment.get("customer_phone") != null) {
                notificationService.sendSmsReminder(appointment);
            }

            // Hatırlatma gönderildi olarak işaretle
            jdbcTemplate.update(
                "UPDATE appointments SET reminder_sent = true WHERE id = ?",
                appointment.get("id")
            );
        }
    }

    public List<Map<String, Object>> getCustomerAppointments(String customerEmail) {
        return appointmentRepository.findByCustomerEmail(customerEmail)
                .stream()
                .map(this::convertToMap)
                .collect(Collectors.toList());
    }

    public void updateAppointment(int id, String customerName, String customerEmail, String customerPhone,
                                int chairId, int staffId, LocalDateTime startTime, LocalDateTime endTime) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        
        appointment.setCustomerName(customerName);
        appointment.setCustomerEmail(customerEmail);
        appointment.setCustomerPhone(customerPhone);
        appointment.setChairId(chairId);
        appointment.setStaffId(staffId);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        
        appointmentRepository.save(appointment);
    }

    public void cancelAppointment(int id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Randevu bulunamadı"));
        
        appointment.setStatus("CANCELLED");
        appointmentRepository.save(appointment);
    }

    private Map<String, Object> convertToMap(Appointment appointment) {
        return Map.of(
            "id", appointment.getId(),
            "customerName", appointment.getCustomerName(),
            "customerEmail", appointment.getCustomerEmail(),
            "customerPhone", appointment.getCustomerPhone(),
            "chairId", appointment.getChairId(),
            "staffId", appointment.getStaffId(),
            "startTime", appointment.getStartTime(),
            "endTime", appointment.getEndTime(),
            "status", appointment.getStatus()
        );
    }
} 