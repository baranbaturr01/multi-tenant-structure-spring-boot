package com.baranbatur.appointment_system.multitenant.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginRequest {
    private String username;
    private String password;
    // getters/setters
}