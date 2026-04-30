# Product CRUD — Spring Boot + JPA + PostgreSQL

Aplikasi CRUD produk dengan autentikasi pengguna, manajemen kategori, dan fitur kepemilikan produk (*product ownership*), dibangun menggunakan Spring Boot, Spring Data JPA, Thymeleaf, dan PostgreSQL.  
Dibuat sebagai boilerplate untuk Final Project Pemrograman Web II (2) B.

## Tech Stack

- Java 21
- Spring Boot 3.2.5
- Spring Security
- Spring Data JPA (Hibernate)
- Thymeleaf + Bootstrap 5
- PostgreSQL (Neon)
- Gradle

## Struktur Project

```
src/main/java/com/example/productcrud/
├── ProductCrudApplication.java
├── config/
│   ├── AuthPageFilter.java              # Filter redirect halaman auth
│   └── SecurityConfig.java             # Konfigurasi Spring Security
├── controller/
│   ├── AuthController.java             # Login & register
│   ├── CategoryController.java         # CRUD kategori
│   ├── DashboardController.java        # Halaman dashboard
│   ├── ProductController.java          # CRUD produk
│   └── ProfileController.java          # Profil & ganti password
├── dto/
│   ├── ChangePasswordRequest.java      # DTO ganti password
│   └── RegisterRequest.java            # DTO registrasi user
├── model/
│   ├── Category.java                   # JPA Entity kategori
│   ├── Product.java                    # JPA Entity produk
│   └── User.java                       # JPA Entity user
├── repository/
│   ├── CategoryRepository.java
│   ├── ProductRepository.java
│   └── UserRepository.java
└── service/
    ├── CategoryService.java
    ├── CustomUserDetailsService.java   # Implementasi UserDetailsService
    └── ProductService.java

src/main/resources/
├── application.properties
└── templates/
    ├── index.html                      # Landing page
    ├── login.html
    ├── register.html
    ├── fragments/
    │   └── layout.html                 # Navbar, header, footer
    ├── category/
    │   ├── form.html                   # Form tambah/edit kategori
    │   └── list.html                   # Daftar kategori
    ├── dashboard/
    │   └── index.html                  # Halaman dashboard
    ├── product/
    │   ├── detail.html
    │   ├── form.html                   # Form tambah/edit produk
    │   └── list.html
    └── profile/
        ├── change-password.html
        ├── edit.html
        └── view.html
```

## Setup

### 1. Clone repository

```bash
git clone https://github.com/<username>/product-crud.git
cd product-crud
```

### 2. Siapkan database PostgreSQL

Buat database PostgreSQL (bisa pakai Neon, Vercel Postgres, atau lokal).

### 3. Buat file `.env`

Salin `.env.example` dan isi dengan kredensial database:

```bash
cp .env.example .env
```

```env
PGHOST=your-database-host
PGUSER=your-username
PGDATABASE=your-database-name
PGPASSWORD=your-password
```

### 4. Jalankan aplikasi

```bash
./gradlew bootRun
```

Buka http://localhost:8080

### 5. (Opsional) Seed data

Jalankan SQL berikut untuk mengisi data awal:

```sql
-- Kategori
INSERT INTO categories (name) VALUES
('Elektronik'), ('Buku'), ('Makanan'), ('Pakaian');

-- User (password: 'password' — hash sesuai BCrypt)
INSERT INTO users (full_name, username, email, password) VALUES
('IniUser', 'user1', 'user1@example.com', '$2a$10$...');

-- Produk
INSERT INTO products (name, category_id, price, stock, description, active, created_at, user_id) VALUES
('Laptop ASUS ROG', 1, 18500000, 8, 'Laptop gaming dengan prosesor terbaru dan kartu grafis RTX', true, '2025-01-15', 1),
('Mouse Logitech MX Master', 1, 1200000, 35, 'Mouse wireless ergonomis dengan sensor presisi tinggi', true, '2025-02-10', 1),
('Buku Java Programming', 2, 150000, 30, 'Panduan lengkap pemrograman Java dari dasar hingga mahir', true, '2025-03-05', 2),
('Kopi Arabica Toraja 250g', 3, 85000, 100, 'Kopi arabica premium dari Toraja dengan cita rasa khas', true, '2025-04-20', 2),
('Headphone Sony WH-1000XM5', 1, 4500000, 15, 'Headphone wireless dengan noise cancelling terbaik di kelasnya', true, '2025-05-01', 1),
('Kemeja Batik Premium', 4, 350000, 50, 'Kemeja batik premium motif parang dengan bahan katun berkualitas', false, '2025-06-12', 2);
```

## Endpoints

### Auth

| Method | URL | Fungsi |
|--------|-----|--------|
| GET | `/login` | Halaman login |
| POST | `/login` | Proses login |
| GET | `/register` | Halaman registrasi |
| POST | `/register` | Proses registrasi |
| POST | `/logout` | Logout |

### Dashboard

| Method | URL | Fungsi |
|--------|-----|--------|
| GET | `/dashboard` | Halaman utama setelah login |

### Produk

| Method | URL | Fungsi |
|--------|-----|--------|
| GET | `/products` | Daftar produk |
| GET | `/products/{id}` | Detail produk |
| GET | `/products/new` | Form tambah produk |
| GET | `/products/{id}/edit` | Form edit produk |
| POST | `/products/save` | Simpan produk (create/update) |
| POST | `/products/{id}/delete` | Hapus produk |

### Kategori

| Method | URL | Fungsi |
|--------|-----|--------|
| GET | `/categories` | Daftar kategori |
| GET | `/categories/new` | Form tambah kategori |
| GET | `/categories/{id}/edit` | Form edit kategori |
| POST | `/categories/save` | Simpan kategori |
| POST | `/categories/{id}/delete` | Hapus kategori |

### Profil

| Method | URL | Fungsi |
|--------|-----|--------|
| GET | `/profile` | Lihat profil |
| GET | `/profile/edit` | Form edit profil |
| POST | `/profile/edit` | Simpan perubahan profil |
| GET | `/profile/change-password` | Form ganti password |
| POST | `/profile/change-password` | Simpan password baru |

## Arsitektur

```
Browser → Controller → Service → Repository → PostgreSQL
                ↑
         Spring Security
       (AuthPageFilter + SecurityConfig)
```

- **SecurityConfig** — konfigurasi autentikasi, otorisasi, dan route yang dilindungi
- **AuthPageFilter** — redirect user yang sudah login agar tidak mengakses halaman auth
- **Controller** — menerima HTTP request, mengembalikan Thymeleaf template
- **Service** — business logic dan validasi
- **Repository** — akses database via Spring Data JPA
- **Entity** — `User`, `Product`, `Category` dengan relasi JPA

## Branch

| Branch | Deskripsi |
|--------|-----------|
| `main` | Boilerplate in-memory (ArrayList) + Category enum |
| `feature/jpa-implementation` | Implementasi JPA + PostgreSQL |
| `feature/product-ownership` | Autentikasi, manajemen user, kepemilikan produk |