package com.migration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.migration.model.Contact;
import com.migration.service.MigrationService;

public class ContactMigratorTest {

    @Test
    public void testParseLine_ValidData() {
        // We pass null for the repository because we aren't hitting the DB in a Unit
        // Test
        MigrationService service = new MigrationService(null);
        String line = "Ava Johnson,ava.johnson@example.com,555-0101";

        Contact result = service.parseLine(line);

        assertEquals("Ava Johnson", result.getName());
        assertEquals("ava.johnson@example.com", result.getEmail());
        assertEquals("555-0101", result.getPhone());
    }
}