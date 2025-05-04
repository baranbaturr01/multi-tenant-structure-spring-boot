package com.baranbatur.appointment_system.multitenant.superadmin;

import com.baranbatur.appointment_system.multitenant.security.Role;
import com.baranbatur.appointment_system.multitenant.tenant.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/super-admin")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class SuperAdminController {

    @Autowired
    private TenantService tenantService;

    @PostMapping("/tenants/register")
    public ResponseEntity<?> registerTenant(@RequestBody Map<String, String> request) {
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
            return ResponseEntity.ok().body("Berber başarıyla kaydedildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Berber kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @GetMapping("/tenants")
    public ResponseEntity<List<Map<String, Object>>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/tenants/{subdomain}")
    public ResponseEntity<Map<String, Object>> getTenantBySubdomain(@PathVariable String subdomain) {
        try {
            return ResponseEntity.ok(tenantService.getTenantBySubdomain(subdomain));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
} 