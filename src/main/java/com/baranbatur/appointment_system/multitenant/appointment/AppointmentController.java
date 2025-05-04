package com.baranbatur.appointment_system.multitenant.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/available-slots")
    public ResponseEntity<List<Map<String, Object>>> getAvailableTimeSlots(
            @RequestParam String date,
            @RequestParam int chairId) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return ResponseEntity.ok(appointmentService.getAvailableTimeSlots(dateTime, chairId));
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Map<String, String> request) {
        try {
            appointmentService.createAppointment(
                request.get("customerName"),
                request.get("customerEmail"),
                request.get("customerPhone"),
                Integer.parseInt(request.get("chairId")),
                Integer.parseInt(request.get("staffId")),
                LocalDateTime.parse(request.get("startTime")),
                LocalDateTime.parse(request.get("endTime"))
            );
            return ResponseEntity.ok().body("Randevu başarıyla oluşturuldu");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Randevu oluşturulurken hata oluştu: " + e.getMessage());
        }
    }
} 