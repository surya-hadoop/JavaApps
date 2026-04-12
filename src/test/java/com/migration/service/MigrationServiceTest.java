package com.migration.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.migration.model.Contact;

public class MigrationServiceTest {
    @Test
    public void testParseLine_CorrectlyMapsFields() {
        MigrationService service = new MigrationService(null); // No repo needed for unit test
        String csvLine = "John Doe, john@example.com, 123456";

        Contact result = service.parseLine(csvLine);

        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("123456", result.getPhone());
    }
}