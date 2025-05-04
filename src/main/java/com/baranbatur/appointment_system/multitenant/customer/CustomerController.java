package com.baranbatur.appointment_system.multitenant.customer;

import com.baranbatur.appointment_system.multitenant.appointment.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('CUSTOMER')")
public class CustomerController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/available-slots")
    public ResponseEntity<List<Map<String, Object>>> getAvailableTimeSlots(
            @RequestParam String date,
            @RequestParam int chairId) {
        LocalDateTime dateTime = LocalDateTime.parse(date);
        return ResponseEntity.ok(appointmentService.getAvailableTimeSlots(dateTime, chairId));
    }

    @PostMapping("/appointments")
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

    @GetMapping("/appointments")
    public ResponseEntity<List<Map<String, Object>>> getCustomerAppointments(
            @RequestParam String customerEmail) {
        return ResponseEntity.ok(appointmentService.getCustomerAppointments(customerEmail));
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable int id, @RequestBody Map<String, String> request) {
        try {
            appointmentService.updateAppointment(
                id,
                request.get("customerName"),
                request.get("customerEmail"),
                request.get("customerPhone"),
                Integer.parseInt(request.get("chairId")),
                Integer.parseInt(request.get("staffId")),
                LocalDateTime.parse(request.get("startTime")),
                LocalDateTime.parse(request.get("endTime"))
            );
            return ResponseEntity.ok().body("Randevu başarıyla güncellendi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Randevu güncellenirken hata oluştu: " + e.getMessage());
        }
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable int id) {
        try {
            appointmentService.cancelAppointment(id);
            return ResponseEntity.ok().body("Randevu başarıyla iptal edildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Randevu iptal edilirken hata oluştu: " + e.getMessage());
        }
    }
} 