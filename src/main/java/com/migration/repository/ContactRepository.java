package com.migration.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.migration.model.Contact;

public class ContactRepository {
    private final Connection connection;

    public ContactRepository(Connection connection) {
        this.connection = connection;
    }

    public void initSchema() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), email VARCHAR(255), phone VARCHAR(50))";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void save(Contact contact) throws SQLException {
        String sql = "INSERT INTO contacts (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, contact.getName());
            ps.setString(2, contact.getEmail());
            ps.setString(3, contact.getPhone());
            ps.executeUpdate();
        }
    }
}