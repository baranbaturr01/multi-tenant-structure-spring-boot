package com.baranbatur.appointment_system.multitenant.appointments;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "appintments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customerName;
    private LocalDateTime appointmentTime;
    private int chairNumber;

    public Appointment() {
    }

    public Appointment(String customerName, LocalDateTime appointmentTime, int chairNumber) {
        this.customerName = customerName;
        this.appointmentTime = appointmentTime;
        this.chairNumber = chairNumber;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public int getChairNumber() {
        return chairNumber;
    }

    public void setChairNumber(int chairNumber) {
        this.chairNumber = chairNumber;
    }
}
