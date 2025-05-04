package com.baranbatur.appointment_system.multitenant.management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    @Autowired
    private ChairService chairService;

    @Autowired
    private StaffService staffService;

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
} 