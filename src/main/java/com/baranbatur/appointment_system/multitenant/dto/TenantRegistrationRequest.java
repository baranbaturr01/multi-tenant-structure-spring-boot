package com.baranbatur.appointment_system.multitenant.dto;

import lombok.Data;

@Data
public class TenantRegistrationRequest {
    private String name;          // subdomain
    private String dbName;        // örn: "berberc_db"
    private String dbUsername;    // genelde "postgres"
    private String dbPassword;    // genelde "password"
}