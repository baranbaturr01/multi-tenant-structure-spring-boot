package com.baranbatur.appointment_system.multitenant.appointments;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AppointmentRepository {

    private final DataSource dataSource;

    public AppointmentRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO appointments (customer_name, appointment_time, chair_number) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, appointment.getCustomerName());
            preparedStatement.setObject(2, appointment.getAppointmentTime());
            preparedStatement.setInt(3, appointment.getChairNumber());
            preparedStatement.executeUpdate();
        }
    }

    // Henüz hatırlatma gönderilmemiş ve zamanı 30dk içinde olanları getir
    @Query("SELECT a FROM Appointment a WHERE a.appointmentTime BETWEEN :from AND :to AND a.reminderSent = false")
    List<Appointment> findPendingReminders(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    ) {
        return null;
    }

    @Modifying
    @Query("UPDATE Appointment a SET a.reminderSent = true WHERE a.id = :id")
    void markReminderSent(@Param("id") Long id) {

    }
}