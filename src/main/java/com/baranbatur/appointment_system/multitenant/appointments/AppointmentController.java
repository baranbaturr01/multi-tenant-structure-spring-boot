package com.baranbatur.appointment_system.multitenant.appointments;


import com.baranbatur.appointment_system.multitenant.appointments.dto.requests.AppointmentRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Appointment appointment = new Appointment(
                appointmentRequest.getCustomerName(),
                appointmentRequest.getAppointmentTime(),
                appointmentRequest.getChairNumber()
        );

        try {
            appointmentService.createAppointment(appointment);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating appointment", e);
        }
    }
}
