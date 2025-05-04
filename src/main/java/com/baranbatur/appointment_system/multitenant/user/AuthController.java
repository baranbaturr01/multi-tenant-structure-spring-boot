package com.baranbatur.appointment_system.multitenant.user;

import com.baranbatur.appointment_system.multitenant.security.JwtUtil;
import com.baranbatur.appointment_system.multitenant.user.dto.request.LoginRequest;
import com.baranbatur.appointment_system.multitenant.user.dto.request.RegisterRequest;
import com.baranbatur.appointment_system.multitenant.user.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff")
public class AuthController {

    private final StaffAuthService staffAuthService;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(StaffAuthService staffAuthService,
                          AuthenticationManager authManager,
                          JwtUtil jwtUtil) {
        this.staffAuthService = staffAuthService;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest req) {
        staffAuthService.register(req.getUsername(), req.getPassword(), "STAFF");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        String token = jwtUtil.generateToken(auth.getName());
        return ResponseEntity.ok(new LoginResponse(token));
    }


}