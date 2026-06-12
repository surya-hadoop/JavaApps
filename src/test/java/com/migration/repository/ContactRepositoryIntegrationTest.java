package com.migration.repository;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Before;
import org.junit.Test;

import com.migration.model.Contact;

public class ContactRepositoryIntegrationTest {
    private Connection connection;
    private ContactRepository repository;

    @Before
    public void setUp() throws Exception {
        String url = "jdbc:mysql://localhost:3306/contact_vault?useSSL=false&allowPublicKeyRetrieval=true";

        // Retry logic: MySQL takes time to boot up internally
        int maxRetries = 10;
        for (int i = 0; i < maxRetries; i++) {
            try {
                connection = DriverManager.getConnection(url, "root", "root_password");
                break; // Success!
            } catch (Exception e) {
                if (i == maxRetries - 1)
                    throw e; // Last attempt failed
                System.out.println("Database not ready, retrying in 2 seconds... (" + (i + 1) + "/" + maxRetries + ")");
                Thread.sleep(2000);
            }
        }

        repository = new ContactRepository(connection);
        repository.initSchema();

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate("DELETE FROM contacts");
        }
    }

    @Test
    public void testSave_InsertsDataIntoRealDb() throws Exception {
        Contact contact = new Contact("Surya Test", "surya@test.com", "999-999");

        repository.save(contact);

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT count(*) FROM contacts WHERE name = 'Surya Test'")) {
            rs.next();
            assertEquals(1, rs.getInt(1));
        }
    }
}