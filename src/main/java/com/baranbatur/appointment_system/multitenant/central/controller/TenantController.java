package com.baranbatur.appointment_system.multitenant.central.controller;

import com.baranbatur.appointment_system.multitenant.central.service.TenantService;
import com.baranbatur.appointment_system.multitenant.dto.TenantRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerTenant(@RequestBody TenantRegistrationRequest req) {
        tenantService.registerTenant(req);
    }
}