package com.baranbatur.appointment_system.multitenant.appointments;


import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public void createAppointment(Appointment appointment) throws SQLException {
        appointmentRepository.save(appointment);
    }
}