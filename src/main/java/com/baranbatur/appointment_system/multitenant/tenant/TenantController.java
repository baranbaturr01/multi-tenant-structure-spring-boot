package com.baranbatur.appointment_system.multitenant.tenant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping
    public ResponseEntity<?> createTenant(@RequestBody Map<String, String> request) {
        try {
            tenantService.createTenant(
                request.get("name"),
                request.get("subdomain"),
                request.get("email"),
                request.get("phone"),
                request.get("address"),
                request.get("workingHoursStart"),
                request.get("workingHoursEnd"),
                Integer.parseInt(request.get("appointmentInterval"))
            );
            return ResponseEntity.ok().body("Tenant başarıyla oluşturuldu");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tenant oluşturulurken hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{subdomain}")
    public ResponseEntity<Map<String, Object>> getTenantBySubdomain(@PathVariable String subdomain) {
        try {
            return ResponseEntity.ok(tenantService.getTenantBySubdomain(subdomain));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 