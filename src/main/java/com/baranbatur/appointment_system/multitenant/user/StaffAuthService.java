package com.baranbatur.appointment_system.multitenant.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StaffAuthService {

    private final StaffRepository repo;
    private final PasswordEncoder encoder;

    public StaffAuthService(StaffRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void register(String username, String rawPassword, String role) {
        if (repo.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Kullanıcı zaten kayıtlı");
        }
        Staff s = Staff.builder()
                .username(username)
                .password(encoder.encode(rawPassword))
                .role(role)
                .build();
        repo.save(s);
    }
}