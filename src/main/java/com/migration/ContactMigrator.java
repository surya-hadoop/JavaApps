package com.migration;

import java.sql.Connection;
import java.sql.DriverManager;

import com.migration.repository.ContactRepository;
import com.migration.service.MigrationService;

public class ContactMigrator {
    public static void main(String[] args) {
        // 1. Configuration from Environment (Safe & Modern)
        String csvFile = env("CSV_PATH", "src/main/resources/contacts.csv");
        String jdbcUrl = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                env("DB_HOST", "localhost"),
                env("DB_PORT", "3306"),
                env("DB_NAME", "contact_vault"));

        System.out.println("Starting Migration using file: " + csvFile);

        // 2. Try-with-resources (Automatic Connection Closing)
        try (Connection conn = DriverManager.getConnection(
                jdbcUrl,
                env("DB_USER", "root"),
                env("DB_PASSWORD", "root_password"))) {

            // 3. Dependency Injection (Manual)
            ContactRepository repo = new ContactRepository(conn);
            MigrationService service = new MigrationService(repo);

            // 4. Execution
            repo.initSchema();
            service.migrate(csvFile);

            System.out.println("✅ CSV Migration completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Migration failed!");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}