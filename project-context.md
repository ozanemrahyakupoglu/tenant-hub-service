# Project Context

## 1. Genel Proje Tanımı & Amaç

**TenantHub**, kiracı ve ev sahibi yönetimini merkezi olarak sağlayan bir gayrimenkul kira yönetim platformudur. Sistem; kullanıcı, gayrimenkul, kiralama ve ödeme süreçlerini uçtan uca yönetir. Backend REST API olarak tasarlanmış olup JWT tabanlı kimlik doğrulama ve rol/yetki bazlı erişim kontrolü içerir.

- **Repo:** `tenant-hub-service`
- **Group ID:** `com.obntech`
- **Port:** `8080`
- **API Base Path:** `/api/v1`

---

## 2. Kullanıcı Rolleri & İzinler

### Roller
| Rol | Açıklama |
|---|---|
| `ADMIN` | Tam yetki |
| `MANAGER` | Yönetim yetkileri |
| `VIEWER` | Sadece okuma |

### Permission Yapısı
Permission'lar `{MODÜL}_{ACTION}` formatındadır. Her modül için CREATE, READ, UPDATE, DELETE yetkileri tanımlanmıştır.

| Modül | Permission'lar |
|---|---|
| USER | `USER_CREATE`, `USER_READ`, `USER_UPDATE`, `USER_DELETE` |
| ROLE | `ROLE_CREATE`, `ROLE_READ`, `ROLE_UPDATE`, `ROLE_DELETE` |
| PERMISSION | `PERMISSION_CREATE`, `PERMISSION_READ`, `PERMISSION_UPDATE`, `PERMISSION_DELETE` |
| REAL_ESTATE | `REAL_ESTATE_CREATE`, `REAL_ESTATE_READ`, `REAL_ESTATE_UPDATE`, `REAL_ESTATE_DELETE` |
| RENT | `RENT_CREATE`, `RENT_READ`, `RENT_UPDATE`, `RENT_DELETE` |
| PAYMENT | `PAYMENT_CREATE`, `PAYMENT_READ`, `PAYMENT_UPDATE`, `PAYMENT_DELETE` |

### Auth Akışı
- `POST /api/v1/auth/login` → `accessToken` (body) + `refreshToken` (HttpOnly cookie)
- `POST /api/v1/auth/refresh` → Cookie'den refreshToken okur, yeni token çifti döner
- `POST /api/v1/auth/logout` → refreshToken cookie'yi siler
- Login response'da `roles` ve `permissions` alanları dönülür → frontend ekran yetkilendirmesi için kullanılır
- JWT access token claim'leri: `sub`, `roles[]`, `permissions[]`

---

## 3. Mimari Kararlar & Prensipler

- **Katmanlı Mimari:** Controller → Service → Repository → Entity
- **Mapper Pattern:** Manuel mapper sınıfları (`*Mapper.java`), MapStruct kullanılmıyor
- **Exception Handling:** `GlobalExceptionHandler` + `BusinessException` + `ErrorCode` enum ile merkezi hata yönetimi. RFC 7807 `ProblemDetail` formatında response dönülür
- **Method Security:** `@EnableMethodSecurity` + `@PreAuthorize("hasAuthority('...')")` ile endpoint bazlı yetkilendirme
- **Stateless Session:** JWT ile stateless, session yok
- **History Tabloları:** Her tablo için `_HIS` suffix'li history tablosu mevcut. INSERT/UPDATE/DELETE trigger'ları ile otomatik doldurulur
- **Audit Alanları:** Her tablo `BaseEntity`'den `createdBy`, `createdDate`, `createdIp`, `updatedBy`, `updatedDate`, `updatedIp` alanlarını miras alır
- **CORS:** `http://localhost:5173` origin'ine izin verilmiş, `allowCredentials: true`
- **Sequence:** Her tablo için ayrı PostgreSQL sequence (`SEQ_{TABLO_ADI}`)

---

## 4. Bileşenler

### 4.1 Database

#### Teknoloji Stack'i
- **Veritabanı:** PostgreSQL
- **Schema:** `devuser`
- **Migration:** Flyway (`src/main/resources/db/migration/V{n}__{açıklama}.sql`)

#### Temel Entity'ler & Şema

```
USERS ──< USERS_ROLE >── ROLE ──< ROLE_PERMISSION >── PERMISSION
USERS (tenant/landlord) ──< REAL_ESTATE
REAL_ESTATE ──< RENT ──< PAYMENT
```

