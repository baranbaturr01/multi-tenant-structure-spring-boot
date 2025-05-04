package com.baranbatur.appointment_system.multitenant.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginResponse {
    private String token;

    public String getToken() {
        return token;
    }
}
