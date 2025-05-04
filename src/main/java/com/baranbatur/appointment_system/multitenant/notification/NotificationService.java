package com.baranbatur.appointment_system.multitenant.notification;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {

    public void sendEmailReminder(Map<String, Object> appointment) {
        String email = (String) appointment.get("customer_email");
        String customerName = (String) appointment.get("customer_name");
        String chairName = (String) appointment.get("chair_name");
        String staffName = (String) appointment.get("staff_name");
        String startTime = appointment.get("start_time").toString();

        // Email gönderme işlemi burada yapılacak
        String subject = "Randevu Hatırlatması";
        String message = String.format(
            "Sayın %s,\n\n" +
            "%s tarihinde %s numaralı koltuğa randevunuz bulunmaktadır.\n" +
            "Berber: %s\n\n" +
            "İyi günler dileriz.",
            customerName, startTime, chairName, staffName
        );

        // TODO: Email gönderme implementasyonu
        System.out.println("Email gönderiliyor: " + email);
        System.out.println("Konu: " + subject);
        System.out.println("Mesaj: " + message);
    }

    public void sendSmsReminder(Map<String, Object> appointment) {
        String phone = (String) appointment.get("customer_phone");
        String customerName = (String) appointment.get("customer_name");
        String chairName = (String) appointment.get("chair_name");
        String staffName = (String) appointment.get("staff_name");
        String startTime = appointment.get("start_time").toString();

        // SMS gönderme işlemi burada yapılacak
        String message = String.format(
            "Sayın %s, %s tarihinde %s numaralı koltuğa randevunuz var. Berber: %s",
            customerName, startTime, chairName, staffName
        );

        // TODO: SMS gönderme implementasyonu
        System.out.println("SMS gönderiliyor: " + phone);
        System.out.println("Mesaj: " + message);
    }
} 