| Tablo | Migration | Açıklama |
|---|---|---|
| `USERS` | V1 | Kullanıcılar |
| `ROLE` | V1 | Roller |
| `PERMISSION` | V1 | Yetkiler |
| `USERS_ROLE` | V1 | Kullanıcı-Rol ilişkisi |
| `ROLE_PERMISSION` | V1 | Rol-Yetki ilişkisi |
| `REAL_ESTATE` | V2 | Gayrimenkuller |
| `REAL_ESTATE` (alter) | V3 | tenant_id, landlord_id FK eklendi |
| `RENT` | V4 | Kiralama kayıtları |
| `PAYMENT` | V5 | Ödeme kayıtları |

#### Kolon Standartları
- Her tablo: `ID`, `STATUS`, `CREATED_BY`, `CREATED_DATE`, `CREATED_IP`, `UPDATED_BY`, `UPDATED_DATE`, `UPDATED_IP`
- History tabloları ek olarak: `ORIGINAL_ID`, `DML_OPERATION`, `DML_BY`, `DML_DATE`, `DML_IP`
- Status değerleri: `ACTIVE`, `INACTIVE` (USERS tablosunda ayrıca `BLOCKED`)
- Para tutarları: `NUMERIC(15,2)`

---

### 4.2 Backend

#### Teknoloji Stack'i
- **Java:** 21
- **Framework:** Spring Boot 3.4.3
- **Security:** Spring Security 6 + JJWT 0.12.6
- **ORM:** Spring Data JPA (Hibernate)
- **Validation:** Jakarta Validation
- **Build:** Maven
- **Utilities:** Lombok

#### API Yapısı & Endpoint Konvansiyonları

```
GET    /api/v1/{resource}                      → Sayfalı liste
GET    /api/v1/{resource}/{id}                 → Tekil kayıt
GET    /api/v1/{resource}/status/{status}      → Statüye göre liste
POST   /api/v1/{resource}                      → Oluştur (201)
PUT    /api/v1/{resource}/{id}                 → Güncelle
PATCH  /api/v1/{resource}/{id}/status/{status} → Statü değiştir (204)
DELETE /api/v1/{resource}/{id}                 → Sil (204)
```

| Resource | Base Path |
|---|---|
| Auth | `/api/v1/auth` |
| Users | `/api/v1/users` |
| Roles | `/api/v1/roles` |
| Permissions | `/api/v1/permissions` |
| User-Roles | `/api/v1/user-roles` |
| Role-Permissions | `/api/v1/role-permissions` |
| Real Estates | `/api/v1/real-estates` |
| Rents | `/api/v1/rents` |
| Payments | `/api/v1/payments` |

#### Mimari Notlar
- Response formatı hata durumunda RFC 7807 `ProblemDetail` (title, status, detail, instance, code)
- Sayfalama: Spring Data `Pageable` (`?page=0&size=20&sort=field,asc`)
- `createdBy` / `updatedBy` şu an `"SYSTEM"` hardcode — ileride JWT principal'dan alınacak
- `@Transactional(readOnly = true)` okuma, `@Transactional` yazma metotlarında kullanılıyor

---

### 4.3 Web Frontend

> Bu proje backend API'dir, frontend kodu barındırmaz.

---

### 4.4 Mobile

> Bu proje backend API'dir, mobil uygulama kodu barındırmaz.

---

## 5. Mevcut Modüller & Özellikler

| Modül | Durum | Notlar |
|---|---|---|
| Auth (Login/Refresh/Logout) | ✅ Tamamlandı | Cookie tabanlı refreshToken |
| JWT Rol & Yetki Yönetimi | ✅ Tamamlandı | Token claim'leri + login response |
| Kullanıcı CRUD | ✅ Tamamlandı | |
| Rol CRUD | ✅ Tamamlandı | |
| Yetki CRUD | ✅ Tamamlandı | |
| Kullanıcı-Rol Yönetimi | ✅ Tamamlandı | |
| Rol-Yetki Yönetimi | ✅ Tamamlandı | |
| Gayrimenkul CRUD | ✅ Tamamlandı | tenant_id, landlord_id FK ile |
| Kiralama CRUD | ✅ Tamamlandı | real_estate_id FK ile |
| Ödeme CRUD | ✅ Tamamlandı | rent_id FK ile, bir rent'e N ödeme |
| Endpoint Yetkilendirme | ✅ Tamamlandı | @PreAuthorize her controller'da |
| Global Hata Yönetimi | ✅ Tamamlandı | 403, 404, 409, 500 |
| History Trigger'ları | ✅ Tamamlandı | Tüm tablolarda |
| Frontend | ⏳ Planlandı | React + Vite |
| createdBy JWT'den okuma | ⏳ Planlandı | Şu an hardcode "SYSTEM" |
