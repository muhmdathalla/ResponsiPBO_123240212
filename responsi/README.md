# Responsi PBO - Keranjang Belanja

## Apa yang saya kerjakan

Saya menambahkan koneksi ke database MySQL supaya data keranjang tidak hilang waktu aplikasi ditutup. Selain itu saya juga menambahkan diskon Event 12.12 sebesar 12% dari subtotal.

Pola arsitektur yang saya pakai adalah **MVP (Model-View-Presenter)**. `CartView` hanya urusin tampilan, logika bisnisnya ada di `Responsi.java` sebagai presenter, dan data diakses lewat `CartRepository`.

## Struktur Folder

```
src/main/java/com/pbo/responsi/
├── Responsi.java                        # entry point (dimodifikasi)
├── database/
│   └── DatabaseConnection.java          # koneksi MySQL (baru)
├── dto/
│   └── CartItemDTO.java
├── model/
│   ├── CartRepository.java              # interface
│   └── FakeCartRepository.java
├── repository/
│   └── MySQLCartRepository.java         # implementasi MySQL (baru)
├── service/
│   ├── DiscountStrategy.java            # interface
│   ├── NoDiscountStrategy.java
│   └── EventDiscountStrategy.java       # diskon 12% (baru)
└── view/
    └── CartView.java
```

## File yang saya tambahkan

### `database/DatabaseConnection.java`

Singleton untuk manajemen koneksi MySQL. Database dan tabel dibuat otomatis kalau belum ada.

```java
public static DatabaseConnection getInstance() {
    if (instance == null) {
        instance = new DatabaseConnection();
    }
    return instance;
}
```

### `repository/MySQLCartRepository.java`

Implementasi `CartRepository` yang menyimpan data ke MySQL lewat JDBC, menggantikan `FakeCartRepository`.

```java
public void save(CartItemDTO item) {
    String sql = "INSERT INTO cart_items (name, price, quantity) VALUES (?, ?, ?)";
    try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
        ps.setString(1, item.getName());
        ps.setDouble(2, item.getPrice());
        ps.setInt(3, item.getQuantity());
        ps.executeUpdate();
    } catch (SQLException e) {
        System.err.println("Error save: " + e.getMessage());
    }
}
```

### `service/EventDiscountStrategy.java`

Implementasi `DiscountStrategy` untuk diskon Event 12.12 sebesar 12%, menggantikan `NoDiscountStrategy`.

```java
public double calculateDiscount(double totalAmount) {
    return totalAmount * 0.12;
}

public String getDiscountName() {
    return "Event 12.12 (12%)";
}
```

### Perubahan di `Responsi.java`

Hanya mengganti implementasi yang dipakai:

```java
// Sebelum
CartRepository repository = new FakeCartRepository();
DiscountStrategy discountStrategy = new NoDiscountStrategy();

// Sesudah
CartRepository repository = new MySQLCartRepository();
DiscountStrategy discountStrategy = new EventDiscountStrategy();
```

## DDL Database

```sql
CREATE TABLE `cart_items` (
    `id`         INT AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(255) NOT NULL UNIQUE,
    `price`      DOUBLE NOT NULL,
    `quantity`   INT NOT NULL DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Database dan tabelnya dibuat otomatis waktu aplikasi pertama kali dijalankan.

## Cara menjalankan

1. Pastikan MySQL sudah nyala
1. Sesuaikan `DB_PASS` di `DatabaseConnection.java` dengan password MySQL kamu (default kosong)
1. Buka project di NetBeans, Clean and Build, lalu Run