# Keranjang Belanja App - Responsi PBO IF-E 2025/2026

## Yang Dilakukan

### 1. Penyimpanan Data ke MySQL
Data yang sebelumnya disimpan sementara di `FakeCartRepository` (ArrayList di memori) sekarang disimpan secara persisten ke database MySQL menggunakan `MySQLCartRepository`.

### 2. Fitur Diskon Event 12.12 (12%)
Dibuat kelas `EventDiscountStrategy` yang mengimplementasikan interface `DiscountStrategy`, memberikan potongan harga sebesar **12%** dari subtotal belanja, menggantikan `NoDiscountStrategy` yang sebelumnya selalu mengembalikan diskon Rp0.

### 3. Pola Arsitektur: MVP (Model-View-Presenter)

```
Responsi.java (Presenter / Entry Point)
    │
    ├── CartView              → Menampilkan UI, menerima input user (Passive View)
    │
    ├── CartRepository        → Interface kontrak data (Model layer)
    │   ├── FakeCartRepository    (implementasi lama, in-memory)
    │   └── MySQLCartRepository   (implementasi baru, MySQL) ← TAMBAHAN
    │
    ├── DiscountStrategy      → Interface strategi diskon (Strategy Pattern)
    │   ├── NoDiscountStrategy    (implementasi lama, diskon Rp0)
    │   └── EventDiscountStrategy (implementasi baru, diskon 12%) ← TAMBAHAN
    │
    └── DatabaseConnection    → Singleton manajemen koneksi MySQL ← TAMBAHAN
```

### 4. File yang Dimodifikasi & Ditambahkan

| File | Keterangan |
|---|---|
| `Responsi.java` | **DIMODIFIKASI** — ganti `FakeCartRepository` → `MySQLCartRepository`, ganti `NoDiscountStrategy` → `EventDiscountStrategy`, tambah inisialisasi DB |
| `pom.xml` | **DIMODIFIKASI** — tambah dependency `mysql-connector-j` dan `maven-assembly-plugin` |
| `database/DatabaseConnection.java` | **BARU** — Singleton manajemen koneksi MySQL, auto-create DB & tabel |
| `repository/MySQLCartRepository.java` | **BARU** — implementasi CRUD ke MySQL via JDBC |
| `service/EventDiscountStrategy.java` | **BARU** — strategi diskon Event 12.12 sebesar 12% |
| `README.md` | **BARU** — file ini |

> File lain (`CartView`, `CartRepository`, `FakeCartRepository`, `CartItemDTO`, `DiscountStrategy`, `NoDiscountStrategy`) **tidak diubah sama sekali**.

---

## Cara Menjalankan

### Prasyarat
- Java 21+
- Maven 3.x
- MySQL Server 8.x (aktif/running)

### 1. Konfigurasi Password MySQL

Buka `src/main/java/com/pbo/responsi/database/DatabaseConnection.java`, ubah:

```java
private static final String DB_PASS = "";  // ← isi password MySQL kamu di sini
```

> Database `cart_db` dan tabel `cart_items` akan **dibuat otomatis** saat pertama kali dijalankan.

### 2. Buka di NetBeans

1. **File → Open Project** → pilih folder project ini
2. Klik kanan project → **Clean and Build**
3. Klik kanan project → **Run** (atau tekan F6)

---

## Skema Tabel Database

```sql
CREATE TABLE `cart_items` (
    `id`         INT AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL UNIQUE,
    `price`      DOUBLE NOT NULL,
    `quantity`   INT NOT NULL DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## Fitur Aplikasi (sama persis dengan sebelumnya)

| Tombol | Fungsi |
|---|---|
| **Tambah ke Keranjang** | Menambah barang baru ke keranjang |
| **Ubah Qty Terpilih** | Mengubah jumlah barang yang dipilih di tabel |
| **Hapus Barang** | Menghapus barang yang dipilih dari keranjang |

Billing section otomatis menampilkan **Subtotal**, **Diskon Event 12.12 (12%)**, dan **Total Bayar**.
