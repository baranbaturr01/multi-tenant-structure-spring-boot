package com.baranbatur.appointment_system.multitenant.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private SettingsService settingsService;

    @GetMapping("/appointment")
    public ResponseEntity<Map<String, Object>> getAppointmentSettings() {
        return ResponseEntity.ok(settingsService.getAppointmentSettings());
    }

    @PutMapping("/appointment")
    public ResponseEntity<?> updateAppointmentSettings(@RequestBody Map<String, String> request) {
        try {
            settingsService.updateAppointmentSettings(
                Integer.parseInt(request.get("reminderBeforeMinutes")),
                Boolean.parseBoolean(request.get("smsEnabled")),
                Boolean.parseBoolean(request.get("emailEnabled"))
            );
            return ResponseEntity.ok().body("Randevu ayarları başarıyla güncellendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Randevu ayarları güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/working-hours")
    public ResponseEntity<?> updateWorkingHours(@RequestBody Map<String, String> request) {
        try {
            settingsService.updateWorkingHours(
                request.get("startTime"),
                request.get("endTime"),
                Integer.parseInt(request.get("appointmentInterval"))
            );
            return ResponseEntity.ok().body("Çalışma saatleri başarıyla güncellendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Çalışma saatleri güncellenirken hata oluştu: " + e.getMessage());
        }
    }
} 