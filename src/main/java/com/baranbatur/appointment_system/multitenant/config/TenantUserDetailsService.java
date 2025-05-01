package com.baranbatur.appointment_system.multitenant.config;

import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import com.baranbatur.appointment_system.multitenant.user.User;
import com.baranbatur.appointment_system.multitenant.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TenantUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public TenantUserDetailsService(UserRepository repo) {
        this.userRepo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String tenant = TenantContext.getCurrentTenant();
        // (RoutingDataSource sayesinde doğru repo kullanılıyor)
        User u = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.withUsername(u.getUsername())
                .password(u.getPassword())
                .roles(u.getRole())
                .build();
    }
}