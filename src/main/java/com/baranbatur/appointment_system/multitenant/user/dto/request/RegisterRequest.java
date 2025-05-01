package com.baranbatur.appointment_system.multitenant.user.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String username;
    private String password;
    // getters/setters
}