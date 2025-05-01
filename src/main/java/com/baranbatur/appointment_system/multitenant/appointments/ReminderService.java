package com.baranbatur.appointment_system.multitenant.appointments;

import com.baranbatur.appointment_system.multitenant.central.entity.Tenant;
import com.baranbatur.appointment_system.multitenant.central.repository.TenantRepository;
import com.baranbatur.appointment_system.multitenant.context.TenantContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReminderService {

    private final TenantRepository tenantRepository;        // central DB
    private final AppointmentRepository appointmentRepo;    // tenant DB (RoutingDataSource)
    private final SmsSender smsSender;                      // SMS servisi
    private final EmailSender emailSender;                  // E-posta servisi

    public ReminderService(TenantRepository tenantRepo,
                           AppointmentRepository appointmentRepo,
                           SmsSender smsSender,
                           EmailSender emailSender) {
        this.tenantRepository = tenantRepo;
        this.appointmentRepo = appointmentRepo;
        this.smsSender = smsSender;
        this.emailSender = emailSender;
    }

    // Her 1 dakikada çalışsın
    @Scheduled(fixedRate = 60_000)
    public void sendReminders() {
        List<Tenant> tenants = tenantRepository.findAll();
        for (Tenant t : tenants) {
            TenantContext.setCurrentTenant(t.getName());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime in30 = now.plusMinutes(30);

            List<Appointment> pending = appointmentRepo.findPendingReminders(now, in30);
            for (Appointment a : pending) {
                String msg = String.format("Merhaba %s, randevunuz %s tarihinde %d numaralı koltuk için. 30 dk içinde sizi bekliyoruz!",
                        a.getCustomerName(),
                        a.getAppointmentTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        a.getChairNumber());
                smsSender.send(a.getCustomerPhone(), msg);
                emailSender.send(a.getCustomerEmail(), "Randevu Hatırlatma", msg);

                appointmentRepo.markReminderSent(a.getId());
            }
            TenantContext.clear();
        }
    }
}