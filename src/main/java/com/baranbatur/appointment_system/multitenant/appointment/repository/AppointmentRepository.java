package com.baranbatur.appointment_system.multitenant.appointment.repository;

import com.baranbatur.appointment_system.multitenant.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    
    List<Appointment> findByCustomerEmail(String customerEmail);
    
    @Query("SELECT a FROM Appointment a WHERE a.chairId = :chairId AND a.startTime >= :startDate AND a.endTime <= :endDate")
    List<Appointment> findAvailableTimeSlots(@Param("chairId") int chairId,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);
} 