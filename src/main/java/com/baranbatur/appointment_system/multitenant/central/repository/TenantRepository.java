package com.baranbatur.appointment_system.multitenant.central.repository;

import com.baranbatur.appointment_system.multitenant.central.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByName(String name);

}
