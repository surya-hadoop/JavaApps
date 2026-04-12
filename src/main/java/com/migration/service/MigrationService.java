package com.migration.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import com.migration.model.Contact;
import com.migration.repository.ContactRepository;

public class MigrationService {
    private final ContactRepository repository;

    public MigrationService(ContactRepository repository) {
        this.repository = repository;
    }

    public void migrate(String csvPath) throws IOException, SQLException {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line = reader.readLine();
            if (line == null)
                return;

            // Simple header detection
            if (isHeader(line)) {
                line = reader.readLine();
            }

            while (line != null) {
                Contact contact = parseLine(line);
                repository.save(contact);
                line = reader.readLine();
            }
        }
    }

    public Contact parseLine(String line) {
        String[] parts = line.split(",", -1);
        return new Contact(
                parts.length > 0 ? parts[0].trim() : "",
                parts.length > 1 ? parts[1].trim() : "",
                parts.length > 2 ? parts[2].trim() : "");
    }

    private boolean isHeader(String line) {
        String lower = line.toLowerCase();
        return lower.contains("name") || lower.contains("email");
    }
}