package com.baranbatur.appointment_system.multitenant.tenantadmin;

import com.baranbatur.appointment_system.multitenant.management.ChairService;
import com.baranbatur.appointment_system.multitenant.management.StaffService;
import com.baranbatur.appointment_system.multitenant.settings.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tenant")
@PreAuthorize("hasRole('TENANT_ADMIN')")
public class TenantAdminController {

    @Autowired
    private ChairService chairService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private SettingsService settingsService;

    // Koltuk işlemleri
    @GetMapping("/chairs")
    public ResponseEntity<List<Map<String, Object>>> getAllChairs() {
        return ResponseEntity.ok(chairService.getAllChairs());
    }

    @PostMapping("/chairs")
    public ResponseEntity<?> createChair(@RequestBody Map<String, String> request) {
        try {
            chairService.createChair(
                Integer.parseInt(request.get("number")),
                request.get("name"),
                request.get("description")
            );
            return ResponseEntity.ok().body("Koltuk başarıyla oluşturuldu");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Koltuk oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/chairs/{id}")
    public ResponseEntity<?> updateChair(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            chairService.updateChair(
                id,
                Integer.parseInt(request.get("number")),
                request.get("name"),
                request.get("description"),
                Boolean.parseBoolean(request.get("isActive"))
            );
            return ResponseEntity.ok().body("Koltuk başarıyla güncellendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Koltuk güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    @DeleteMapping("/chairs/{id}")
    public ResponseEntity<?> deleteChair(@PathVariable int id) {
        try {
            chairService.deleteChair(id);
            return ResponseEntity.ok().body("Koltuk başarıyla silindi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Koltuk silinirken hata oluştu: " + e.getMessage());
        }
    }

    // Personel işlemleri
    @GetMapping("/staff")
    public ResponseEntity<List<Map<String, Object>>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @PostMapping("/staff")
    public ResponseEntity<?> createStaff(@RequestBody Map<String, String> request) {
        try {
            staffService.createStaff(
                request.get("name"),
                request.get("email"),
                request.get("phone"),
                request.get("chairId") != null ? Integer.parseInt(request.get("chairId")) : null
            );
            return ResponseEntity.ok().body("Personel başarıyla oluşturuldu");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Personel oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @PutMapping("/staff/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            staffService.updateStaff(
                id,
                request.get("name"),
                request.get("email"),
                request.get("phone"),
                request.get("chairId") != null ? Integer.parseInt(request.get("chairId")) : null,
                Boolean.parseBoolean(request.get("isActive"))
            );
            return ResponseEntity.ok().body("Personel başarıyla güncellendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Personel güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    @DeleteMapping("/staff/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable int id) {
        try {
            staffService.deleteStaff(id);
            return ResponseEntity.ok().body("Personel başarıyla silindi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Personel silinirken hata oluştu: " + e.getMessage());
        }
    }

    // Randevu ayarları
    @GetMapping("/appointment-settings")
    public ResponseEntity<Map<String, Object>> getAppointmentSettings() {
        return ResponseEntity.ok(settingsService.getAppointmentSettings());
    }

    @PutMapping("/appointment-settings")
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

    // Çalışma saatleri
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