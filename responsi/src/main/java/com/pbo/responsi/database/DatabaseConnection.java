package com.pbo.responsi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DB_HOST = "localhost";
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = "cart_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // ganti password MySQL kamu

    private static final String URL =
        "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {}

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, DB_USER, DB_PASS);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver tidak ditemukan.", e);
            }
        }
        return connection;
    }

    public static void initializeDatabase() {
        String baseUrl =
            "jdbc:mysql://" + DB_HOST + ":" + DB_PORT
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Jakarta";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection c = DriverManager.getConnection(baseUrl, DB_USER, DB_PASS);
                 Statement s = c.createStatement()) {
                s.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS `" + DB_NAME + "` "
                    + "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci"
                );
            }

            try (Connection c = DriverManager.getConnection(URL, DB_USER, DB_PASS);
                 Statement s = c.createStatement()) {
                s.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS `cart_items` ("
                    + "`id`         INT AUTO_INCREMENT PRIMARY KEY, "
                    + "`name`       VARCHAR(255) NOT NULL UNIQUE, "
                    + "`price`      DOUBLE NOT NULL, "
                    + "`quantity`   INT NOT NULL DEFAULT 1, "
                    + "`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
                );
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver tidak ditemukan!", e);
        } catch (SQLException e) {
            throw new RuntimeException(
                "Gagal inisialisasi database!\n"
                + "Pastikan MySQL berjalan dan konfigurasi sudah benar.\n"
                + "Error: " + e.getMessage(), e
            );
        }
    }
}
