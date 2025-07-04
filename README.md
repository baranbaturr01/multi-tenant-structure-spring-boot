# multi-tenant-structure-spring-boot
1. Berber/Kuaför Kayıt Senaryosu SUPER_ADMIN tarafından
POST /api/super-admin/tenants/register
Request Body:
{
    "subdomain": "berber1",
    "name": "Berber Dükkanı",
    "email": "berber1@example.com",
    "password": "123456"
}

Response Body:
{
    "message": "Berber başarıyla kaydedildi",
    "tenant": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "subdomain": "berber1",
        "name": "Berber Dükkanı",
        "email": "berber1@example.com"
    }
}
2. Berber/Kuaför Giriş Senaryosu
POST /api/auth/login
Request Body:
{
    "email": "berber1@example.com",
    "password": "123456"
}

Response Body:
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "role": "TENANT_ADMIN",
    "tenantId": "550e8400-e29b-41d4-a716-446655440000"
}
3. Berber/Kuaför Kurulum Senaryosu
a) Çalışma Saatlerini Ayarlama:
POST /api/management/working-hours
Request Body:
{
    "monday": {
        "startTime": "09:00",
        "endTime": "18:00",
        "appointmentInterval": 30
    },
    "tuesday": {
        "startTime": "09:00",
        "endTime": "18:00",
        "appointmentInterval": 30
    },
    // ... diğer günler
}

Response Body:
{
    "message": "Çalışma saatleri başarıyla güncellendi"
}
b) Koltuk Ekleme:
POST /api/management/chairs
Request Body:
{
    "name": "Koltuk 1",
    "description": "Saç kesim koltugu"
}

Response Body:
{
    "id": 1,
    "name": "Koltuk 1",
    "description": "Saç kesim koltugu",
    "active": true
}
c) Personel Ekleme:
POST /api/management/staff
Request Body:
{
    "name": "Ahmet Yılmaz",
    "email": "ahmet@berber1.com",
    "phone": "5551234567",
    "chairId": 1
}

Response Body:
{
    "id": 1,
    "name": "Ahmet Yılmaz",
    "email": "ahmet@berber1.com",
    "phone": "5551234567",
    "chairId": 1,
    "active": true
}
d) Randevu Ayarlarını Yapma:
POST /api/management/settings
Request Body:
{
    "reminderBeforeMinutes": 60,
    "smsEnabled": true,
    "emailEnabled": true
}

Response Body:
{
    "message": "Randevu ayarları başarıyla güncellendi"
}

4. Müşteri Randevu Senaryosu
a) Müsait Randevu Saatlerini Görüntüleme:
GET /api/appointments/available-slots?date=2024-03-20&chairId=1

Response Body:
{
    "availableSlots": [
        {
            "startTime": "2024-03-20T09:00:00",
            "endTime": "2024-03-20T09:30:00"
        },
        {
            "startTime": "2024-03-20T09:30:00",
            "endTime": "2024-03-20T10:00:00"
        }
        // ... diğer müsait saatler
    ]
}
b) Randevu Oluşturma:
POST /api/appointments
Request Body:
{
    "customerName": "Mehmet Demir",
    "customerEmail": "mehmet@example.com",
    "customerPhone": "5559876543",
    "chairId": 1,
    "staffId": 1,
    "startTime": "2024-03-20T09:00:00",
    "endTime": "2024-03-20T09:30:00"
}

Response Body:
{
    "id": 1,
    "customerName": "Mehmet Demir",
    "customerEmail": "mehmet@example.com",
    "customerPhone": "5559876543",
    "chairId": 1,
    "staffId": 1,
    "startTime": "2024-03-20T09:00:00",
    "endTime": "2024-03-20T09:30:00",
    "status": "ACTIVE"
}
c) Randevu Görüntüleme:
GET /api/appointments/customer/mehmet@example.com

Response Body:
{
    "appointments": [
        {
            "id": 1,
            "customerName": "Mehmet Demir",
            "chairId": 1,
            "staffId": 1,
            "startTime": "2024-03-20T09:00:00",
            "endTime": "2024-03-20T09:30:00",
            "status": "ACTIVE"
        }
        // ... diğer randevular
    ]
}
d) Randevu Güncelleme:
PUT /api/appointments/1
Request Body:
{
    "startTime": "2024-03-20T10:00:00",
    "endTime": "2024-03-20T10:30:00"
}

Response Body:
{
    "message": "Randevu başarıyla güncellendi"
}
e) Randevu İptal:
DELETE /api/appointments/1

Response Body:
{
    "message": "Randevu başarıyla iptal edildi"
}
5. Otomatik Hatırlatma Sistemi
Bu sistem arka planda çalışır ve endpoint gerektirmez. Sistem:
Her dakika kontrol eder
Yaklaşan randevuları tespit eder
Ayarlara göre SMS veya email gönderir
Hatırlatma gönderilen randevuları işaretler
Bu endpointler ve senaryolar, sistemin temel işlevselliğini kapsamaktadır. Her endpoint için gerekli validasyonlar ve hata durumları da mevcuttur.

Örnek Kullanım Senaryosu:
Berber Kaydı:
Berber "Ahmet Usta" sisteme kaydolur
Subdomain: ahmetusta.bikaresoft.com
Yönetim paneli: ahmetusta.bikaresoft.com/admin
Müşteri paneli: ahmetusta.bikaresoft.com
Berber Kurulumu:
Ahmet Usta yönetim paneline girer
Çalışma saatlerini ayarlar
Koltukları ekler
Personeli ekler
Müşteri Randevu İşlemi:
Müşteri ahmetusta.bikaresoft.com adresine gider
Randevu alır
Randevu günü yaklaşınca hatırlatma alır
Önemli Noktalar:
Her berber/kuaför kendi subdomain'i üzerinden yönetim yapar
Müşteriler berberin subdomain'i üzerinden randevu alır
Her subdomain kendi veritabanını kullanır
Sistem otomatik olarak subdomain'e göre doğru veritabanına bağlanır
Bu yapı sayesinde:
Her berber/kuaför bağımsız çalışır
Müşteriler doğrudan berberin sayfasına gider
Yönetim ve müşteri arayüzleri ayrı tutulur
Sistem ölçeklenebilir ve yönetilebilir kalır